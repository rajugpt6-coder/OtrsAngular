package com.techment.OtrsSystem.Controller;

import com.techment.OtrsSystem.Security.JwtProvider;
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
                loginDto.getLastName(), loginDto.getMiddleName(), loginDto.getPhoneNo(), loginDto.getEmployeeId(),
                loginDto.getWorkingNumber(), loginDto.getLandline(), loginDto.getGender()).orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST,"User already exists"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Optional<User> getUserDetails(@PathVariable("id") long id) {
        return userService.findUserById(id);
    }

    @GetMapping("/myDetails/{email}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CSR') or hasRole('ROLE_USER')")
    public User getMyDetails(@PathVariable("email") String email, @RequestHeader(value="Authorization") String token ) {

            return userService.findUserByEmail(email, token);

    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CSR')")
    public Page<User> getAllUsers(Pageable pageable) {
        return userService.getAll(pageable);
    }

    @PostMapping("/resolver/{department}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public Optional<User> createResolver(@RequestBody @Validated LoginDto loginDto, @PathVariable("department") String department) {
        return userService.createResolver(loginDto.getUsername(), loginDto.getPassword(), loginDto.getFirstName(),
                loginDto.getLastName(), loginDto.getMiddleName(), loginDto.getPhoneNo(), loginDto.getEmployeeId(),
                loginDto.getWorkingNumber(), loginDto.getLandline(), loginDto.getGender(), department);
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
    public void updateProfile(@PathVariable("id") long id, @RequestBody UserDto userDto,
                              @RequestHeader(value = "Authorization") String token) {

        userService.updateProfile(id,userDto.getUsername(), userDto.getFirstName(), userDto.getLastName(),
                userDto.getMiddleName(), userDto.getPhoneNo(), token);
    }

    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public void activateUser(@PathVariable("id") long id) {
        userService.updateActivationStatus(id, STATUS_ACTIVE);
    }

    @PatchMapping("/role/admin/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public  void  makeAdmin(@PathVariable("userId") long id){
        userService.updateRole(id, "ROLE_ADMIN");
    }

    //
    @GetMapping("/search/employeeId/{employeeId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Page<User> getUsersByEmployeeId(@PathVariable("employeeId") String employeeId, Pageable pageable) {
        return userService.findUsersByEmployeeId(employeeId, pageable);
    }

    @GetMapping("/search/firstName/{firstName}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Page<User> getUsersByFirstName(@PathVariable("firstName") String firstName, Pageable pageable) {
        return userService.findUsersByFirstName(firstName, pageable);
    }

    @GetMapping("/search/activationStatus/{activationStatus}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Page<User> getUsersByActivationStatus(@PathVariable("activationStatus") String activationStatus, Pageable pageable) {
        return userService.findUsersByActivationStatus(activationStatus, pageable);
    }


}
