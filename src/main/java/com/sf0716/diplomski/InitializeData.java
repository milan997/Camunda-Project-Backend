package com.sf0716.diplomski;

import java.util.List;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class InitializeData {
	
	private final String PASSWORD =  "asd";
	private final String WORKFLOW = "WORKFLOW";
	
	@Autowired
	private IdentityService identityService;
	@Autowired
	private RuntimeService runtimeService;
	
	/**
	 * Adding users and user groups and...
	 */
	@EventListener(ApplicationReadyEvent.class)
	public void onAppReady() {
	    //deleteAllProcessInstances();
	    clearDatabase();
	    addData();
	}
	
	private void deleteAllProcessInstances() {
		List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery().list();
		for (ProcessInstance proccesInstance : processInstances)
			runtimeService.deleteProcessInstance(proccesInstance.getId(), "fuck you");
	}
	/**
	 * Delete all users and groups beside "demo" and "camunda-admin|
	 */
	private void clearDatabase() {
		List<User> users = identityService.createUserQuery().list();
		for (User user : users) 
			if(!user.getId().equals("demo"))
				identityService.deleteUser(user.getId());
		List<Group> groups = identityService.createGroupQuery().list();
		for (Group group : groups) {
			if(!group.getId().equals("camunda-admin"))
				identityService.deleteGroup(group.getId());
		}
	}
	
	private void addData() {
		//////// USERS //////////////////////////////////////
		User student = createUser("pera", "Pera", "Peric");
		User referentSS = createUser("reufik", "Reufik", "Dza");
		User rukovodilacSP = createUser("rajko", "Rajko", "Rajkovic");
		User dekan = createUser("dejan", "Dejan", "Radovan");
		
		User profesor1 = createUser("profesor1");
		User profesor2 = createUser("profesor2");
		User profesor3 = createUser("profesor3");
		User profesor4 = createUser("profesor4");
		User profesor5 = createUser("profesor5");
		User profesor6 = createUser("profesor6");
		User profesor7 = createUser("profesor7");
		User profesor8 = createUser("profesor8");
		User profesor9 = createUser("profesor9");
		
		////// GROUPS /////////////////////////////////////////////////
		Group groupStudent = createGroup("student");
		Group groupReferentSS = createGroup("referentSS");
		Group groupRukovodilacSP = createGroup("rukovodstvo");
		Group groupProfesor = createGroup("profesor");
		Group groupDekan = createGroup("dekan");

		////// MEMBERSHIPS ////////////////////////////////////////////////		
		identityService.createMembership(student.getId(), groupStudent.getId());
		identityService.createMembership(referentSS.getId(), groupReferentSS.getId());
		identityService.createMembership(rukovodilacSP.getId(), groupRukovodilacSP.getId());
		identityService.createMembership(dekan.getId(), groupDekan.getId());
		
		identityService.createMembership(profesor1.getId(), groupProfesor.getId());
		identityService.createMembership(profesor2.getId(), groupProfesor.getId());
		identityService.createMembership(profesor3.getId(), groupProfesor.getId());
		identityService.createMembership(profesor4.getId(), groupProfesor.getId());
		identityService.createMembership(profesor5.getId(), groupProfesor.getId());
		identityService.createMembership(profesor6.getId(), groupProfesor.getId());
		identityService.createMembership(profesor7.getId(), groupProfesor.getId());
		identityService.createMembership(profesor8.getId(), groupProfesor.getId());
		identityService.createMembership(profesor9.getId(), groupProfesor.getId());
	}
	
	private User createUser(String id, String firstName, String lastName) {
		User user = identityService.newUser(id);
		user.setEmail("milan.997@hotmail.com");
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setPassword(PASSWORD);
		identityService.saveUser(user);
		return user;
	}
	
	private User createUser(String id) {
		return createUser(id, id, id);
	}
	
	private Group createGroup(String id) {
		Group group = identityService.newGroup(id);
		group.setName(id);
		group.setType(WORKFLOW);
		identityService.saveGroup(group);
		return group;
	}
}