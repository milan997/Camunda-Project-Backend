package com.sf0716.diplomski.handlers;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.delegate.Expression;

public class SetMessageHandler implements ExecutionListener {

	private Expression message;
	
	@Override
	public void notify(DelegateExecution execution) throws Exception {
		String message = (String) this.message.getValue(execution);
		execution.setVariable("message", message);
	}

	public Expression getMessage() {
		return message;
	}

	public void setMessage(Expression message) {
		this.message = message;
	}
}