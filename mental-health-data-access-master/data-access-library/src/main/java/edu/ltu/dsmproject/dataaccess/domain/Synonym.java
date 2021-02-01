package edu.ltu.dsmproject.dataaccess.domain;

public class Synonym {
	public String inputWord;
	public String keyword;
	public String word;
	public int symptomID;
	
	public Synonym(String inputWord, String keyword) {
		this.inputWord = inputWord;
		this.keyword = keyword;
	}
}
