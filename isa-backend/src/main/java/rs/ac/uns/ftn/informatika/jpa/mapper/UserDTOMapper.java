package rs.ac.uns.ftn.informatika.jpa.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rs.ac.uns.ftn.informatika.jpa.dto.UserDTO;
import rs.ac.uns.ftn.informatika.jpa.model.Role;
import rs.ac.uns.ftn.informatika.jpa.model.User;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserDTOMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public UserDTOMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public User fromDTOtoUser(UserDTO dto) {
        User user = modelMapper.map(dto, User.class);
        user.setRoles(roleNamesToRoles(dto.getRoles()));
        return user;
    }

    public UserDTO fromUsertoDTO(User user) {
        UserDTO dto = modelMapper.map(user, UserDTO.class);
        dto.setRoles(rolesToRoleNames(user.getRoles()));
        return dto;
    }

    private List<String> rolesToRoleNames(List<Role> roles) {
        return roles.stream().map(Role::getName).collect(Collectors.toList());
    }

    private List<Role> roleNamesToRoles(List<String> roleNames) {
        return roleNames.stream()
                .map(name -> {
                    Role role = new Role();
                    role.setName(name);
                    return role;
                })
                .collect(Collectors.toList());
    }
}
