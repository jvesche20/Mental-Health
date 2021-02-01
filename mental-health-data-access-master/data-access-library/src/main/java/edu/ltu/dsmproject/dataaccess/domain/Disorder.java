package edu.ltu.dsmproject.dataaccess.domain;

public class Disorder {
	public int disorderID;
	public String disorderName;
	public int parentDisorderID;
	public String disorderFeatures;
	public String wordMatch;

	public Disorder(int id, String name, int pid, String df, String wm){
		this.disorderID = id;
		this.disorderName = name;
		this.parentDisorderID = pid;
		this.disorderFeatures = df;
		this.wordMatch = wm;
	}

	public Disorder() {
		// TODO Auto-generated constructor stub
	}
}
