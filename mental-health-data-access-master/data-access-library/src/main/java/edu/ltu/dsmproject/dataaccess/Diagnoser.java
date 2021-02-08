package edu.ltu.dsmproject.dataaccess;

import edu.ltu.dsmproject.dataaccess.dao.PatientsDatabaseAccessObject;
import edu.ltu.dsmproject.dataaccess.domain.*;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

import java.util.List;
import java.util.Properties;

/**
 * Performs diagnosis of patients.
 */
public class Diagnoser {
	private Patient patient;

	private List<Disorder> disorders;
	private ArrayList<Score> scores = new ArrayList<>();
	private List<Symptom> symptoms;

	/**
	 * Creates a new Diagnoser instance with the provided Patient.
	 * @param patient The patient to be diagnosed.
	 * @throws IllegalArgumentException Patient is null.
	 */
	public Diagnoser(Patient patient) {
		if (patient == null) {
			throw new IllegalArgumentException("The patient cannot be null.");
		}

		disorders = PatientsDatabaseAccessObject.getInstance().getDisorders();
		symptoms = PatientsDatabaseAccessObject.getInstance().getSymptoms();
		this.patient = patient;
		
		findAndStorePatientWords();
		if(this.patient.words != null) {
			findWordMatch();
		}
	}

	public void findWordMatch() {
		String[] patientWords = patient.words.split(" ");
		scores.clear();

		for (Disorder disorder: disorders) {
			if (disorder.wordMatch != null) {
				List<String> matches = Arrays.stream(patientWords)
						.filter(word -> disorder.wordMatch.contains(word))
						.collect(Collectors.toList());

				String[] disorderWords = disorder.wordMatch.split(" ");
				Score score = new Score(disorder.disorderID, getMatchPercentage(matches, disorderWords), matches);
				scores.add(score);
			}
		}

		printScores(scores);

		bestOf();
	}

	public List<Score> getScores() {
		return scores;
	}

	public Q nextQ(int id){
		String symptomName = "";
		Symptom hold = new Symptom();
		for(Symptom symptom: symptoms) {
			if(symptom.disorderID == scores.get(id).dID && !patient.symptoms.contains(symptom.symptomName)) {
				symptomName = symptom.symptomName;
				hold = symptom;
				break;
			}
		}

		List<Integer> matchingDisorderIds = getMatchingDisorderIds(symptoms, symptomName);

		return symptomName.contentEquals("")
				? this.nextQ(id + 1)
				: new Q(PatientsDatabaseAccessObject.getInstance().getQuestions().get(hold.questionID - 1), matchingDisorderIds, symptomName, id);
	}

	private static void printScores(ArrayList<Score> scores) {
		for(Score score: scores) {
			System.out.println("ID: " + score.dID + " Percent: " + score.matchPercentage + "%" + " Words: " + score.matches.toString());
		}
	}

	private static int getMatchPercentage(List<String> matches, String[] disorderWords) {
		return (int)(((float)matches.size() / (float)disorderWords.length) * 100);
	}

	private static List<Integer> getMatchingDisorderIds(List<Symptom> symptoms, String symptomName) {
		return symptoms.stream()
				.filter(symptom -> symptom.symptomName.equalsIgnoreCase(symptomName))
				.map(symptom -> symptom.disorderID)
				.collect(Collectors.toList());
	}

	private void bestOf() {
		scores.sort(Collections.reverseOrder());

		for (int x = 0; x < 100; x++) {
			findSymptoms(scores.get(x).dID);
		}

		System.out.println(patient.symptoms);
	}

	private void findSymptoms(int id) {
		List<String> chanceSymptomNames = symptoms.stream()
				.filter(symptom -> symptom.disorderID == id && symptom.symptomName != null)
				.map(symptom -> symptom.symptomName)
				.collect(Collectors.toList());

		StringBuilder patientSymptomsBuilder = new StringBuilder(patient.symptoms);
		HashSet<String> uniqueWords = new HashSet<>(Arrays.asList(patient.words.split(" ")));

		for(String word: uniqueWords) {
			for(String symptomName : chanceSymptomNames) {
				if(word.equals(symptomName) && !patient.symptoms.contains(symptomName)) {
					patientSymptomsBuilder.append(" ").append(word);
				}
			}
		}

		patient.symptoms = patientSymptomsBuilder.toString();
	}

	private void findAndStorePatientWords() {
		if(patient.problemDescription != null) {
			Properties props = new Properties();
			props.setProperty("annotators", "tokenize, ssplit, pos");
			StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

			// read some text in the text variable
			String text = patient.problemDescription;

			// create an empty Annotation just with the given text
			Annotation document = new Annotation(text);

			// run all Annotators on this text
			pipeline.annotate(document);

			List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
			ArrayList<String> tempWords = new ArrayList<String>();
			String outWords = "";
			for (CoreMap sentence : sentences) {
				// traversing the words in the current sentence
				// a CoreLabel is a CoreMap with additional token-specific methods
				for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
					// this is the text of the token
					String word = token.get(CoreAnnotations.TextAnnotation.class);
					// this is the POS tag of the token
					String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);

					System.out.println(String.format("Print: word: [%s] pos: [%s]", word, pos));

					if (pos.equals("NN") || pos.equals("VB") || pos.equals("JJ")) {
						tempWords.add(word);
					}
				}
			}


			for (String t : tempWords){
				outWords = outWords + " " + t;
			}
			System.out.println(String.format("Print: outwords : [%s]", outWords));

			patient.setWords(outWords);
			PatientsDatabaseAccessObject.getInstance().addPatientWords(patient);
		}
	}
}
