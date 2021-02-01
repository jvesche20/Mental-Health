package edu.ltu.dsmproject.dataaccess.domain;

import java.util.ArrayList;
import java.util.List;

public class Q {
	public Question question;
	public ArrayList<Integer> symptoms;
	public String symName;
	public int diagnosisID;
	
	public Q(Question question, List<Integer> symptoms, String symName, int diagnosisID){
		this.question = question;
		this.symptoms = new ArrayList<>(symptoms);
		this.symName = symName;
		this.diagnosisID = diagnosisID;
	}
}
