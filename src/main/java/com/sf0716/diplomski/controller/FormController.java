package com.sf0716.diplomski.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;

import org.camunda.bpm.engine.BadUserRequestException;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.exception.NullValueException;
import org.camunda.bpm.engine.impl.persistence.entity.UserEntity;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.variable.VariableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/tasks")
@Consumes(MediaType.APPLICATION_JSON_VALUE)
@Produces(MediaType.APPLICATION_JSON_VALUE)
public class FormController {
	
	@Autowired
	private FormService formService;
	
	@Autowired
	private TaskService taskService;
	
	@GetMapping("/{taskId}/form-variables")
	public ResponseEntity<Map<String, Object>> getFormVariables(HttpSession session, @PathVariable String taskId) {
		String userId = (String) session.getAttribute("userId");
		if (userId == null)
			return new ResponseEntity<Map<String, Object>>(HttpStatus.UNAUTHORIZED);
		
		try {
			VariableMap variables = formService.getTaskFormVariables(taskId);
			Map<String, Object> variablesDTO = convertVariablesToDto(variables);
			return new ResponseEntity<Map<String, Object>>(variablesDTO, HttpStatus.OK);
		} catch (BadUserRequestException e) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping("/{taskId}/submit-form")
	public ResponseEntity<Void> submitForm(
			HttpSession session,
			@PathVariable String taskId,
			@RequestBody Map<String, Object> properties) {
		String userId = (String) session.getAttribute("userId");
		boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
		if (userId == null)
			return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
		Task task = taskService.createTaskQuery().taskId(taskId).list().get(0);
		if (!userId.equals(task.getAssignee()) && !isAdmin) {
			return new ResponseEntity<Void>(HttpStatus.FORBIDDEN);
		}
		
		try {
			formService.submitTaskForm(taskId, properties);
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		} catch (NullValueException e) {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping("/{taskId}/submit-file")
	@Consumes(MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Void> submitFile(
			HttpSession session,
			@PathVariable String taskId,
			@RequestParam("upload") MultipartFile file) {
		String userId = (String) session.getAttribute("userId");
		boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
		if (userId == null)
			return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
		Task task = taskService.createTaskQuery().taskId(taskId).list().get(0);
		if (!userId.equals(task.getAssignee()) && !isAdmin) {
			return new ResponseEntity<Void>(HttpStatus.FORBIDDEN);
		}
		
		try {
			String fileName = taskId + ".pdf";
			
			File imagesFolder = new File("data/");
			if(!imagesFolder.exists())
				imagesFolder.mkdir();
			imagesFolder.setWritable(true);
			
			try (InputStream in = file.getInputStream();
				OutputStream out = new FileOutputStream(new File("data/" + fileName))) {
					int read = 0;
					byte[] buffer = new byte[1024];

					while((read = in.read(buffer)) != -1)
						out.write(buffer, 0 , read);
			}
			catch (Exception e) {
				e.getMessage();
				e.printStackTrace();
			}
			
			Map<String, Object> properties = new HashMap<>();
			properties.put("upload", fileName);
			formService.submitTaskForm(taskId, properties);
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		} catch (NullValueException e) {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, Object> convertVariablesToDto(VariableMap variables){
		Map<String, Object> variablesDTO = new HashMap<>();
		for (Map.Entry<String, Object> variable : variables.entrySet()) {
			String key = variable.getKey();
			Object value = variable.getValue();
			if (value instanceof String)
				value = (String) value;
			else if (value instanceof Boolean)
				value = (boolean) value;	
			else if (value instanceof Integer)
				value = (int) value;	
			else if (value instanceof UserEntity)
				value = ((UserEntity) value).getId();
			else if (value instanceof List) 
				value = (List<String>) value;
			else 
				throw new ClassCastException("Neuspijelo kastovanje...");
			
			variablesDTO.put(key, value);
		}
		return variablesDTO;
	}
}
