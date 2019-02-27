package com.sf0716.diplomski.handlers;

import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;

public class PostaviKomisijuHandler implements ExecutionListener {
	
	@Override
	public void notify(DelegateExecution execution) throws Exception {		
		String predsednikKomisije = (String) execution.getVariable("predsednikKomisije");
		String clanKomisije1 = (String) execution.getVariable("clanKomisije1");
		String clanKomisije2 = (String) execution.getVariable("clanKomisije2");
		String clanKomisije3 = (String) execution.getVariable("clanKomisije3");
		
		List<String> komisija = new ArrayList<>();
		komisija.add(predsednikKomisije);
		komisija.add(clanKomisije1);
		if (clanKomisije2 != null)
			komisija.add(clanKomisije2);
		if (clanKomisije3 != null)
			komisija.add(clanKomisije3);
		
		execution.setVariable("komisija", komisija);
	}

}
