package rs.ac.uns.ftn.informatika.jpa.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rs.ac.uns.ftn.informatika.jpa.dto.ChatDTO;
import rs.ac.uns.ftn.informatika.jpa.dto.UserDTO;
import rs.ac.uns.ftn.informatika.jpa.model.Chat;
import rs.ac.uns.ftn.informatika.jpa.model.Role;
import rs.ac.uns.ftn.informatika.jpa.model.User;

@Component
public class ChatDTOMapper {

    private final ModelMapper modelMapper;

    @Autowired
    private UserDTOMapper userDTOMapper;

    @Autowired
    public ChatDTOMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;

        // Prilagođeno pravilo za ignorisanje neinicijalizovanih kolekcija
        this.modelMapper.getConfiguration().setPropertyCondition(context -> {
            if (context.getSource() instanceof org.hibernate.collection.spi.PersistentCollection) {
                org.hibernate.collection.spi.PersistentCollection collection =
                        (org.hibernate.collection.spi.PersistentCollection) context.getSource();
                return collection.wasInitialized(); // Mapiraj samo ako je inicijalizovano
            }
            return true; // Sve ostalo mapiraj normalno
        });
    }

    public Chat fromDTOtoChat(ChatDTO dto) {
        Chat chat = modelMapper.map(dto, Chat.class);
        System.out.println("Name: " + dto.getName());
        System.out.println("Id: " + dto.getId());
        Role role = new Role();

        return chat;
    }

    public ChatDTO fromChatToDTO(Chat chat) {
        ChatDTO dto = modelMapper.map(chat, ChatDTO.class);
        return dto;
    }
}
