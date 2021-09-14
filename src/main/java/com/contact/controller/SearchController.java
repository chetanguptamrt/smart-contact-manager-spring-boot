package com.contact.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.contact.entity.Contact;
import com.contact.entity.User;
import com.contact.service.UserService;

@RestController
public class SearchController {

	@Autowired
	private UserService userService;
	
	@GetMapping("/search/{query}")
	public ResponseEntity<?> search(@PathVariable("query") String query, Principal principal){
		User user = this.userService.getUserByUsername(principal.getName());
		List<Contact> contact = this.userService.findByNameContainingAndUser(query, user);
		return ResponseEntity.ok(contact);
	}

}
