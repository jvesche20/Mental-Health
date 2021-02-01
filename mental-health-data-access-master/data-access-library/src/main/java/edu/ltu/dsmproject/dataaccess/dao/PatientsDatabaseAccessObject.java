package edu.ltu.dsmproject.dataaccess.dao;

import edu.ltu.dsmproject.dataaccess.domain.*;
import edu.ltu.dsmproject.dataaccess.factory.PatientFactory;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * Provides the patients portion of the data access layer for the mental health application as a singleton.
 */
public class PatientsDatabaseAccessObject extends MSAccessDatabaseAccessObject {
	private static final String MS_ACCESS_DB_FILENAME = "MentalIllnessDB.accdb";

	private final List<Patient> patients = new ArrayList<>();
	private final List<Keyword> keywords = new ArrayList<>();
	private final List<Synonym> thesaurus = new ArrayList<>();
	private final List<Question> questions = new ArrayList<>();
	private final List<Disorder> disorders = new ArrayList<>();
	private final List<Symptom> symptoms = new ArrayList<>();

	private static PatientsDatabaseAccessObject instance = null;

	/**
	 * Creates a new PatientsDatabaseAccessObject instance and opens the connection to the database.
	 * @throws DatabaseReadException The database read operation failed.
	 */
	private PatientsDatabaseAccessObject() {
		super(MS_ACCESS_DB_FILENAME);
		fillKeywords();
		fillPatients();
		//fillQuestions();
		fillDisorders();
	}

	/**
	 * Gets the singleton instance.
	 * @return The singleton instance.
	 */
	public static PatientsDatabaseAccessObject getInstance() {
		if (instance == null) {
			instance = new PatientsDatabaseAccessObject();
		}
		return instance;
	}

