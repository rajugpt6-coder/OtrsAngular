package com.techment.OtrsSystem.Service;

import com.techment.OtrsSystem.Repository.GenderRepository;
import com.techment.OtrsSystem.Repository.RoleRepository;
import com.techment.OtrsSystem.Repository.UserRepository;
import com.techment.OtrsSystem.Security.JwtProvider;
import com.techment.OtrsSystem.domain.CustomerServiceRepresentative;
import com.techment.OtrsSystem.domain.Gender;
import com.techment.OtrsSystem.domain.Role;
import com.techment.OtrsSystem.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private static final String ACTIVATION_STATUS = "deactive";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private GenderRepository genderRepository;

    @Autowired
    private JwtProvider jwtProvider;

//    @Autowired
//    public UserService(UserRepository userRepository, AuthenticationManager authenticationManager,
//                       RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder, JwtProvider jwtProvider) {
//        this.userRepository = userRepository;
//        this.authenticationManager = authenticationManager;
//        this.roleRepository = roleRepository;
//        this.passwordEncoder = passwordEncoder;
//        this.jwtProvider = jwtProvider;
//    }

    /**
     * Sign in a user into the application, with JWT-enabled authentication
     *
     * @param username  username
     * @param password  password
     * @return Optional of the Java Web Token, empty otherwise
     */
//    public Optional<String> signin(String username, String password) {
//        LOGGER.info("New user attempting to sign in");
//        Optional<String> token = Optional.empty();
//        Optional<User> user = userRepository.findByEmail(username);
//
//        if (user.isPresent()) {
//            try {
//                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
//                token = Optional.of(jwtProvider.createToken(username, user.get().getRoles()));
//            } catch (AuthenticationException e){
//                LOGGER.info("Log in failed for user {}", username);
//            }
//        }
//        return token;
//    }

    public  String signin(String username, String password) {
        LOGGER.info("New user attempting to sign in");
        String token = "";
        Optional<User> user = userRepository.findByEmail(username);
        String rtn="";

        if (user.isPresent()) {
            try {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
                token = jwtProvider.createToken(username, user.get().getRoles());
                rtn= "{\"status\":\"success\"," +
                        "\"id\":" +"\""+user.get().getId()+"\""+
                        ",\"email\":" +"\""+user.get().getEmail()+"\""+
                        ",\"phoneNo\":" +"\""+user.get().getPhoneNo()+"\""+
                        ",\"role\":" +"\""+user.get().getRoles()+"\""+
                        ",\"token\":" +"\""+token+"\""+
                        '}';

            } catch (AuthenticationException e){
                rtn= "{\"status\":\"failure\",\"msg\":\"Incorrect user name or password !!\"}";
            }
        }
        else{
            rtn= "{\"status\":\"failure\",\"msg\":\"please sign up !!\"}";
        }
        return rtn;
    }

    /**
     * Create a new user in the database.
     *
     * @param username username
     * @param password password
     * @param firstName first name
     * @param lastName last name
     * @return Optional of user, empty if the user already exists.
     */
    public Optional<User> signup(String username, String password, String firstName, String lastName, String middleName, String phoneNo, String employeeId,
                                 String workingNumber, String landline, String gender) {
        LOGGER.info("New user attempting to sign up");
        Optional<User> user = Optional.empty();
        return createUser(user,"ROLE_ADMIN", username, password, firstName, lastName, middleName, phoneNo, employeeId,
                workingNumber, landline, gender);
    }

    public Page<User> getAll(Pageable pageable) throws NoSuchElementException {
        return userRepository.findAll(pageable);
    }

    public Optional<User> findUserById (long id) {   return userRepository.findById(id);   }

    public Boolean isExist(long id){
        return userRepository.existsById(id);
    }

    public CustomerServiceRepresentative getCustomerServiceRepresentative(long id){
        return userRepository.findCustomerServiceRepresentativeById(id);
    }

    public Optional<User> createResolver( String username, String password, String firstName,
                                          String lastName, String middleName, String phoneNo, String employeeId,
                                          String workingNumber, String landline, String gender) {
        Optional<User> user = Optional.empty();
        return createUser(user,"ROLE_CSR",  username, password, firstName,
                 lastName,  middleName,  phoneNo, employeeId, workingNumber,
                landline, gender);
    }

    private Optional<User> createUser(Optional<User> user, String role, String username, String password, String firstName,
                                      String lastName, String middleName, String phoneNo, String employeeId, String workingNumber,
                                      String landline, String genderName) {


        if (!userRepository.findByEmail(username).isPresent()) {
            Optional<Role> roles = roleRepository.findByRoleName(role);
            Optional<Gender> gender = genderRepository.findByGenderName(genderName);
            user = Optional.of(userRepository.save(new User(username,
                    passwordEncoder.encode(password),
                    firstName,
                    middleName,
                    lastName,
                    employeeId,
                    ACTIVATION_STATUS,
                    workingNumber,
                    landline,
                    phoneNo,
                    Arrays.asList(roles.get()),
                    gender.get()
            )));
        }
        return user;
    }


    public void deleteUser(long id) {
         userRepository.deleteById(id);
    }

    public void updateProfile(long id, String username, String firstName,
                              String lastName, String middleName, String phoneNo){



        if(userRepository.existsById(id)){
            User user = userRepository.findById(id).get();
            user.setEmail(username);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setMiddleName(middleName);
            user.setPhoneNo(phoneNo);
            userRepository.save(user);

        }
    }

    public void updateActivationStatus(long id, String activation) {
        if(userRepository.existsById(id)){
            User user = userRepository.findById(id).get();
            user.setActivationStatus(activation);
            userRepository.save(user);
        }

    }

    public User findUserByEmail(String email, String token)  throws NoSuchElementException {
        if(email.equalsIgnoreCase(jwtProvider.getUsername(token))) {
            return userRepository.findByEmail(email).orElseThrow(() ->
                    new NoSuchElementException("No users found"));
        }
        return null;
    }

    public void updateRole(long id, String role) {
        if(userRepository.existsById(id)){
            List<Role> roles = new ArrayList<>();
            roles.add(roleRepository.findByRoleName(role).get());
            User user = userRepository.findById(id).get();
            user.setRoles(roles);
            userRepository.save(user);
        }
    }
}
