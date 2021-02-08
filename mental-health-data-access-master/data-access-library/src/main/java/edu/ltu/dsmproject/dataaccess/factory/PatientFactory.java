package edu.ltu.dsmproject.dataaccess.factory;

import edu.ltu.dsmproject.dataaccess.domain.Patient;

import java.util.List;

/**
 * Creates new Patient objects.
 */
public class PatientFactory {
    /**
     * Creates a new Patient using the provided data.
     * @param existingPatients A list of the existing patients.
     * @param patientName The name of the patient to create.
     * @param problemDescription The description of the patient's problem.
     * @param isMale Whether the patient is male (true) or female (false).
     * @return A new Patient object with the next available patient ID.
     */
    public static Patient createPatient(List<Patient> existingPatients, String patientName, String problemDescription, Boolean isMale) {
        int patientId = getNextPatientId(existingPatients);

        Patient patient = new Patient(patientName, problemDescription, isMale, patientId);
        patient.setDiagnosis(0);

        return patient;
    }

    private static int getNextPatientId(List<Patient> existingPatients) {
        return existingPatients.stream()
                .map(patient -> patient.patientID)
                .max(Integer::compareTo)
                .map(highestPatientIdFound -> highestPatientIdFound + 1)
                .orElse(1);
    }
}
