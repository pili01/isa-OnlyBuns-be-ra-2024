package rs.ac.uns.ftn.informatika.jpa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.informatika.jpa.authentification.JwtResponse;
import rs.ac.uns.ftn.informatika.jpa.authentification.TokenBasedAuthentication;
import rs.ac.uns.ftn.informatika.jpa.dto.LoginRequest;
import rs.ac.uns.ftn.informatika.jpa.dto.UserDTO;
import rs.ac.uns.ftn.informatika.jpa.model.Role;
import rs.ac.uns.ftn.informatika.jpa.model.User;
import rs.ac.uns.ftn.informatika.jpa.repository.RoleRepository;
import rs.ac.uns.ftn.informatika.jpa.service.UserService;
import rs.ac.uns.ftn.informatika.jpa.mapper.UserDTOMapper;
import rs.ac.uns.ftn.informatika.jpa.token.Token;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;


    @Autowired
    private UserDTOMapper userDTOMapper;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private Token jwtToken;


    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            User user = userService.findByEmail(loginRequest.getEmail()).orElse(null);

            // Provera da li je nalog verifikovan
            if (user == null || !user.isEnabled()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Account not verified. Please verify your email.");
            }

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = jwtToken.generateToken(user.getEmail());

            /*Role authenticatedRole = userService.findRoleByName("AUTHENTICATED")
                    .orElseThrow(() -> new RuntimeException("Role not found"));
            user.setRole(authenticatedRole);
            userService.updateUser(user);*/


            // Vraćamo JSON objekat sa `access_token` poljem
            Map<String, String> response = new HashMap<>();
            response.put("access_token", jwt);

            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }






    // Fetch all users

//    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> usersDTO = userService.findAll().stream()
                .map(userDTOMapper::fromUsertoDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(usersDTO);
    }

    // Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable int id) {
        return userService.findOne(id)
                .map(userDTOMapper::fromUsertoDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Create a new user
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO) {
        User user = userDTOMapper.fromDTOtoUser(userDTO);
        user = userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDTOMapper.fromUsertoDTO(user));
    }


    /*
    // Update an existing user
    @PutMapping
    public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UserDTO userDTO) {
        Optional<User> existingUserOpt = userService.findOne(userDTO.getId());

        if (existingUserOpt.isPresent()) {
            User existingUser = existingUserOpt.get();

            existingUser.setUsername(userDTO.getUsername());
            existingUser.setEmail(userDTO.getEmail());
            existingUser.setFirstName(userDTO.getFirstName());
            existingUser.setLastName(userDTO.getLastName());
            existingUser.setEnabled(userDTO.isEnabled());

            // Pretvaranje String role naziva u Role objekte (ako koristiš String-Role mapiranje)
            List<Role> roles = userDTO.getRoles().stream()
                    .map(roleName -> roleRepository.findByName(roleName)
                            .orElseThrow(() -> new IllegalArgumentException("Invalid role: " + roleName)))
                    .collect(Collectors.toList());

            existingUser.setRoles(roles);

            User updatedUser = userService.save(existingUser);
            return ResponseEntity.ok(userDTOMapper.fromUsertoDTO(updatedUser));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }*/




    // Delete a user by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        if (userService.existsById(id)) {
            userService.remove(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/getUserByName/{username}")
    public ResponseEntity<UserDTO> getUserByUsername(@PathVariable String username) {
        return userService.findByUsername(username)
                .map(userDTOMapper::fromUsertoDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    // Get user by email
    @GetMapping("/findByEmail")
    public ResponseEntity<UserDTO> getUserByEmail(@RequestParam String email) {
        return userService.findByEmail(email)
                .map(userDTOMapper::fromUsertoDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Check if email exists
    @GetMapping("/existsByEmail")
    public ResponseEntity<Boolean> existsByEmail(@RequestParam String email) {
        return ResponseEntity.ok(userService.existsByEmail(email));
    }

    // Check if username exists
    @GetMapping("/existsByUsername")
    public ResponseEntity<Boolean> existsByUsername(@RequestParam String username) {
        return ResponseEntity.ok(userService.existsByUsername(username));
    }

    // Register a new user
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@Valid @RequestBody UserDTO userDTO) {
        try {
            System.out.println("First Name: " + userDTO.getFirstName());
            System.out.println("Last Name: " + userDTO.getLastName());

            userService.registerUser(userDTO);

            // Koristimo HashMap za kreiranje JSON odgovora
            Map<String, String> response = new HashMap<>();
            response.put("message", "User registered successfully! Please verify your email.");

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (MessagingException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Failed to send verification email");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // Verify user's email
    @GetMapping("/verify")
    public ResponseEntity<String> verifyUser(@RequestParam("email") String email) {
        boolean isVerified = userService.verifyUser(email);
        if (isVerified) {
            return ResponseEntity.ok("User verified successfully!");
        } else {
            return ResponseEntity.badRequest().body("Verification failed or user already verified");
        }
    }
}
