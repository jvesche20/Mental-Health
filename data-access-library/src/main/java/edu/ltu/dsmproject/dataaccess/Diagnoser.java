package edu.ltu.dsmproject.dataaccess;

import edu.ltu.dsmproject.dataaccess.dao.PatientsDatabaseAccessObject;
import edu.ltu.dsmproject.dataaccess.domain.*;

//import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

import java.util.List;
import java.util.Properties;
import java.util.stream.IntStream;

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

		long startTime = System.currentTimeMillis();

		for (Disorder disorder: disorders) {
			if (disorder.wordMatch != null) {


				List<String> disorderWords = new ArrayList<String>();
				for(Symptom symptom: symptoms) {
					if(symptom.disorderID == disorder.disorderID) {
						disorderWords.add(symptom.symptomName);
					}
				}
				List<String> matches = Arrays.stream(patientWords)
						.filter(word -> disorderWords.contains(word))
						.collect(Collectors.toList());

				if(matches.size() > 1)
				{
//					for (String words : disorderWords) {
////						System.out.println(String.format(words + "== Print: words: 65 ==" + disorderWords.length + "	vdhsvbjsd\n"));
//					}
					Score score = new Score(disorder.disorderID, getMatchPercentage(matches, disorderWords), matches);
					System.out.println("disorder Id : " + score.dID + "score : " + score.matchPercentage + "Number matches:	"+score.matches.size());
					scores.add(score);
				}

			}
		}
		long endTime = System.currentTimeMillis();
		long duration = (endTime - startTime);
		System.out.println(String.format("Duration in milli seconds %s", duration));
		printScores(scores);

		bestOf();
	}

	public List<Score> getScores() {
		return scores;
	}

	public Q nextQ(int disorderId){
		String symptomName = "";
		Symptom hold = new Symptom();

		String [] str = patient.symptoms.split(" ");
		int size = str.length;

		int [] arr = new int [size -1];
		for(int i=1; i<size; i++) {
			arr[i -1] = Integer.parseInt(str[i]);
		}
		System.out.println(Arrays.toString(arr));
		ArrayList<Integer> list = IntStream.of(arr).boxed().collect(Collectors.toCollection(ArrayList::new));
		for(Symptom symptom: symptoms) {
			if(list.contains(disorderId) && symptom.disorderID == disorderId && disorderId != 0 && !patient.words.contains(symptom.symptomName)) {
				symptomName = symptom.symptomName;
				hold = symptom;
				break;
			}
		}

		System.out.println(String.format("===%s=== QuestionforSymptom: %s",hold.disorderID,hold.symptomName));
		int prevInd = list.indexOf(disorderId);
		List<Integer> matchingDisorderIds = getMatchingDisorderIds(symptoms, symptomName);

		return symptomName.contentEquals("")
				? this.nextQ(list.get(prevInd + 1))
				: new Q(PatientsDatabaseAccessObject.getInstance().getQuestions().get(hold.questionID - 1), matchingDisorderIds, symptomName, disorderId);
	}

	private static void printScores(ArrayList<Score> scores) {
// 		for(Score score: scores) {
// //			System.out.println("ID: " + score.dID + " Percent: " + score.matchPercentage + "%" + " Words: " + score.matches.toString());
// 		}
	}

	private static int getMatchPercentage(List<String> matches, List<String> disorderWords) {
		return (int)(((float)matches.size() / (float)disorderWords.size()) * 100);
	}

	private static List<Integer> getMatchingDisorderIds(List<Symptom> symptoms, String symptomName) {
		return symptoms.stream()
				.filter(symptom -> symptom.symptomName.equalsIgnoreCase(symptomName))
				.map(symptom -> symptom.disorderID)
				.collect(Collectors.toList());
	}

	private void bestOf() {
		scores.sort(Collections.reverseOrder());

		patient.symptoms = "";

		for (int x = 0; x < scores.size(); x++) {
			findSymptoms(scores.get(x).dID);
		}

//		System.out.println(patient.symptoms);
	}

	private void findSymptoms(int id) {
//		List<String> chanceSymptomNames = symptoms.stream()
//				.filter(symptom -> symptom.disorderID == id && symptom.symptomName != null)
//				.map(symptom -> symptom.symptomName)
//				.collect(Collectors.toList());
//
//		StringBuilder patientSymptomsBuilder = new StringBuilder(patient.symptoms);
//		HashSet<String> uniqueWords = new HashSet<>(Arrays.asList(patient.words.split(" ")));
//
//		for(String word: uniqueWords) {
//			for(String symptomName : chanceSymptomNames) {
//				if(word.equals(symptomName) && !patient.symptoms.contains(symptomName)) {
//					patientSymptomsBuilder.append(" ").append(word);
//				}
//			}
//		}
		StringBuilder patientSymptomsBuilder = new StringBuilder(patient.symptoms);
		patientSymptomsBuilder.append(" ").append(id);

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

