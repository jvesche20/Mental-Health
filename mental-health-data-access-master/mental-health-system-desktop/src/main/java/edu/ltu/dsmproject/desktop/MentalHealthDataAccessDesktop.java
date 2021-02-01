package edu.ltu.dsmproject.desktop;

import javax.swing.*;

/*Mental Health System
 * this system is meant to demonstrate a computers ability to suggest possible mental illnesses
 * this system is not meant to determine a definite illness for a patient*/
class MentalHealthDataAccessDesktop {
	public static void main(String[] Args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (UnsupportedLookAndFeelException | IllegalAccessException | InstantiationException | ClassNotFoundException e) {
			logUnableToSetLookAndFeel(e);
		}

		LoginGUI loginGUI = new LoginGUI();
		loginGUI.setupPanel();
	}

	private static void logUnableToSetLookAndFeel(Exception e) {
		System.out.println("Could not set system look and feel: " + e.getMessage());
	}
}