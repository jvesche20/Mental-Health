package edu.ltu.dsmproject.dataaccess.factory;

import edu.ltu.dsmproject.dataaccess.domain.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PatientFactoryTest {
    PatientFactory subject;

    @BeforeEach
    public void setUp() {
        subject = new PatientFactory();
    }

    @Test
    void createPatient_shouldCreatePatientWithExpectedData() {
        Patient existingPatient1 = new Patient(2, 1, "I have a problem", "03/01/2020", "John Doe", true);
        Patient existingPatient2 = new Patient(1, 1, "I have a problem, too", "03/01/2020", "Jane Doe", false);
        List<Patient> existingPatients = new ArrayList<>();
        existingPatients.add(existingPatient1);
        existingPatients.add(existingPatient2);

        Patient actual = PatientFactory.createPatient(existingPatients, "My name", "Some problem goes here", true);

        assertNotNull(actual);
        assertEquals(3, actual.patientID);
        assertEquals("My name", actual.patientName);
        assertEquals("Some problem goes here", actual.problemDescription);
        assertEquals("Male", actual.gender);
        assertEquals(0, actual.diagnosedID);
        assertEquals("", actual.symptoms);
        assertNull(actual.words);
    }

    @Test
    void createPatient_shouldCreateFirstPatientWithExpectedData() {
        List<Patient> existingPatients = new ArrayList<>();

        Patient actual = PatientFactory.createPatient(existingPatients, "My name", "Some problem goes here", true);

        assertNotNull(actual);
        assertEquals(1, actual.patientID);
        assertEquals("My name", actual.patientName);
        assertEquals("Some problem goes here", actual.problemDescription);
        assertEquals("Male", actual.gender);
        assertEquals(0, actual.diagnosedID);
        assertEquals("", actual.symptoms);
        assertNull(actual.words);
    }
}