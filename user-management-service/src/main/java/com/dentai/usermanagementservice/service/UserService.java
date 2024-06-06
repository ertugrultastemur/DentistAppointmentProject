package com.dentai.usermanagementservice.service;

import com.dentai.usermanagementservice.client.AuthenticationServiceClient;
import com.dentai.usermanagementservice.dto.PatientDto;
import com.dentai.usermanagementservice.dto.UserDto;
import com.dentai.usermanagementservice.exception.InvalidUserDataException;
import com.dentai.usermanagementservice.exception.PasswordEncoderException;
import com.dentai.usermanagementservice.exception.UserAlreadyExistException;
import com.dentai.usermanagementservice.exception.UserNotFoundException;
import com.dentai.usermanagementservice.model.Role;
import com.dentai.usermanagementservice.model.User;
import com.dentai.usermanagementservice.repository.UserRepository;
import com.dentai.usermanagementservice.util.HeaderUtil;
import com.dentai.usermanagementservice.util.ModelMapperService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

    Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final AuthenticationServiceClient authenticationServiceClient;

    private final ModelMapperService modelMapperService;


    public UserService(UserRepository userRepository, AuthenticationServiceClient authenticationServiceClient, ModelMapperService modelMapperService) {
        logger.trace("UserService: ctor started");
        this.userRepository = userRepository;
        this.authenticationServiceClient = authenticationServiceClient;
        this.modelMapperService = modelMapperService;
        logger.trace("UserService: ctor finished");
    }


    public UserDto getUserById(Long id) {
        logger.trace("UserService: getUserById started");
        User user = userRepository.findById(id).orElseThrow(() -> {
            logger.error("UserService: getUserById | User not found by id: {}", id);
            return new UserNotFoundException("User not found by id: " + id);});
        logger.info("UserService: getUserById finished. ");

        return UserDto.convert(user);
    }

    public UserDto getUserByEmail(String email) {
        logger.trace("UserService: getUserByEmail started");
        User user = userRepository.findByEmail(email).orElseThrow(() -> {
            logger.error("UserService: getUserByEmail | User not found by email: {}", email);
            return new UserNotFoundException("User not found by email: " + email);});
        logger.trace("UserService: getUserByEmail finished");
        return UserDto.convert(user);
    }

    public List<UserDto> getAllNames() {
        logger.trace("UserService: getAllNames started");
        logger.trace("UserService: getAllNames finished");
        List<User> users = userRepository.findAll();
        return users.stream().map(u -> UserDto.builder().id(u.getId()).name(u.getName()).build()).collect(Collectors.toList());
    }

    public List<UserDto> getAllUsers() {
        logger.trace("UserService: getAllUsers started");
        logger.trace("UserService: getAllUsers finished");
        return userRepository.findAll()
                .stream()
                .map(UserDto::convert)
                .collect(Collectors.toList());
    }
    public String getEmailById(Long id) {
        logger.trace("UserService: getEmailById started");
        User user = userRepository.findById(id).orElseThrow(() -> {
            logger.error("UserService: getEmailById | User not found by id: {}", id);
            return new UserNotFoundException("User not found by id: " + id);});
        logger.trace("UserService: getEmailById finished");
        return user.getEmail();
    }
    public UserDto createUser(UserDto userDto, HttpServletRequest request) {
        logger.trace("UserService: createUser started");
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            logger.error("UserService: createUser | User already exists by email: {}", userDto.getEmail());
            throw new UserAlreadyExistException("User already exists by email: " + userDto.getEmail());
        }
        if (userDto.getEmail().isEmpty()||userDto.getRoles().size()>3||userDto.getRoles().isEmpty()) {
            logger.error("UserService: createUser | User roles must be between 1 and 3 and email cannot be empty");
            throw new InvalidUserDataException("User roles must be between 1 and 3 and email cannot be empty");
        }
        EnumSet<Role> allowedRoles = EnumSet.of(Role.ROLE_TECHNICIAN, Role.ROLE_DENTIST, Role.ROLE_ADMIN);
        if (!allowedRoles.containsAll(userDto.getRoles())) {
            logger.error("UserService: createUser | User roles must be Technician, Dentist or Admin");
            throw new InvalidUserDataException("User roles must be Technician, Dentist or Admin");
        }

        ResponseEntity<String> response = authenticationServiceClient.passwordEncoder(userDto.getPassword());
        if (response == null ) {
            logger.error("UserService: createUser | Password encoding failed");
            throw new PasswordEncoderException("Password encoding failed");
        }
        String password = response.getBody();


        User user = User.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .password(password)
                .roles(userDto.getRoles())
                .phoneNumber(userDto.getPhoneNumber())
                .address(userDto.getAddress())
                .age(userDto.getAge())
                .patients(new ArrayList<>())
                .profession(userDto.getProfession())
                .createdBy(HeaderUtil.getUserId(request))
                .build();
        user = userRepository.save(user);
        logger.info("UserService: userRepository.save finished.");
        logger.trace("UserService: createUser finished");
        return UserDto.convert(user);
    }


    @Modifying
    @Transactional
    public UserDto updateUser(UserDto userDto, HttpServletRequest request) {
        logger.trace("UserService: updateUser started");
        User user = userRepository.findByEmail(userDto.getEmail()).orElseThrow(() -> {
            logger.error("UserService: updateUser | User not found by email: {}", userDto.getEmail());
            return new UserNotFoundException("User not found by email: " + userDto.getEmail());});
        logger.info("UserService: userRepository.findByEmail finished.");

        if (userDto.getRoles() != null && userDto.getRoles().size()>3) {
            logger.error("UserService: updateUser | User roles must be between 1 and 3 and email cannot be empty");
            throw new InvalidUserDataException("User roles must be between 1 and 3 and email cannot be empty");
        }
        EnumSet<Role> allowedRoles = EnumSet.of(Role.ROLE_TECHNICIAN, Role.ROLE_DENTIST, Role.ROLE_ADMIN);
        if (userDto.getRoles() != null && !allowedRoles.containsAll(userDto.getRoles())) {
            logger.error("UserService: updateUser | User roles must be Technician, Dentist or Admin");
            throw new InvalidUserDataException("User roles must be Technician, Dentist or Admin");
        }

        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            ResponseEntity<String> response = authenticationServiceClient.passwordEncoder(userDto.getPassword());
            if (response == null ) {
                logger.error("UserService: createUser | Password encoding failed");
                throw new PasswordEncoderException("Password encoding failed");
            }
            userDto.setPassword(response.getBody());

        }
        user.getOperations().clear();
        if (userDto.getOperations() != null) {
            user.getOperations().addAll(userDto.getOperations());
        }
        this.modelMapperService.forRequest().map(userDto, user);

        user.setUpdatedBy(HeaderUtil.getUserId(request));

        logger.info("UserService: updateUser user updated.");
        logger.trace("UserService: updateUser finished");
        return UserDto.convert(userRepository.save(user));
    }

    @Modifying
    @Transactional
    public void deleteUser(String email, HttpServletRequest request) {
        logger.trace("UserService: deleteUser started");
        Long userId = HeaderUtil.getUserId(request);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found by email: " + email));
        logger.info("UserService: userRepository.findByEmail finished.");
        user.setDeleted(true);
        user.setUpdatedBy(userId);
        userRepository.save(user);
        logger.info("UserService: deleteUser soft deleted. ");
        logger.trace("UserService: deleteUser finished");
    }

    @Modifying
    @Transactional
    public void deleteUserById(Long id, HttpServletRequest request) {
        logger.trace("UserService: deleteUserById started");
        Long userId = HeaderUtil.getUserId(request);
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found by id: " + id));
        logger.info("UserService: userRepository.findById finished.");
        userRepository.softDeleteById(user.getId(), userId);
        logger.info("UserService: deleteUserById soft deleted. ");
        logger.trace("UserService: deleteUserById finished");
    }



}
