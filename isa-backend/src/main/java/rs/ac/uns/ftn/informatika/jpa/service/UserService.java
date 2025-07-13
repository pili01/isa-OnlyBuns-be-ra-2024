package rs.ac.uns.ftn.informatika.jpa.service;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.uns.ftn.informatika.jpa.dto.UserDTO;
import rs.ac.uns.ftn.informatika.jpa.mapper.UserDTOMapper;
import rs.ac.uns.ftn.informatika.jpa.model.Role;
import rs.ac.uns.ftn.informatika.jpa.model.User;
import rs.ac.uns.ftn.informatika.jpa.repository.RoleRepository;
import rs.ac.uns.ftn.informatika.jpa.repository.UserRepository;
import rs.ac.uns.ftn.informatika.jpa.repository.UserRepositoryCustom;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;




import java.nio.charset.StandardCharsets;

@Service
public class UserService {


    private BloomFilter<String> usernameBloomFilter;




    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRepositoryCustom userRepositoryCustom;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserDTOMapper userDTOMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void initBloomFilter() {
        long estimatedUserCount = userRepository.count();
        usernameBloomFilter = BloomFilter.create(
            Funnels.stringFunnel(StandardCharsets.UTF_8),
            estimatedUserCount == 0 ? 1000 : estimatedUserCount,
            0.01 // 1% greške
        );
        userRepository.findAll().forEach(user -> usernameBloomFilter.put(user.getUsername()));
    }

    public Optional<User> findOne(int id) {
        return Optional.ofNullable(userRepository.findById(id).orElse(null));
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Page<User> findAll(Pageable page) {
        return userRepository.findAll(page);
    }

    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email));
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsById(int id) {
        return userRepository.existsById(id);
    }

    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }

    public void remove(int id) {
        userRepository.deleteById(id);
    }





    public boolean checkUsernameInBloomFilter(String username) {
        // Proverava da li korisničko ime možda postoji
        return usernameBloomFilter.mightContain(username);
    }

    public void addUsernameToBloomFilter(String username) {
        // Dodaje novo korisničko ime u Bloom Filter
        usernameBloomFilter.put(username);
    }


    @Transactional(isolation = Isolation.SERIALIZABLE) // SERIALIZABLE je najsigurniji nivo izolacije
    public User registerUser(UserDTO userDTO) throws MessagingException {
        try {
            // Simuliraj konkurentne zahteve - čeka 5 sekundi
            Thread.sleep(5000);

            if (checkUsernameInBloomFilter(userDTO.getUsername())) {
                // Ako Bloom Filter kaže da korisničko ime možda postoji, proveri u bazi
                if (existsByUsername(userDTO.getUsername())) {
                    throw new IllegalArgumentException("Username already exists");
                }
            }

            // Proveri da li korisnik već postoji
            if (existsByEmail(userDTO.getEmail())) {
                throw new IllegalArgumentException("User with the given email or username already exists");
            }

            // Kreiraj novog korisnika
            User user = new User();
            user.setUsername(userDTO.getUsername());
            user.setEmail(userDTO.getEmail());
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            user.setFirstName(userDTO.getFirstName());
            user.setLastName(userDTO.getLastName());
            user.setAddress((userDTO.getAddress()));
            user.setEnabled(false);
            user.setCreationTime(LocalDateTime.now());

            // Postavi default ulogu
            Role defaultRole = roleRepository.findByName("NOT_AUTHENTICATED")
                    .orElseThrow(() -> new IllegalArgumentException("Role 'NOT_AUTHENTICATED' not found"));
            user.setRole(defaultRole);

            // Sačuvaj korisnika
            user = userRepository.save(user);

            // Dodaj korisničko ime u Bloom filter
            addUsernameToBloomFilter(user.getUsername());

            // Pošalji email za verifikaciju
            String verificationLink = "http://localhost:8080/api/users/verify?email=" + user.getEmail();
            emailService.sendVerificationEmail(userDTO, verificationLink);

            return user;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Thread interrupted during registration", e);
        }
    }


    @Transactional
    public boolean verifyUser(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null && !user.isEnabled()) {
            user.setEnabled(true);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Transactional
    public void cleanUpUsers() {
        List<User> unverified = userRepository.findAllUnverified();
        for (User user : unverified) {
            if (!user.isEnabled() && user.getCreationTime().isBefore(LocalDateTime.now().minusDays(3))) {
                userRepository.delete(user);
            }
        }
    }


    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User updateUser(User user) {
        if (userRepository.existsById(user.getId())) {
            return userRepository.save(user);
        } else {
            throw new IllegalArgumentException("User with ID " + user.getId() + " does not exist.");
        }
    }


    public Optional<Role> findRoleByName(String roleName) {
        return roleRepository.findByName(roleName);
    }

    public String getUserRoleByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        return user.getRole().toString();
    }

    public Page<User> findAllWithFilters(Pageable pageable, String firstName, String lastName, String email, Integer minPosts, Integer maxPosts, String sort) {
        return userRepositoryCustom.findAllWithFilters(pageable, firstName, lastName, email, minPosts, maxPosts, sort);
    }

    public Page<User> findAllFollowersWithFilters(Pageable pageable, String firstName, String lastName, String email, Integer minPosts, Integer maxPosts, String sort, int userId) {
        return userRepositoryCustom.findAllFollowersWithFilters(pageable, firstName, lastName, email, minPosts, maxPosts, sort, userId);
    }

    public Page<User> findAllFollowingsWithFilters(Pageable pageable, String firstName, String lastName, String email, Integer minPosts, Integer maxPosts, String sort, int userId) {
        return userRepositoryCustom.findAllFollowingsWithFilters(pageable, firstName, lastName, email, minPosts, maxPosts, sort, userId);
    }

    @Transactional(readOnly = true)
    public UserDTO findChatsByUsername(String userName) {
        Optional<User> user = userRepository.findByUsername(userName);
        if (user.isPresent()) {
            Hibernate.initialize(user.get().getChats());
        }
        return userDTOMapper.fromUsertoDTO(user.get());
    }

    public Page<User> searchUsrsByUsername(Pageable pageable, String search) {
        return userRepository.findAllByUsernameContainingIgnoreCase(pageable,search);
    }

    public void changePassword(String username, String oldPassword, String newPassword) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BadCredentialsException("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }



    public void updateUserProfile(String username, String firstname, String lastname, String address) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Ažuriranje samo prosleđenih podataka
        if (firstname != null) {
            user.setFirstName(firstname);
        }
        if (lastname != null) {
            user.setLastName(lastname);
        }
        if (address != null) {
            user.setAddress(address);
        }

        userRepository.save(user);
    }


}
