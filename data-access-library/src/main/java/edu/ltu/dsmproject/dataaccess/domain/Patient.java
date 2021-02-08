package edu.ltu.dsmproject.dataaccess.domain;

/*Patient object to match DB*/
import java.text.SimpleDateFormat;
import java.util.Date;

public class Patient {
	public int patientID;
	public String patientName;
	public String gender;
	public String problemDescription;
	public String diagnosisDate;
	public String words;
	public String symptoms;
	public int diagnosedID;
	
	public void setDiagnosis(int diagnosedID) {
		this.diagnosedID = diagnosedID;
		String pattern = "MM/dd/yyyy";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

		this.diagnosisDate = simpleDateFormat.format(new Date());
	}
	
	public void setWords(String words) {
		this.words = words;
	}
	
	public Patient(String patientName, String problemDescription, int patientID) {
		this.patientName = patientName;
		this.problemDescription = problemDescription;
		this.patientID = patientID;
	}
	
	public Patient(String patientName, String problemDescription, Boolean isMale, int patientID) {
		this.patientName = patientName;
		this.problemDescription = problemDescription;
		this.gender = isMale ? "Male" : "Female";
		this.patientID = patientID;
		this.symptoms = "";
	}
	
	public Patient(int patientID, int diagnosedID, String problemDescription, String diagnosisDate, String patientName) {
		this.patientName = patientName;
		this.problemDescription = problemDescription;
		this.diagnosedID = diagnosedID;
		this.patientID = patientID;
		this.diagnosisDate = diagnosisDate;
	}

	public Patient(int patientID, int diagnosedID, String problemDescription, String diagnosisDate, String patientName, Boolean isMale) {
		this.patientName = patientName;
		this.gender = isMale ? "Male" : "Female";
		this.problemDescription = problemDescription;
		this.diagnosedID = diagnosedID;
		this.patientID = patientID;
		this.diagnosisDate = diagnosisDate;
	}
	
	//Include gender as a string to retrieve from database
	public Patient(int patientID, int diagnosedID, String problemDescription, String diagnosisDate, String patientName, String gender, String words) {
		this.patientName = patientName;
		this.gender = gender;
		this.problemDescription = problemDescription;
		this.diagnosedID = diagnosedID;
		this.patientID = patientID;
		this.diagnosisDate = diagnosisDate;
		this.words = words;
		this.symptoms = "";
	}

	public Patient() {}

	public int getPatientID() { return patientID; }
	public String getPatientName() { return patientName; }
	public String getGender() { return gender; }
	public int getDiagnosedID() { return diagnosedID; }
	public String getDiagnosisDate() { return diagnosisDate; }
}