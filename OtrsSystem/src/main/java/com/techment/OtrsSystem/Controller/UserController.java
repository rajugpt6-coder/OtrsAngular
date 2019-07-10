package com.techment.OtrsSystem.Controller;

import com.techment.OtrsSystem.Service.UserService;
import com.techment.OtrsSystem.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;

import javax.validation.Valid;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private static final String STATUS_ACTIVE = "active";

    @Autowired
    private UserService userService;

    @PostMapping("/signin")
    @ResponseStatus(HttpStatus.OK)
    public String login(@RequestBody @Valid LoginDto loginDto) {
        return userService.signin(loginDto.getUsername(), loginDto.getPassword());
//                .orElseThrow(()->
//                new HttpServerErrorException(HttpStatus.FORBIDDEN, "Login Failed"));
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public User signup(@RequestBody @Valid LoginDto loginDto){
        return userService.signup(loginDto.getUsername(), loginDto.getPassword(), loginDto.getFirstName(),
                loginDto.getLastName(), loginDto.getMiddleName(), loginDto.getPhoneNo()).orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST,"User already exists"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Optional<User> getUserDetails(@PathVariable("id") long id) {
        return userService.findUserById(id);
    }

    @GetMapping("/myDetails/{email}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CSR') or hasRole('ROLE_CSR')")
    public User getMyDetails(@PathVariable("email") String email) {
            return userService.findUserByEmail(email);
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CSR')")
    public Page<User> getAllUsers(Pageable pageable) {
        return userService.getAll(pageable);
    }

    @PostMapping("/resolver")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public Optional<User> createResolver(@RequestBody @Validated LoginDto loginDto) {
        return userService.createResolver(loginDto.getUsername(), loginDto.getPassword(), loginDto.getFirstName(),
                loginDto.getLastName(), loginDto.getMiddleName(), loginDto.getPhoneNo());
    }

    @DeleteMapping("/{id}/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@PathVariable("id") long id){
        userService.deleteUser(id);
    }

    @PutMapping("/{id}/update")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CSR') or hasRole('ROLE_USER')")
    @ResponseStatus(HttpStatus.CREATED)
    public void updateProfile(@PathVariable("id") long id, @RequestBody UserDto userDto) {

        userService.updateProfile(id,userDto.getUsername(), userDto.getFirstName(), userDto.getLastName(),
                userDto.getMiddleName(), userDto.getPhoneNo());
    }

    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public void activateUser(@PathVariable("id") long id) {
        userService.updateActivationStatus(id, STATUS_ACTIVE);
    }
}
