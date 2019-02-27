package com.sf0716.diplomski.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.task.IdentityLink;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sf0716.diplomski.dto.TaskDTO;

@RestController
@RequestMapping("/api/tasks")
@Consumes(MediaType.APPLICATION_JSON_VALUE)
@Produces(MediaType.APPLICATION_JSON_VALUE)
public class TasksController {

	public static final String ADMIN_GROUP = "camunda-admin";
	
	@Autowired
	private TaskService taskService;
	@Autowired
	private IdentityService identityService;
	
	@GetMapping("/my-tasks") 
	public ResponseEntity<List<TaskDTO>> getTasks(HttpSession session){
		String userId = (String) session.getAttribute("userId");
		if (userId == null)
			return new ResponseEntity<List<TaskDTO>>(HttpStatus.UNAUTHORIZED);
		
		List<Group> candidateGroups = identityService.createGroupQuery().groupMember(userId).list();
		List<String> candidateGroupsIds = new ArrayList<>();
		boolean userIsAdmin = false;
		for (Group group: candidateGroups) {
			if (group.getId().equals(ADMIN_GROUP)) 
				userIsAdmin = true;
			candidateGroupsIds.add(group.getId());
		}
		
		TaskQuery taskQuery = taskService.createTaskQuery().initializeFormKeys();
		// adminu vracamo sve taskove
		if (userIsAdmin) {}	
		// izuzetak su student i profesor koji mogu da vide samo taskove assignovane njima
		else if (userIsStudent(candidateGroupsIds) || userIsProfessor(candidateGroupsIds))
			taskQuery.taskAssignee(userId);
		// By default taskCandidateUser(String), taskCandidateGroup(String) and taskCandidateGroupIn(List) only select not assigned tasks
		else 
			taskQuery.taskCandidateGroupIn(candidateGroupsIds).includeAssignedTasks();	
		
		List<Task> tasks =  taskQuery.orderByTaskCreateTime().desc().list();
		
		List<TaskDTO> tasksDTO = new ArrayList<>();
		for (Task task : tasks) {
			List<IdentityLink> identities = taskService.getIdentityLinksForTask(task.getId()); 
			tasksDTO.add(new TaskDTO(task, identities));
		}
		return new ResponseEntity<List<TaskDTO>>(tasksDTO, HttpStatus.OK);
	}
	
	private boolean userIsProfessor(List<String> candidateGroupsIds) {
		return candidateGroupsIds.size() == 1 && candidateGroupsIds.get(0).equals("profesor");
	}

	private boolean userIsStudent(List<String> candidateGroupsIds) {
		return candidateGroupsIds.size() == 1 && candidateGroupsIds.get(0).equals("student");
	}
	
	@PostMapping("/{taskId}/claim")
	public ResponseEntity<Void> claim (
			HttpSession session,
			@PathVariable String taskId) {
		String userId = (String) session.getAttribute("userId");
		if (userId == null)
			return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
		
		Task task = taskService.createTaskQuery().taskId(taskId).list().get(0);
		if (task.getAssignee() == null) {
			taskService.claim(taskId, userId);
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<Void>(HttpStatus.FORBIDDEN);
		}
		
	}
	
	@PostMapping("/{taskId}/unclaim")
	public ResponseEntity<Void> unclaim (
			HttpSession session,
			@PathVariable String taskId) {
		String userId = (String) session.getAttribute("userId");
		if (userId == null)
			return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
		
		Task task = taskService.createTaskQuery().taskId(taskId).list().get(0);
		if (task.getAssignee().equals(userId)) {
			taskService.claim(taskId, null);
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<Void>(HttpStatus.FORBIDDEN);
		}
		
	}	
}