package com.in28minutes.restufulwebservices.user;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class UserJPAResource {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PostRepository postRepository;
	
	@RequestMapping(path = "/jpa/users/{id}", method = RequestMethod.GET)
	public Resource<User> retrieveUserById(@PathVariable Integer id) {
		Optional<User> user = userRepository.findById(id);
		if(!user.isPresent()) {
			throw new UserNotFoundException("id-" + id);
		}
		
		Resource<User> resource = new Resource<User>(user.get());
		ControllerLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAllUsers());
		resource.add(linkTo.withRel("all-users"));
		return resource;
	}
	
	@DeleteMapping(path = "/jpa/users/{id}")
	public void deleteUser(@PathVariable Integer id) {
		userRepository.deleteById(id);
	}
	
	@RequestMapping(path="/jpa/users", method = RequestMethod.GET )
	public List<User> retrieveAllUsers(){
		return userRepository.findAll();
	}
	
	@PostMapping("/jpa/users")
	public ResponseEntity<Object> createUser(@RequestBody User user) {
		User savedUsers = userRepository.save(user);
		URI location = ServletUriComponentsBuilder.
					 fromCurrentRequest().
		             path("/{id}").
		             buildAndExpand(savedUsers.getId()).toUri();
		
		return ResponseEntity.created(location).build();
	}
	
	@RequestMapping(path="/jpa/users/{id}/post", method = RequestMethod.GET )
	public List<Post> retrieveAllPostForUser(@PathVariable int id){
		Optional<User> userOptional = userRepository.findById(id);
		
		if(!userOptional.isPresent())
			throw new UserNotFoundException("id-" +id);
		return userOptional.get().getPost();
		
	}
	
	@PostMapping("/jpa/users/{id}/post")
	public ResponseEntity<Object> createPostForUser(@PathVariable int id, @RequestBody Post post) {
		
		Optional<User> userOptional = userRepository.findById(id);
		if(!userOptional.isPresent()) {
			throw new UserNotFoundException("id-" + id);
		}
		User savedUsers = userOptional.get();
		post.setUser(savedUsers);
		
		postRepository.save(post);
		URI location = ServletUriComponentsBuilder.
					 fromCurrentRequest().
		             path("/{id}").
		             buildAndExpand(post.getId()).toUri();
		
		return ResponseEntity.created(location).build();
	}
}
