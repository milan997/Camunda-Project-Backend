package com.sf0716.diplomski.handlers;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;

/***
 * Converts time duration from minutes to PT*M
 * @author alowishusad
 *
 */
public class ConvertTimeHandler implements ExecutionListener {

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		String rokZaIzradu = (String) execution.getVariable("rokZaIzradu");
		rokZaIzradu = "PT" + rokZaIzradu + "M";
		System.out.println("Rok za izradu produzen za: " + rokZaIzradu);
		execution.setVariable("rokZaIzradu", rokZaIzradu);
	}
	
}