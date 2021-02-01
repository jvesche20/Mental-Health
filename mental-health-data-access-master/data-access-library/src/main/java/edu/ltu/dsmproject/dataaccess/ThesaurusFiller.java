package edu.ltu.dsmproject.dataaccess;

import edu.ltu.dsmproject.dataaccess.dao.PatientsDatabaseAccessObject;
import edu.ltu.dsmproject.dataaccess.domain.Synonym;

import java.util.Scanner;

public class ThesaurusFiller {
	public ThesaurusFiller() {
		String base;
		String word;
		Scanner scan = new Scanner(System.in);
		
		while(true) {
			System.out.println("Enter base word");
			base = scan.nextLine();
			System.out.println("Enter other meanings");
			while (scan.hasNextLine()) {
				word = scan.nextLine();
				PatientsDatabaseAccessObject.getInstance().addSynonym(new Synonym(word, base));
			}
		}
	}
}
