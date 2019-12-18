package com.in28minutes.restufulwebservices.helloworld;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

	@Autowired
	public MessageSource messageSource;
	@GetMapping(path="/hello")
	public String hello() {
		return "Hello Rest Controller";
	}
	
	@GetMapping(path="/hello-bean")
	public HelloBean helloBean() {
		return new HelloBean("Hello");
	}
	
	@GetMapping(path="/hello-bean/path-variable/{name}")
	public HelloBean helloBeanPathVariable(@PathVariable String name) {
		return new HelloBean(String.format("Hello world, %s", name));
	}
	
	@GetMapping(path="/hello-bean-internationalized")
	public String helloWorldInternationalized() {
		return messageSource.getMessage("good.morning.message", null, LocaleContextHolder.getLocale());
	}
}
