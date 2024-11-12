package rs.ac.uns.ftn.informatika.jpa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.informatika.jpa.dto.PostCreationDTO;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping(value = "api/posts")
public class PostController {

    private static final String UPLOAD_DIR = "storage/";
    private final Path externalImageStorageLocation = Paths.get("storage");

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

    @PreAuthorize("hasAuthority('AUTHENTICATED')")
    @PostMapping(consumes = "application/json", value = "/add")
    public ResponseEntity<PostDTO> addPost(@RequestBody PostCreationDTO postCreationDTO){
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
//        Optional<User> author = userService.findOne(userId);
        Optional<User> author = userService.findByUsername(userName);

        if(!author.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        UserDTO authorDTO = userDTOMapper.fromUsertoDTO(author.get());
        postCreationDTO.setAuthor(authorDTO);
        Post post = postDTOMapper.fromDTOtoPost(postCreationDTO);
//        post = author.get().addPost(post);
//        post.setAuthor(author.get());
        post = postService.save(post);
        PostDTO postDTO = postDTOMapper.toDTO(post);
//        postDTO.setId(post.getId());
        return new ResponseEntity<>(postDTO, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('AUTHENTICATED')")
    @PostMapping(value= "/images")
    public ResponseEntity<String> uploadImage(@RequestParam("image") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("No file uploaded");
        }

        try {
            // Create the uploads directory if it doesn't exist
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // Get the file name and create a path to save it
            String fileName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
            Path path = Paths.get(UPLOAD_DIR + fileName);
            // Copy the file to the server directory
            Files.copy(file.getInputStream(), path);

            // Return the file path as a response
            return new ResponseEntity<>(fileName, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error uploading the file");
        }
    }
    @PostMapping("/upload/string")
    public ResponseEntity<String> greet(@RequestBody String name) {
        String message = "Hello, " + name + "!";
        // Return ResponseEntity with status OK and the greeting message
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('AUTHENTICATED')")
    @PatchMapping(value = "/like/{postId}")
    public ResponseEntity<PostDTO> addLike(@PathVariable Integer postId){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<User> author = userService.findByUsername(username);
        if(!author.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Post post = postService.addLike(postId, author.get());

        if(post == null){
            return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        post.setComments(new HashSet<>());
        PostDTO postDTO = postDTOMapper.toDTO(post);
        return new ResponseEntity<>(postDTO, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('AUTHENTICATED')")
    @PatchMapping(value = "/unlike/{postId}")
    public ResponseEntity<PostDTO> removeLike(@PathVariable int postId){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<User> author = userService.findByUsername(username);
        if(!author.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Post post = postService.removeLike(postId, author.get());

        if(post == null){
            return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        post.setComments(new HashSet<>());
        PostDTO postDTO = postDTOMapper.toDTO(post);
        return new ResponseEntity<>(postDTO, HttpStatus.OK);
    }


    @GetMapping("/images/{imageName}")
    public ResponseEntity<Resource> getImage(@PathVariable String imageName) {
        try {
            Path imagePath = externalImageStorageLocation.resolve(imageName).normalize();
            Resource resource = new UrlResource(imagePath.toUri());

            if (resource.exists()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception ex) {
            return ResponseEntity.status(500).build();
        }
    }
}
