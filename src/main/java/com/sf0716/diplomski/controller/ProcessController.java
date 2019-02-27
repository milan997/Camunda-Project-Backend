package com.sf0716.diplomski.controller;

import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/processes")
@Consumes(MediaType.APPLICATION_JSON_VALUE)
@Produces(MediaType.APPLICATION_JSON_VALUE)
public class ProcessController {

	public static final String PROCESS_DEFINITION_ID = "Process_Diplomski";

	@Autowired
	private IdentityService identityService;
	@Autowired
	private RuntimeService runtimeService;

	@PostMapping()
	public ResponseEntity<Void> startProcess(HttpSession session) {
		String userId = (String) session.getAttribute("userId");
		if (userId == null)
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

		identityService.setAuthenticatedUserId(userId);
		runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_ID);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
	@DeleteMapping("/{processId}")
	public ResponseEntity<Void> delete (
			HttpSession session,
			@PathVariable String processId) {
		String userId = (String) session.getAttribute("userId");
		boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
		
		if (userId == null)
			return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
		if (!isAdmin)
			return new ResponseEntity<Void>(HttpStatus.FORBIDDEN);
		
		runtimeService.deleteProcessInstance(processId, "terminated by admin");
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		
	}
}
