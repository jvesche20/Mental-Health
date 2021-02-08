package edu.ltu.dsmproject.dataaccess.domain;

public class Symptom {
	public int symptomID;
	public int disorderID;
	public String symptomName;
	public int questionID;
	
	public Symptom(int symptomID, int disorderID, String symptomName, int questionID) {
		this.symptomID = symptomID;
		this.disorderID = disorderID;
		this.symptomName = symptomName;
		this.questionID = questionID;
	}

	public Symptom() {
		// TODO Auto-generated constructor stub
	}
}
