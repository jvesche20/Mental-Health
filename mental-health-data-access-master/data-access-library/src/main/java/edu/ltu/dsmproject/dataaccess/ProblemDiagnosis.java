package edu.ltu.dsmproject.dataaccess;

import edu.ltu.dsmproject.dataaccess.dao.PatientsDatabaseAccessObject;
import edu.ltu.dsmproject.dataaccess.domain.Keyword;
import edu.ltu.dsmproject.dataaccess.domain.Results;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProblemDiagnosis {
	String problemDescription;
	public Map<String, Integer> values;
	public List<Results> results = new ArrayList<>();
	
	public ProblemDiagnosis(String problemDescription) {
		this.problemDescription = problemDescription;
		values = new HashMap<>();
	}

	/**
	 * Sentence 1: -------can't sleep------     sleep -1
	 * Sentence 2: -------very----sleep--------	sleep +2
	 * 
	 * 
	 * Final result:
	 * keywords    value
	 * sleep		-1+2=1
	 */
	public List<Results> runDiagnosis() {
		// 0. prepare keyword sets
		List<Keyword> keywords = PatientsDatabaseAccessObject.getInstance().getKeywords();
		
		Map<String, Keyword> illnessKeywords = new HashMap<>();
		Map<String, Keyword> modWords = new HashMap<>();

		for (Keyword keyword: keywords) {
			if (keyword.type.equals("Key"))  {
				illnessKeywords.put(keyword.word.toLowerCase(), keyword); 
			}
			else if (keyword.type.equals("Mod"))  {
				modWords.put(keyword.word.toLowerCase(), keyword);
			}
			else {
				throw new RuntimeException("Unknown keyword type: " + keyword.type);
			}
		}
		
		
		// 1. separate by sentence (full stop, comma and semicolon)
		String[] sentences = problemDescription.split("\\s*[,.;]\\s*");
		
		for (String sentence : sentences) {
			// 2. get all words in sentence
			String[] words = sentence.split("\\s+");
			
			int modValue = 1;
			Map<String, Integer> sentenceValues = new HashMap<String, Integer>();

			for(String word: words) {
				word = word.toLowerCase();
				
				// add keyword value
				Keyword keyword = illnessKeywords.get(word);
				if (keyword != null) {
					// this word is a keyword
					int val = 0;
					sentenceValues.put(word, val + 1);
				}
				
				// apply modifier (multiplicative)
				Keyword mod = modWords.get(word);
				if (mod != null) {
					modValue *= mod.modValue;
				}
			}
			
			// merge sentence results into overall results
			for (Map.Entry<String, Integer> entry : sentenceValues.entrySet()) {
				int val = values.getOrDefault(entry.getKey(), 0);
				val += entry.getValue() * modValue;
				values.put(entry.getKey(), val);
				this.results.add(new Results(entry.getKey(), val));
			}
		}
		
		// finished
		System.out.println("Found " + values.size() + " keywords:");

		System.out.println("Keyword, Value");
		for (Map.Entry<String, Integer> entry : values.entrySet()) {
			System.out.println(entry.getKey() + ", " + entry.getValue());
		}
		
		return this.results;
	}
	
	/*
	 * Feeling sad or having a depressed mood
	 * Loss of interest or pleasure in activities once enjoyed
	 * Changes in appetite — weight loss or gain unrelated to dieting
	 * Trouble sleeping or sleeping too much
	 * Loss of energy or increased fatigue
	 * Increase in purposeless physical activity (e.g., hand-wringing or pacing) or slowed movements and speech (actions observable by others)
	 * Feeling worthless or guilty
	 * Difficulty thinking, concentrating or making decisions
	 * Thoughts of death or suicide
	 
	 */
	
	public Map<String, Integer> makeDiagnosis(String problemDescription) {
		ProblemDiagnosis diagnosis = new ProblemDiagnosis(problemDescription);
		
		diagnosis.runDiagnosis();
		
		return this.values;
	}

	public List<Results> getValues() {
		return this.results;
	}
}
