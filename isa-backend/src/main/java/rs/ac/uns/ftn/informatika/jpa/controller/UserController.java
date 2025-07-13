package rs.ac.uns.ftn.informatika.jpa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import rs.ac.uns.ftn.informatika.jpa.dto.*;
import rs.ac.uns.ftn.informatika.jpa.model.Location;
import rs.ac.uns.ftn.informatika.jpa.model.Post;
import rs.ac.uns.ftn.informatika.jpa.model.Role;
import rs.ac.uns.ftn.informatika.jpa.model.User;
import rs.ac.uns.ftn.informatika.jpa.repository.LocationRepository;
import rs.ac.uns.ftn.informatika.jpa.repository.RoleRepository;
import rs.ac.uns.ftn.informatika.jpa.repository.UserRepository;
import rs.ac.uns.ftn.informatika.jpa.service.FollowService;
import rs.ac.uns.ftn.informatika.jpa.service.PostService;
import rs.ac.uns.ftn.informatika.jpa.service.UserService;
import rs.ac.uns.ftn.informatika.jpa.mapper.UserDTOMapper;
import rs.ac.uns.ftn.informatika.jpa.token.Token;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PostService postService;

    @Autowired
    private UserDTOMapper userDTOMapper;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private Token jwtToken;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FollowService followService;
    @Autowired
    private LocationRepository locationRepository;


    @CrossOrigin(origins = {"http://localhost:58700", "http://localhost:4200"})
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            User user = userService.findByEmail(loginRequest.getEmail()).orElse(null);

            // Provera da li je nalog verifikovan
            if (user == null || !user.isEnabled()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Account not verified. Please verify your email.");
            }

            // Autentifikacija korisnika
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Generisanje JWT tokena
            String jwt = jwtToken.generateToken(user.getEmail(),user.getUsername());

            // Preuzimanje korisničke uloge
            String role = user.getRole().getName();
            if (role.equals("NOT_AUTHENTICATED")) {
                role = "AUTHENTICATED";
                user.setRole(roleRepository.findByName(role).orElse(null));
                userService.updateUser(user);
            }


            // Vraćamo JSON objekat sa `access_token` i `role` poljem
            Map<String, String> response = new HashMap<>();
            response.put("access_token", jwt);
            response.put("role", role);

            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }


    // Fetch all users

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> usersDTO = userService.findAll().stream()
                .map(userDTOMapper::fromUsertoDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(usersDTO);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/allPaged")
    public List<UserDTO> getAllUsersPaged(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Integer minPosts,
            @RequestParam(required = false) Integer maxPosts,
            @RequestParam(required = false) String sort) {

        Pageable pageable = PageRequest.of(page, size);
        Page<User> usersPage = userService.findAllWithFilters(pageable, firstName, lastName, email, minPosts, maxPosts, sort);

        return usersPage.stream()
                .map(userDTOMapper::fromUsertoDTO)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasAuthority('AUTHENTICATED')")
    @GetMapping("/searchForUsers")
    public List<UserDTO> getAllUsersPaged(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam String search) {

        Pageable pageable = PageRequest.of(page, size);
        Page<User> usersPage = userService.searchUsrsByUsername(pageable,search);

        return usersPage.stream()
                .map(userDTOMapper::fromUsertoDTO)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasAuthority('AUTHENTICATED')")
    @GetMapping("/allChats")
    public ResponseEntity<UserDTO> getUserChats() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        UserDTO userDTO = userService.findChatsByUsername(userName);

        if (userDTO==null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        userDTO.setChats(userDTO.getChats().stream()
                .sorted(Comparator.comparing(ChatDTO::getLastActivity).reversed()) // Sortira po `lastActivity` opadajuće
                .collect(Collectors.toCollection(LinkedHashSet::new)));
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @GetMapping("/allFollowersPaged")
    public List<UserDTO> getAllUserFollowersPaged(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam String username,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Integer minPosts,
            @RequestParam(required = false) Integer maxPosts,
            @RequestParam(required = false) String sort) {

        Optional<User> user = userService.findByUsername(username);

        Pageable pageable = PageRequest.of(page, size);
        Page<User> usersPage = userService.findAllFollowersWithFilters(pageable, firstName, lastName, email, minPosts, maxPosts, sort,user.get().getId());

        return usersPage.stream()
                .map(userDTOMapper::fromUsertoDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/allFollowingsPaged")
    public List<UserDTO> getAllUserFollowingsPaged(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam String username,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Integer minPosts,
            @RequestParam(required = false) Integer maxPosts,
            @RequestParam(required = false) String sort) {

        Optional<User> user = userService.findByUsername(username);

        Pageable pageable = PageRequest.of(page, size);
        Page<User> usersPage = userService.findAllFollowingsWithFilters(pageable, firstName, lastName, email, minPosts, maxPosts, sort,user.get().getId());

        return usersPage.stream()
                .map(userDTOMapper::fromUsertoDTO)
                .collect(Collectors.toList());
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
        String loggedInUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> loggedInUser = userService.findByUsername(loggedInUsername);

        Optional<UserDTO> userDTO=userService.findByUsername(username)
                .map(userDTOMapper::fromUsertoDTO);

        boolean isFollowed = followService.getFollowers(userDTO.get().getId()).stream()
                .anyMatch(follower -> follower.getUsername().equals(loggedInUsername));

        userDTO.get().setUserFollowedByMe(isFollowed);
        return userDTO.map(ResponseEntity::ok)
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


    @PreAuthorize("hasAuthority('AUTHENTICATED')")
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @RequestBody Map<String, String> payload,
            Authentication authentication) {
        // Dobijanje trenutnog korisničkog imena iz konteksta autentifikacije
        String currentUsername = authentication.getName();

        // Ekstrakcija parametara iz payload-a
        String oldPassword = payload.get("oldPassword");
        String newPassword = payload.get("newPassword");

        // Pozivanje metode servisa za promenu lozinke
        userService.changePassword(currentUsername, oldPassword, newPassword);

        // Vraćanje uspešnog odgovora
        return ResponseEntity.ok("Password changed successfully!");
    }

    @PreAuthorize("hasAuthority('AUTHENTICATED')")
    @PutMapping("/update-profile")
    public ResponseEntity<?> updateProfile(@RequestBody Map<String, String> updatedData, Authentication authentication) {
        String currentUsername = authentication.getName();

        // Ažuriranje samo onih podataka koji su prosleđeni
        userService.updateUserProfile(currentUsername,
                updatedData.getOrDefault("firstname", null),
                updatedData.getOrDefault("lastname", null),
                updatedData.getOrDefault("address", null)
        );

        return ResponseEntity.ok("Profile updated successfully!");
    }


    @PutMapping("/{username}/location")
    @PreAuthorize("hasAuthority('AUTHENTICATED')")
    public ResponseEntity<String> updateUserLocation(
            @PathVariable String username,
            @RequestBody LocationDTO locationDTO
    ) {
        Optional<User> userOptional = userService.findByEmail(username);

        if (!userOptional.isPresent()) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        User user = userOptional.get();
        Location location = new Location();
        location.setLatitude(locationDTO.getLatitude());
        location.setLongitude(locationDTO.getLongitude());
        location = locationRepository.save(location);

        user.setLocation(location);
        userService.save(user);

        return new ResponseEntity<>("Location updated successfully", HttpStatus.OK);
    }




    @GetMapping("/currentlocation")
    @PreAuthorize("hasAuthority('AUTHENTICATED')")
    public ResponseEntity<LocationDTO> getUserLocation(@RequestParam String email) {
        Optional<User> user = userService.findByEmail(email);

        if (user.isPresent()) {
            Location location = user.get().getLocation();

            if (location == null ) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            // Vratite DTO sa podacima o lokaciji
            return ResponseEntity.ok(new LocationDTO(location.getId(),location.getLatitude(), location.getLongitude()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }





}
