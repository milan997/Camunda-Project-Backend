package com.sf0716.diplomski.controller;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sf0716.diplomski.dto.UserDTO;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;

@Controller
@RequestMapping("/api")
@Consumes(MediaType.APPLICATION_JSON_VALUE)
@Produces(MediaType.APPLICATION_JSON_VALUE)
public class AuthController {

	@Autowired
	private IdentityService identityService;
	
	@PostMapping("/login")
	public ResponseEntity<UserDTO> login(HttpSession session, 
			@RequestBody Map<String, String> params) {
		String username = params.get("username");
		String password = params.get("password");
		
		if (!identityService.checkPassword(username, password))
			return new ResponseEntity<UserDTO>(HttpStatus.UNAUTHORIZED);
		
		session.setAttribute("userId", username);
		
		User user = identityService
				.createUserQuery().userId(username).singleResult();
		List<Group> groups = identityService.createGroupQuery().groupMember(username).list();
		//zapamtiti jel admin u sesiji
		boolean isAdmin = false;
		for (Group group: groups) {
			if (group.getId().equals("camunda-admin")) {
				isAdmin = true;
				break;
			}
		}
		
		session.setAttribute("isAdmin", isAdmin);
		return new ResponseEntity<UserDTO>(new UserDTO(user, groups), HttpStatus.OK);
	}
	
	
	@PostMapping("/logout")
	public ResponseEntity<Void> logout(HttpSession session) {
		session.invalidate();
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}

}