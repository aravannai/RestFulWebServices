package com.in28minutes.restufulwebservices.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class UserDaoService {

	private static List<User> users = new ArrayList<User>();
	private static Integer userCount = 5;
	
	static {
		users.add(new User(1, "Sathish", new Date()));
		users.add(new User(2, "Naveena", new Date()));
		users.add(new User(3, "Srihari", new Date()));
		users.add(new User(4, "Dharanidharan", new Date()));
		users.add(new User(5, "Lavanya", new Date()));
	}
	
	public List<User> findAll(){
		return users;
	}
	
	public User save(User u) {
		if(u.getId()==null) {
			u.setId(++userCount);
		}
		users.add(u);
		return u;
	}
	
	public User findById(int id) {
		for(User user: users) {
			if(user.getId()==id) {
				return user;
			}
		}
		return null;
	}
	
	public User deleteById(int id) {
		Iterator<User> iterator = users.iterator();
		while(iterator.hasNext()) {
			User u = iterator.next();
			if(u.getId()==id) {
				iterator.remove();
				return u;
			}
		}
		
		return null;
		
	}
}
