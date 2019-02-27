package com.sf0716.diplomski.handlers;

import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StartProcessHandler implements ExecutionListener {

	@Autowired
	private IdentityService identityService;
	
	@Override
	public void notify(DelegateExecution execution) throws Exception {
		List<User> rukovodstvoUsers = identityService.createUserQuery().memberOfGroup("rukovodstvo").list();
		List<String> rukovodstvo = new ArrayList<>();
		for (User user: rukovodstvoUsers) {
			rukovodstvo.add(user.getId());
		}
		execution.setVariable("rukovodstvo",  rukovodstvo);
		
		List<User> profesoriUsers = identityService.createUserQuery().memberOfGroup("profesor").list();
		List<String> profesori = new ArrayList<>();
		for (User user: profesoriUsers) {
			profesori.add(user.getId());
		}
		execution.setVariable("profesori",  profesori);
		
		String userId = (String) execution.getVariable("student");
		User user = identityService.createUserQuery().userId(userId).singleResult();
		execution.setVariable("email",  user.getEmail());
	}
	
}
