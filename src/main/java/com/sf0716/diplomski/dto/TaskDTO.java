package com.sf0716.diplomski.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.engine.task.IdentityLink;
import org.camunda.bpm.engine.task.Task;

public class TaskDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	private String name;
	private String description;
	private String assignee;
	private String processDefinitionId;
	private String formKey;
	private String createTime;
	private String processInstanceId;
	private List<String> candidateGroups = new ArrayList<>();

	public TaskDTO() {
	}

	public TaskDTO(Task task) {
		id = task.getId();
		name = task.getName();
		description = task.getDescription();
		assignee = task.getAssignee();
		processDefinitionId = task.getProcessDefinitionId();
		createTime = task.getCreateTime().toString();
		processInstanceId = task.getProcessInstanceId();

		formKey = "/" + task.getFormKey().split("/")[1];
	}

	public TaskDTO(Task task, List<IdentityLink> identities) {
		this(task);
		for (IdentityLink identityLink : identities) {
			String groupId = identityLink.getGroupId();
			if (groupId != null)
				this.candidateGroups.add(groupId);
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	public String getFormKey() {
		return formKey;
	}

	public void setFormKey(String formKey) {
		this.formKey = formKey;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public List<String> getCandidateGroups() {
		return candidateGroups;
	}

	public void setCandidateGroups(List<String> candidateGroups) {
		this.candidateGroups = candidateGroups;
	}

}
