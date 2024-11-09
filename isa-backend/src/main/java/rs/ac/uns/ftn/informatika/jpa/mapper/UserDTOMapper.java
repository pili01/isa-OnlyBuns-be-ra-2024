package rs.ac.uns.ftn.informatika.jpa.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rs.ac.uns.ftn.informatika.jpa.dto.UserDTO;
import rs.ac.uns.ftn.informatika.jpa.model.Role;
import rs.ac.uns.ftn.informatika.jpa.model.User;

@Component
public class UserDTOMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public UserDTOMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public User fromDTOtoUser(UserDTO dto) {
        User user = modelMapper.map(dto, User.class);
        System.out.println("First Name: " + dto.getFirstName());
        System.out.println("Last Name: " + dto.getLastName());
        Role role = new Role();
        role.setName(dto.getRole());
        user.setRole(role);

        return user;
    }

    public UserDTO fromUsertoDTO(User user) {
        UserDTO dto = modelMapper.map(user, UserDTO.class);

        if (user.getRole() != null) {
            dto.setRole(user.getRole().getName());
        }

        return dto;
    }
}
