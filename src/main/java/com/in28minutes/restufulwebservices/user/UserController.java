package com.in28minutes.restufulwebservices.user;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.net.URI;
import java.util.List;

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
public class UserController {

	@Autowired
	public UserDaoService userDao;
	
	@RequestMapping(path = "/users/{id}", method = RequestMethod.GET)
	public Resource<User> retrieveUserById(@PathVariable Integer id) {
		User user = userDao.findById(id);
		if(user == null) {
			throw new UserNotFoundException("id-" + id);
		}
		
		Resource<User> resource = new Resource<User>(user);
		ControllerLinkBuilder linkTo = linkTo(methodOn((this.getClass())).retrieveAllUsers());
		resource.add(linkTo.withRel("all-users"));
		
		return resource;
	}
	
	@DeleteMapping(path = "/users/{id}")
	public void deleteUser(@PathVariable Integer id) {
		User user = userDao.deleteById(id);
		if(user == null) {
			throw new UserNotFoundException("id-" + id);
		}
	}
	
	@RequestMapping(path="/users", method = RequestMethod.GET )
	public List<User> retrieveAllUsers(){
		return userDao.findAll();
	}
	
	@PostMapping("/users")
	public ResponseEntity<Object> createUser(@RequestBody User user) {
		User savedUsers = userDao.save(user);
		URI location = ServletUriComponentsBuilder.
					 fromCurrentRequest().
		             path("/{id}").
		             buildAndExpand(savedUsers.getId()).toUri();
		
		return ResponseEntity.created(location).build();
	}
}
