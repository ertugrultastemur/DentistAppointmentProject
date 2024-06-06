package com.dentai.usermanagementservice.service;

import com.dentai.usermanagementservice.dto.UserDto;
import com.dentai.usermanagementservice.model.User;
import com.dentai.usermanagementservice.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DentistInfoService {

    private final UserRepository userRepository;

    public DentistInfoService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDto> getAllDentists() {
        List<UserDto> userDtos = new ArrayList<>();
        List<User> users = userRepository.findAll();
        users.forEach(u -> {
            UserDto userDto = UserDto.builder()
                    .id(u.getId())
                    .name(u.getName())
                    .email(u.getEmail())
                    .build();

            userDtos.add(userDto);
        });

        return userDtos;
    }
}
