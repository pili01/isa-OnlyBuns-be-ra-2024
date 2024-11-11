package rs.ac.uns.ftn.informatika.jpa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.uns.ftn.informatika.jpa.dto.CommentDTO;
import rs.ac.uns.ftn.informatika.jpa.dto.PostDTO;
import rs.ac.uns.ftn.informatika.jpa.dto.StudentDTO;
import rs.ac.uns.ftn.informatika.jpa.model.Comment;
import rs.ac.uns.ftn.informatika.jpa.model.Post;
import rs.ac.uns.ftn.informatika.jpa.model.Student;
import rs.ac.uns.ftn.informatika.jpa.service.PostService;
import rs.ac.uns.ftn.informatika.jpa.service.StudentService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping(value = "/all")
    public ResponseEntity<List<PostDTO>> getAllPosts() {

        List<Post> posts = postService.findAll();

        List<PostDTO> postsDTO = new ArrayList<>();
        for (Post p : posts) {
            postsDTO.add(new PostDTO(p));
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
        String MyUsername="user";
        Page<Post> posts = postService.findAll(page);

        // convert posts to DTOs
        List<PostDTO> postsDTO = new ArrayList<>();
        for (Post p : posts) {
            PostDTO postDTO=new PostDTO(p);
            postDTO.setLikedByMe(p.getLikers().stream().anyMatch(t->t.getUsername().equalsIgnoreCase(MyUsername)));
            postsDTO.add(postDTO);
        }

        return new ResponseEntity<>(postsDTO, HttpStatus.OK);
    }
}
