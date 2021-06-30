package it.polito.tdp.crimes.model;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	
	private EventsDao dao;
	private SimpleWeightedGraph<String, DefaultWeightedEdge> grafo;
	private List<String> percorsoMigliore;
	
	public Model() {
		dao = new EventsDao();
	}
	
	public List<String> getCategorie(){
		return dao.getCategorie();
	}
	
	public void creaGrafo(String categoria, int mese) {
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		//aggiunta dei vertici 
		Graphs.addAllVertices(this.grafo, dao.getVertici(categoria, mese));
		
		//aggiunta degli archi
		for(Adiacenza a : dao.getAdiacenze(categoria, mese)) {
			if(this.grafo.getEdge(a.getV1(), a.getV2())==null) {
				
				Graphs.addEdge(this.grafo, a.getV1(), a.getV2(), a.getPeso());
			
			}
		}
		System.out.println("vertici: "+grafo.vertexSet().size());
		System.out.println("archi: "+grafo.edgeSet().size());
		for(String s : grafo.vertexSet()) {
			System.out.println(s);
		}
		
		
	}
	
	public List<Adiacenza> getArchi(){
		List<Adiacenza> adiacenza = new ArrayList<Adiacenza>();
		double pesomedio=0.0;
		for(DefaultWeightedEdge e : this.grafo.edgeSet()) {
			pesomedio+=this.grafo.getEdgeWeight(e);
		}
		
		pesomedio = pesomedio/this.grafo.edgeSet().size();
		
		for(DefaultWeightedEdge e1 : this.grafo.edgeSet()) {
			if(this.grafo.getEdgeWeight(e1)>pesomedio) {
				adiacenza.add(new Adiacenza(this.grafo.getEdgeSource(e1),this.grafo.getEdgeTarget(e1),this.grafo.getEdgeWeight(e1)));
			}
		}
		return adiacenza;
		
	}
	
	public List<String> trovaPercorso(String sorgente, String destinazione){
		this.percorsoMigliore = new ArrayList<>();
		List<String> parziale = new ArrayList<String>();
		
		parziale.add(sorgente);
		
		cerca(destinazione, parziale);
		
		
		return this.percorsoMigliore;
	}
	
	private void cerca(String destinazione, List<String> parziale) {
		//caso terminale
		if(parziale.get(parziale.size()-1).equals(destinazione)) {
			if(parziale.size() > this.percorsoMigliore.size() ) {
				this.percorsoMigliore = new ArrayList<>(parziale);
			}
			return;
		}
		
		//caso normale scorro i vicini dell'ultimo inserito
		for(String vicino : Graphs.neighborListOf(grafo, parziale.get(parziale.size()-1))) {
			if(!parziale.contains(vicino)) {
				parziale.add(vicino);
				cerca(destinazione,parziale);
				parziale.remove(parziale.size()-1);
			}
		}
		
		
		
	}
	
	
	
	
	
	
	
	
	
	
}
