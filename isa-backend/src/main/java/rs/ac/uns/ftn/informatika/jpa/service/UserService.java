package rs.ac.uns.ftn.informatika.jpa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.uns.ftn.informatika.jpa.dto.UserDTO;
import rs.ac.uns.ftn.informatika.jpa.model.Role;
import rs.ac.uns.ftn.informatika.jpa.model.User;
import rs.ac.uns.ftn.informatika.jpa.repository.RoleRepository;
import rs.ac.uns.ftn.informatika.jpa.repository.UserRepository;
import javax.mail.MessagingException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private EmailService emailService;




    @Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<User> findOne(int id) {
        return Optional.ofNullable(userRepository.findById(id).orElse(null));
    }

    public List<User> findAll() {
        return userRepository.findAll();
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


    public User save(User user) {
        return userRepository.save(user);
    }

    public void remove(int id) {
        userRepository.deleteById(id);
    }



    @Transactional
    public User registerUser(UserDTO userDTO) throws MessagingException {
        if (existsByEmail(userDTO.getEmail()) || existsByUsername(userDTO.getUsername())) {
            throw new IllegalArgumentException("User with the given email or username already exists");
        }

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setAddress((userDTO.getAddress()));
        user.setEnabled(false);

        Role defaultRole = roleRepository.findByName("NOT_AUTHENTICATED")
                .orElseThrow(() -> new IllegalArgumentException("Role 'NOT_AUTHENTICATED' not found"));

        user.setRole(defaultRole);

        user = userRepository.save(user);


        // Slanje verifikacionog email-a
        String verificationLink = "http://localhost:8080/api/users/verify?email=" + user.getEmail();
        emailService.sendVerificationEmail(userDTO, verificationLink);

        return user;
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

}
