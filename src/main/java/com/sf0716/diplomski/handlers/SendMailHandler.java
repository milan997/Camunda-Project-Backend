package com.sf0716.diplomski.handlers;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.Expression;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class SendMailHandler implements JavaDelegate {
	
	private static final String subject = "Rezultati procesa izrade diplomskog";
	private static final String from = "alowishus.ad@gmail.com";
	
	private Expression message;
	private Expression email;
	
	@Autowired
    private JavaMailSender emailSender;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		String text = (String) this.message.getValue(execution);
		String to = (String) this.email.getValue(execution);
		
		SimpleMailMessage mail = new SimpleMailMessage(); 
		mail.setTo(to); 
		mail.setFrom(from);
		mail.setSubject(subject); 
		mail.setText(text);
		System.out.println("Sending mail to: " + to);
		emailSender.send(mail);
	}

	public Expression getMessage() {
		return message;
	}

	public void setMessage(Expression message) {
		this.message = message;
	}

	public Expression getEmail() {
		return email;
	}

	public void setEmail(Expression email) {
		this.email = email;
	}
}