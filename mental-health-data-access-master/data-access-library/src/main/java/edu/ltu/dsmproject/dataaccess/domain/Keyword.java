package edu.ltu.dsmproject.dataaccess.domain;

/*Keyword object to match DB*/
public class Keyword {
	public String word;
	public String type;
	public int majorDepressiveDisorder;
	public int attentionDeficitHyperactivityDisorder;
	public int postTraumaticStressDisorder;
	public int modValue;
	
	public Keyword(String word, String type, int majorDepressiveDisorder, int postTraumaticStressDisorder, int attentionDeficitHyperactivityDisorder, int modValue) {
		this.word = word;
		this.type = type;
		this.majorDepressiveDisorder = majorDepressiveDisorder;
		this.attentionDeficitHyperactivityDisorder = attentionDeficitHyperactivityDisorder;
		this.postTraumaticStressDisorder = postTraumaticStressDisorder;
		this.modValue = modValue;
	}
}
