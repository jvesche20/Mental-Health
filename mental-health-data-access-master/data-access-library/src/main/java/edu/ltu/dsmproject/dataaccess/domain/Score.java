package edu.ltu.dsmproject.dataaccess.domain;

import java.util.ArrayList;
import java.util.List;

public class Score implements Comparable<Score>{
	public int dID;
	public int matchPercentage;
	public List<String> matches;
	
	public Score(int dID, int matchPercentage, List<String> matches){
		this.dID = dID;
		this.matchPercentage = matchPercentage;
		this.matches = new ArrayList<>(matches);
	}
	
    public Integer getPercentage() {
        return matchPercentage;
    }

	@Override
	public int compareTo(Score o) {
		// TODO Auto-generated method stub
		return this.getPercentage().compareTo(o.getPercentage());
	}
}
