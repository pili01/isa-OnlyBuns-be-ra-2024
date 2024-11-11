package rs.ac.uns.ftn.informatika.jpa.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rs.ac.uns.ftn.informatika.jpa.dto.CommentDTO;
import rs.ac.uns.ftn.informatika.jpa.dto.PostDTO;
import rs.ac.uns.ftn.informatika.jpa.dto.UserDTO;
import rs.ac.uns.ftn.informatika.jpa.model.Comment;
import rs.ac.uns.ftn.informatika.jpa.model.Location;
import rs.ac.uns.ftn.informatika.jpa.model.Post;
import rs.ac.uns.ftn.informatika.jpa.model.User;

import java.util.Collections;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class PostDTOMapper {
    private final ModelMapper modelMapper;

    @Autowired
    public PostDTOMapper(ModelMapper modelMapper) { this.modelMapper = modelMapper; }

    public Post fromDTOtoPost(PostDTO postDTO) {
        Location location = modelMapper.map(postDTO.getLocation(), Location.class);
//        Set<Comment> comments = Collections.singleton(modelMapper.map(postDTO.getComments(), Comment.class));
        Set<Comment> comments = new HashSet<Comment>();
        for(CommentDTO commentDTO : postDTO.getComments()) {
            comments.add(modelMapper.map(commentDTO, Comment.class));
        }
        Set<User> likers = new HashSet<User>();
        for(UserDTO userDTO : postDTO.getLikers()) {
            likers.add(modelMapper.map(userDTO, User.class));
        }

        Post post = modelMapper.map(postDTO, Post.class);
//        post.setLocation(location);
//        post.setComments(comments);
//        post.setLikers(likers);

        return post;
    }

    public PostDTO toDTO(Post post) {
        return modelMapper.map(post, PostDTO.class);
    }

    public PostDTO fromPosttoDTO(Post dto) {
        return modelMapper.map(dto, PostDTO.class);
    }

//
//    public Set<PostDTO> toDTOs(Set<Post> posts) {
//        Set<PostDTO> postDTOSet = new HashSet<>();
//        for(Post post : posts) {
//            postDTOSet.add(toDTO(post));
//        }
//        return postDTOSet;
//    }
}
