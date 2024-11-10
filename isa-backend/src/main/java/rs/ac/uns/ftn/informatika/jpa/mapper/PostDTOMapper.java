package rs.ac.uns.ftn.informatika.jpa.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rs.ac.uns.ftn.informatika.jpa.dto.PostDTO;
import rs.ac.uns.ftn.informatika.jpa.dto.TeacherDTO;
import rs.ac.uns.ftn.informatika.jpa.model.Post;
import rs.ac.uns.ftn.informatika.jpa.model.Teacher;

@Component
public class PostDTOMapper {
    private static ModelMapper modelMapper;

    @Autowired
    public PostDTOMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public static Post fromDTOtoPost(PostDTO dto) {
        return modelMapper.map(dto, Post.class);
    }

    public static PostDTO fromPosttoDTO(Post dto) {
        return modelMapper.map(dto, PostDTO.class);
    }
}
