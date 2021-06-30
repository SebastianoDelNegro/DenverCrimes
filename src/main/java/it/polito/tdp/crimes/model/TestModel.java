package it.polito.tdp.crimes.model;

import java.util.List;

public class TestModel {

	public static void main(String[] args) {
		
		Model m = new Model();
		
		m.creaGrafo("aggravated-assault", 4);
		
		List<String> percorso = m.trovaPercorso("aggravated-assault", "weapon-fire-into-occ-bldg" );
		for(String s : percorso) {
			System.out.println("\n"+s);
		}
	}

}
