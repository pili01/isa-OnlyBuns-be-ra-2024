package rs.ac.uns.ftn.informatika.jpa.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.informatika.jpa.additionalSerices.CommentRateLimited;
import rs.ac.uns.ftn.informatika.jpa.dto.CommentDTO;
import rs.ac.uns.ftn.informatika.jpa.model.Comment;
import rs.ac.uns.ftn.informatika.jpa.model.Post;
import rs.ac.uns.ftn.informatika.jpa.model.User;
import rs.ac.uns.ftn.informatika.jpa.repository.CommentRepository;
import rs.ac.uns.ftn.informatika.jpa.service.CommentService;
import rs.ac.uns.ftn.informatika.jpa.service.PostService;
import rs.ac.uns.ftn.informatika.jpa.service.UserService;

import java.util.Optional;

@RestController
@RequestMapping(value = "api/post/comment")
public class CommentController {

    private final ModelMapper modelMapper;

    @Autowired
    private UserService userService;
    @Autowired
    private PostService postService;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private CommentService commentService;

    public CommentController(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @CommentRateLimited
    @PreAuthorize("hasAuthority('AUTHENTICATED')")
    @PostMapping(consumes = "application/json", value = "/{postId}")
    public ResponseEntity<CommentDTO> addComment(@RequestBody CommentDTO commentDTO, @PathVariable int postId) {
        System.out.println("ALO PA STA JE OVO!!\n");
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
//        Optional<User> author = userService.findOne(userId);
        Optional<User> author = userService.findByUsername(userName);
        Post post = postService.findOne(postId);
        if(!author.isPresent() || post == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if(!commentService.canUserAddComment(author.get().getId())) {
            return new ResponseEntity<>(commentDTO, HttpStatus.TOO_MANY_REQUESTS);
        }

        Comment comment = modelMapper.map(commentDTO, Comment.class);
        comment.setAuthor(author.get());
        comment.setPost(post);
        comment = commentService.save(comment);
        if(comment == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        commentDTO.setId(comment.getId());
        return new ResponseEntity<>(commentDTO, HttpStatus.CREATED);
    }


    @PreAuthorize("hasAuthority('AUTHENTICATED')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<CommentDTO> deleteComment(@PathVariable int id) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
//        Optional<User> author = userService.findOne(userId);
        Optional<User> author = userService.findByUsername(userName);
        if(!author.isPresent())
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        Comment comment = commentService.remove(id, author.get().getId());
        if(comment == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        CommentDTO commentDTO = modelMapper.map(comment, CommentDTO.class);
        return new ResponseEntity<>(commentDTO, HttpStatus.OK);
    }
}
