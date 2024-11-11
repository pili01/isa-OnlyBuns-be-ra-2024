package rs.ac.uns.ftn.informatika.jpa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.informatika.jpa.dto.PostDTO;
import rs.ac.uns.ftn.informatika.jpa.dto.StudentDTO;
import rs.ac.uns.ftn.informatika.jpa.dto.UserDTO;
import rs.ac.uns.ftn.informatika.jpa.mapper.PostDTOMapper;
import rs.ac.uns.ftn.informatika.jpa.mapper.UserDTOMapper;
import rs.ac.uns.ftn.informatika.jpa.model.Post;
import rs.ac.uns.ftn.informatika.jpa.model.Student;
import rs.ac.uns.ftn.informatika.jpa.model.User;
import rs.ac.uns.ftn.informatika.jpa.service.PostService;
import rs.ac.uns.ftn.informatika.jpa.service.StudentService;
import rs.ac.uns.ftn.informatika.jpa.service.UserService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "api/posts")
public class PostController {

    @Autowired
    private PostService postService;
    @Autowired
    private UserService userService;
    @Autowired
    private PostDTOMapper postDTOMapper;
    @Autowired
    private UserDTOMapper userDTOMapper;

    @GetMapping(value = "/all")
    public ResponseEntity<List<PostDTO>> getAllPosts() {

        List<Post> posts = postService.findAll();

        List<PostDTO> postsDTO = new ArrayList<>();
        for (Post p : posts) {
            PostDTO pDTO = new PostDTO(p);
            pDTO.setAuthor(userDTOMapper.fromUsertoDTO(p.getAuthor()));
            postsDTO.add(pDTO);
        }

        return new ResponseEntity<>(postsDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/allPostComments")
    public ResponseEntity<PostDTO> getPostWithComments(@RequestParam Integer id) {

        PostDTO post = postService.findOneWithComments(id);
        if(post == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    // GET /api/posts?page=0&size=5&sort=firstName,DESC
    @GetMapping(value = "/allPaged")
    public ResponseEntity<List<PostDTO>> getPostsPage(Pageable page) {

        // page object holds data about pagination and sorting
        // the object is created based on the url parameters "page", "size" and "sort"
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Page<Post> posts = postService.findAll(page);

        // convert posts to DTOs
        List<PostDTO> postsDTO = new ArrayList<>();
        for (Post p : posts) {
            PostDTO postDTO=new PostDTO(p);
            postDTO.setLikedByMe(p.getLikers().stream().anyMatch(t->t.getUsername().equalsIgnoreCase(username)));
            postsDTO.add(postDTO);
        }

        return new ResponseEntity<>(postsDTO, HttpStatus.OK);
    }


    @PostMapping(consumes = "application/json", value = "/{userId}")
    public ResponseEntity<PostDTO> addPost(@RequestBody PostDTO postDTO, @PathVariable int userId){
        Optional<User> author = userService.findOne(userId);
        if(!author.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        UserDTO authorDTO = userDTOMapper.fromUsertoDTO(author.get());
        postDTO.setAuthor(authorDTO);
        Post post = postDTOMapper.fromDTOtoPost(postDTO);
//        post = author.get().addPost(post);
//        post.setAuthor(author.get());
        post = postService.save(post);
        postDTO.setId(post.getId());
        return new ResponseEntity<>(postDTO, HttpStatus.CREATED);
    }

    @PatchMapping(value = "/like/{postId}/{userId}")
    public ResponseEntity<PostDTO> addLike(@PathVariable int postId, @PathVariable int userId){
        Optional<User> author = userService.findOne(userId);
        if(!author.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Post post = postService.addLike(postId, author.get());
//        Post post = postService.findOneWithLikers(postId);
        if(post == null){
            return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
//        post.addLike(author.get());
//        post = postService.save(post);
        post.setComments(new HashSet<>());
        PostDTO postDTO = postDTOMapper.toDTO(post);
        return new ResponseEntity<>(postDTO, HttpStatus.OK);
    }

    @PatchMapping(value = "/unlike/{postId}/{userId}")
    public ResponseEntity<PostDTO> removeLike(@PathVariable int postId, @PathVariable int userId){
        Optional<User> author = userService.findOne(userId);
        if(!author.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Post post = postService.removeLike(postId, author.get());
//        Post post = postService.findOneWithLikers(postId);
        if(post == null){
            return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
//        post.addLike(author.get());
//        post = postService.save(post);
        post.setComments(new HashSet<>());
        PostDTO postDTO = postDTOMapper.toDTO(post);
        return new ResponseEntity<>(postDTO, HttpStatus.OK);
    }
}
