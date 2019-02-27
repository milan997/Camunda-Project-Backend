package com.sf0716.diplomski.handlers;

import java.util.List;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;

public class MentorHandler implements ExecutionListener {

	@Override
	public void notify(DelegateExecution execution) throws Exception {

		@SuppressWarnings("unchecked")
		List<String> profesori = (List<String>) execution.getVariable("profesori");
		String mentor = (String) execution.getVariable("mentor");
		profesori.remove(mentor);
		execution.setVariable("profesori", profesori);
	}
}