	/**
	 * Adds the provided Patient to the Patient table.
	 * @param patient The Patient to add.
	 * @throws IllegalArgumentException The patient to add is null.
	 * @throws DatabaseWriteException The database write operation failed.
	 */
	public void addPatient(Patient patient) {
		if (patient == null) {
			throw new IllegalArgumentException("The patient to add cannot be null.");
		}

		StringBuilder problemDescription = new StringBuilder(patient.problemDescription.length());
		for (char c : patient.problemDescription.toCharArray()) {
			problemDescription.append(c == '\'' ? "''" : c);
		}

		final String sql = "INSERT INTO Patient (PatientID, DiagnosedID, ProblemDescription, DiagnosisDate, PatientName, Gender) "
				+ "VALUES (?, ?, ?, ?, ?, ?)";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, patient.patientID);
			preparedStatement.setInt(2, patient.diagnosedID);
			preparedStatement.setString(3, problemDescription.toString());
			preparedStatement.setString(4, patient.diagnosisDate);
			preparedStatement.setString(5, patient.patientName);
			preparedStatement.setString(6, patient.gender);

			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			throw new DatabaseWriteException("Could not add patient.", e);
		}

		this.patients.add(patient);
	}

	/**
	 * Adds a new synonym to the Thesaurus table.
	 * @param synonym The new synonym to add.
	 * @throws DatabaseWriteException The database write operation failed.
	 */
	public void addSynonym(Synonym synonym) {
		final String sql = "INSERT INTO Thesaurus (InputWord, Keyword) Values (?, ?)";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, synonym.inputWord);
			preparedStatement.setString(2, synonym.keyword);

			preparedStatement.executeUpdate();
		}
		catch (SQLException e) {
			throw new DatabaseWriteException("Could not add synonym.", e);
		}
	}

	/**
	 * Adds a new word to the Keywords table.
	 * @param word The new word to add.
	 * @throws DatabaseWriteException The database write operation failed.
	 */
	public void addWord(Keyword word) {
		final String sql = "INSERT INTO Keywords (Word, WordType, MajorDepressiveDisorder, AttentionDeficitHyperactivityDisorder, PosttraumaticStressDisorder, ModValue)"
				+ "VALUES (?, ?, ?, ?, ?, ?)";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, word.word);
			preparedStatement.setString(2, word.type);
			preparedStatement.setInt(3, word.majorDepressiveDisorder);
			preparedStatement.setInt(4, word.attentionDeficitHyperactivityDisorder);
			preparedStatement.setInt(5, word.postTraumaticStressDisorder);
			preparedStatement.setInt(6, word.modValue);

			preparedStatement.executeUpdate();
		}
		catch(SQLException e) {
			throw new DatabaseWriteException("Could not add word.", e);
		}
	}

	/**
	 * Updates the Patient in the database with the current results.
	 * @param patient The Patient to update.
	 * @throws IllegalArgumentException The patient to add is null.
	 * @throws DatabaseWriteException The database write operation failed.
	 */
	public void addPatientResults(Patient patient) {
		if (patient == null) {
			throw new IllegalArgumentException("The patient results to add cannot be null.");
		}

		final String sql = "UPDATE Patient SET DiagnosedID = ?, Symptoms = ?, Words = ? WHERE PatientID = ?";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, patient.diagnosedID);
			preparedStatement.setString(2, patient.symptoms);
			preparedStatement.setString(3, patient.words);
			preparedStatement.setInt(4, patient.patientID);

			preparedStatement.executeUpdate();
		}
		catch(SQLException e) {
			throw new DatabaseWriteException("Could not add patient results.", e);
		}
	}

	/**
	 * Adds words to the database for the provided Patient.
	 * @param patient The Patient to update the words for.
	 * @throws IllegalArgumentException Patient is null.
	 * @throws DatabaseWriteException The database write operation failed.
	 */
	public void addPatientWords(Patient patient) {
		if (patient == null) {
			throw new IllegalArgumentException("The patient to add words for cannot be null.");
		}

		final String sql = "UPDATE Patient SET Words = ? WHERE PatientID = ?";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, patient.words);
			preparedStatement.setInt(2, patient.patientID);

			preparedStatement.executeUpdate();
		}
		catch(SQLException e) {
			throw new DatabaseWriteException("Could not add patient words.", e);
		}
	}

	/**
	 * Creates and adds a new Patient to the database, then returns it.
	 * @param patientName The new patient's name.
	 * @param problemDescription The problem description for the new patient.
	 * @param isMale Whether the patient is male (true) or female (false).
	 * @return The new Patient object.
	 * @throws DatabaseWriteException The database write operation failed.
	 */
	public Patient createAndAddPatient(String patientName, String problemDescription, Boolean isMale) {
		List<Patient> existingPatients = this.getPatients();
		Patient patient = PatientFactory.createPatient(existingPatients, patientName, problemDescription, isMale);
		this.addPatient(patient);

		return patient;
	}

	/**
	 * Gets the list of disorders stored in the database.
	 * @return The list of disorders stored in the database.
	 * @throws DatabaseReadException The database read operation failed.
	 */
	public List<Disorder> getDisorders() {
		if (this.disorders.isEmpty()) {
			this.fillDisorders();
		}
		return this.disorders;
	}

	/**
	 * Gets the list of keywords stored in the database.
	 * @return The list of keywords stored in the database.
	 * @throws DatabaseReadException The database read operation failed.
	 */
	public List<Keyword> getKeywords() {
		if (this.keywords.isEmpty()) {
			this.fillKeywords();
		}
		return this.keywords;
	}

	/**
	 * Gets the list of patients stored in the database.
	 * @return The list of patients stored in the database.
	 * @throws DatabaseReadException The database read operation failed.
	 */
	public List<Patient> getPatients() {
		if (this.patients.isEmpty()) {
			this.fillPatients();
		}
		return this.patients;
	}

	/**
	 * Gets the list of symptoms stored in the database.
	 * @return The list of symptoms stored in the database.
	 * @throws DatabaseReadException The database read operation failed.
	 */
	public List<Symptom> getSymptoms() {
		if (this.symptoms.isEmpty()) {
			this.fillSymptoms();
		}
		return this.symptoms;
	}

	/**
	 * Gets the list of synonyms stored in the database.
	 * @return The list of synonyms stored in the database.
	 * @throws DatabaseReadException The database read operation failed.
	 */
	public List<Synonym> getThesaurus() {
		if (this.thesaurus.isEmpty()) {
			this.fillThesaurus();
		}
		return this.thesaurus;
	}

	/**
	 * Gets the list of questions stored in the database.
	 * @return The list of questions stored in the database.
	 * @throws DatabaseReadException The database read operation failed.
	 */
	public List<Question> getQuestions() {
		if (this.questions.isEmpty()) {
			this.fillQuestions();
		}
		return this.questions;
	}
	
	public void showWords() throws FileNotFoundException {
		final String wordsFilepath = "db/words.txt";

		try {
			Statement s = connection.createStatement();
			String selTable = "SELECT WordMatch From Disorder";
			s.execute(selTable);
			ResultSet rs = s.getResultSet();

			HashSet<String> retrievedWords = new HashSet<>();
			while((rs != null) && (rs.next()))
			{
				String[] words = rs.getString("WordMatch").split("\\s+");
				Collections.addAll(retrievedWords, words);
			}

			PrintWriter printWriter = new PrintWriter(wordsFilepath);
			for(String word: retrievedWords) {
				printWriter.println(word);
			}
		} catch (SQLException e) {
			throw new DatabaseReadException("Could not read word matches.", e);
		}
	}

	private void fillDisorders() {
		try {
			Statement s = connection.createStatement();
			final String selTable = "SELECT DisorderID, DisorderName, ParentDisorderID, DisorderFeatures, WordMatch FROM Disorder";
			s.execute(selTable);
			ResultSet rs = s.getResultSet();

			while ((rs != null) && (rs.next())) {
				Disorder disorder = new Disorder(
						rs.getInt("DisorderID"),
						rs.getString("DisorderName"),
						rs.getInt("ParentDisorderID"),
						rs.getString("DisorderFeatures"),
						rs.getString("WordMatch")
				);
				this.disorders.add(disorder);
			}
		} catch (SQLException e) {
			throw new DatabaseReadException("Could not read disorders.", e);
		}
	}

	private void fillKeywords() {
		try {
			Statement s = connection.createStatement();
			String selTable = "SELECT Word, WordType, MajorDepressiveDisorder, PostTraumaticStressDisorder, AttentionDeficitHyperActivityDisorder, ModValue FROM Keywords";
			s.execute(selTable);
			ResultSet rs = s.getResultSet();

			while ((rs != null) && (rs.next())) {
				Keyword keyword = new Keyword(
						rs.getString("Word").toLowerCase(),
						rs.getString("WordType"),
						rs.getInt("MajorDepressiveDisorder"),
						rs.getInt("PostTraumaticStressDisorder"),
						rs.getInt("AttentionDeficitHyperactivityDisorder"),
						rs.getInt("ModValue")
				);
				this.keywords.add(keyword);
			}
		} catch (SQLException e) {
			throw new DatabaseReadException("Could not read keywords.", e);
		}
	}

	private void fillPatients() {
		try {
			Statement s = connection.createStatement();
			final String selTable = "SELECT PatientID, DiagnosedID, ProblemDescription, DiagnosisDate, PatientName, Gender, Words FROM Patient";
			s.execute(selTable);
			ResultSet rs = s.getResultSet();

			while ((rs != null) && (rs.next())) {
				Patient patient = new Patient(
						rs.getInt("PatientID"),
						rs.getInt("DiagnosedID"),
						rs.getString("ProblemDescription"),
						rs.getString("DiagnosisDate"),
						rs.getString("PatientName"),
						rs.getString("Gender"),
						rs.getString("Words")
				);
				this.patients.add(patient);
			}
		} catch (SQLException e) {
			throw new DatabaseReadException("Could not read patients.", e);
		}
	}

	private void fillQuestions() {
		try {
			Statement s = connection.createStatement();
			String selTable = "SELECT QuestionID, Question From Questions";
			s.execute(selTable);
			ResultSet rs = s.getResultSet();

			while ((rs != null) && (rs.next())) {
				Question question = new Question(
						rs.getInt("QuestionID"),
						rs.getString("Question")
				);
				this.questions.add(question);
			}
		} catch (SQLException e) {
			throw new DatabaseReadException("Could not read questions.", e);
		}
	}

	private void fillSymptoms() {
		try {
			Statement s = connection.createStatement();
			String selTable = "SELECT SymptomID, DisorderID, SymptomName, QuestionID FROM Symptom";
			s.execute(selTable);
			ResultSet rs = s.getResultSet();

			while ((rs != null) && (rs.next())) {
				Symptom symptom = new Symptom(
						rs.getInt("SymptomID"),
						rs.getInt("DisorderID"),
						rs.getString("SymptomName"),
						rs.getInt("QuestionID")
				);
				this.symptoms.add(symptom);
			}
		} catch (SQLException e) {
			throw new DatabaseReadException("Could not read symptoms.", e);
		}
	}

	private void fillThesaurus() {
		try {
			Statement s = connection.createStatement();
			String selTable = "SELECT InputWord, Keyword FROM Thesaurus";
			s.execute(selTable);
			ResultSet rs = s.getResultSet();

			while ((rs != null) && (rs.next())) {
				Synonym synonym = new Synonym(
						rs.getString("InputWord"),
						rs.getString("Keyword")
				);
				this.thesaurus.add(synonym);
			}
		} catch (SQLException e) {
			throw new DatabaseReadException("Could not read thesaurus.", e);
		}
	}
}
