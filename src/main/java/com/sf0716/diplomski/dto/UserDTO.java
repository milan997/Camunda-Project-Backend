package com.sf0716.diplomski.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.identity.User;

public class UserDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String firstName;
	private String lastName;
	private String email;
	private List<String> groups = new ArrayList<>();
	
	public UserDTO () {}
	
	public UserDTO (User user) {
		id = user.getId();
		firstName = user.getFirstName();
		lastName = user.getLastName();
		email = user.getEmail();
	}
	
	/**
	 * Add groups to user
	 * @param user
	 * @param group
	 */
	public UserDTO(User user, List<Group> groups) {
		this(user);
		for (Group group : groups) 
			this.groups.add(group.getId());
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public List<String> getGroups() {
		return groups;
	}

	public void setGroups(List<String> groups) {
		this.groups = groups;
	}
	
	
}
