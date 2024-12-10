package rs.ac.uns.ftn.informatika.jpa.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rs.ac.uns.ftn.informatika.jpa.dto.ChatDTO;
import rs.ac.uns.ftn.informatika.jpa.dto.MessageDTO;
import rs.ac.uns.ftn.informatika.jpa.model.Chat;
import rs.ac.uns.ftn.informatika.jpa.model.Message;
import rs.ac.uns.ftn.informatika.jpa.model.Role;

@Component
public class MessageDTOMapper {

    private final ModelMapper modelMapper;

    @Autowired
    private UserDTOMapper userDTOMapper;

    @Autowired
    public MessageDTOMapper(ModelMapper modelMapper) {
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

    public Message fromDTOtoMessage(MessageDTO dto) {
        Message message = modelMapper.map(dto, Message.class);
        System.out.println("Name: " + dto.getContent());
        System.out.println("Id: " + dto.getId());
        Role role = new Role();

        return message;
    }

    public MessageDTO fromMessageToDTO(Message message) {
        MessageDTO dto = modelMapper.map(message, MessageDTO.class);
        return dto;
    }
}
