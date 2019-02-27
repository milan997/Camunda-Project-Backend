package com.sf0716.diplomski.handlers;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;

/**
 * Clanovi komisije su uneli svoje primedbe
 * postavljamo odluku 
 * @author alowishusad
 *
 */
public class Task09Handler implements ExecutionListener {
	
	@Override
	public void notify(DelegateExecution execution) throws Exception {
		String primedbaPredsednik = (String) execution.getVariable("primedbaPredsednik");
		String primedbaClan1 = (String) execution.getVariable("primedbaClan1");
		String primedbaClan2 = (String) execution.getVariable("primedbaClan2");
		String primedbaClan3 = (String) execution.getVariable("primedbaClan3");
		
		boolean nemaPrimedbi = areEmptyOrNull(primedbaPredsednik, primedbaClan1, primedbaClan2, primedbaClan3);
		
		String odluka = nemaPrimedbi ? "radPrihvacen" : "potrebnaDorada";
		execution.setVariable("odluka", odluka);
	}
	
	private boolean isEmptyOrNull(String s) {
		return s == null || "".equals(s.trim());
	}
	
	private boolean areEmptyOrNull(String... strings) {
		for (String s: strings) 
			if (!isEmptyOrNull(s))
				return false;
		return true;
	}
}
