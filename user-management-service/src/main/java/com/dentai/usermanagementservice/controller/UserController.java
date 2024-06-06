package com.dentai.usermanagementservice.controller;

import com.dentai.usermanagementservice.dto.UserDto;
import com.dentai.usermanagementservice.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/users")
public class UserController {

    Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        logger.trace("UserController: ctor called");
        this.userService = userService;
        logger.trace("UserController: ctor finished");
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable @NotNull Long id) {
        logger.trace("UserController: getUserById called");
        logger.info("UserController: getUserById started.");
        return ResponseEntity.ok(userService.getUserById(id));
    }


    @GetMapping("/getUserByEmail/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable @NotNull String email) {
        logger.trace("UserController: getUserByEmail called");
        logger.info("UserController: getUserByEmail started.");
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @GetMapping("/getEmailById/{id}")
    public ResponseEntity<String> getEmailById(@PathVariable @NotNull Long id) {
        logger.trace("UserController: getEmailById called");
        logger.info("UserController: getEmailById started.");
        return ResponseEntity.ok(userService.getEmailById(id));
    }

    @GetMapping("/getAllNames")
    public ResponseEntity<List<UserDto>> getNameById() {
        logger.trace("UserController: getAllNames called");
        logger.info("UserController: getAllNames started.");
        return ResponseEntity.ok(userService.getAllNames());
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        logger.trace("UserController: getAllUsers called");
        logger.info("UserController: getAllUsers started.");
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping("/addUser")
    public ResponseEntity<UserDto> addUser(@RequestBody @NotNull UserDto userDto , HttpServletRequest request) {
        logger.trace("UserController: addUser called");
        logger.info("UserController: addUser started.");
        return ResponseEntity.ok(userService.createUser(userDto,request));
    }

    @PutMapping("/updateUser")
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody @NotNull UserDto userDto, HttpServletRequest request) {
        logger.trace("UserController: updateUser called");
        logger.info("UserController: updateUser started.");
        return ResponseEntity.ok(userService.updateUser(userDto,request));
    }

    @DeleteMapping("/deleteUser/{email}")
    public ResponseEntity<Void> deleteUser(@PathVariable @NotNull String email, HttpServletRequest request) {
        logger.trace("UserController: deleteUser called");
        userService.deleteUser(email,request);
        logger.info("UserController: deleteUser started.");
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/deleteUserById/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable @NotNull Long id, HttpServletRequest request) {
        logger.trace("UserController: deleteUserById called");
        userService.deleteUserById(id,request);
        logger.info("UserController: deleteUserById started.");
        return ResponseEntity.ok().build();
    }
}
