package edu.ltu.dsmproject.desktop.tablemodels;

import edu.ltu.dsmproject.dataaccess.domain.Patient;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

/**
 * Provides a TableModel for the patient list in the doctor's view.
 */
public class PatientTableModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String[] columnNames = {
		"Patient Name", "Date Diagnosed", "Diagnosis", "Gender", "Patient ID" //add edit column CR
	};
	
	private List<Patient> patients;
	
	public PatientTableModel(List<Patient> patients) {
		this.patients = patients;
	}
	
	public PatientTableModel() {
		this.patients = new ArrayList<>();
	}
	
	@Override
	public int getRowCount() {
		return this.patients.size();
	}

	@Override
	public int getColumnCount() {
		return this.columnNames.length;
	}
	
	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Patient pat = this.patients.get(rowIndex);

		switch (columnIndex) {
			case 0: return pat.patientName;
			case 1: return pat.diagnosisDate;
			case 2: return pat.diagnosedID;
			case 3: return pat.gender;
			case 4: return pat.patientID;
			default: return null;
		}
	}
}
