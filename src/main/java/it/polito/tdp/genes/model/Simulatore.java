package it.polito.tdp.genes.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;

public class Simulatore {

	// Parametri della simulazione 
	private int numIng;
	private int mesiMax = 36; // durata del progetto e quindi della simulazione
	
	// Stato del mondo
	private Graph<Genes, DefaultWeightedEdge> grafo;
	
	// Coda degli eventi
	private PriorityQueue<Event> queue;
	
	// Output della simulazione
	// private Map<Genes, Integer> geniStudiati; // gene --> numero di ingegneri che hanno studiato quel gene
	private List<Genes> geneStudiato; // dato un ingegnere i-esimo (0, ..., n-1) dimmi su quale gene sta lavorando
	
	public Simulatore(Graph<Genes, DefaultWeightedEdge> grafo) {
		this.grafo = grafo;
	}
	
	public void init(int n, Genes partenza) {
		this.numIng = n;
		
		if (Graphs.neighborListOf(this.grafo, partenza) == null) { // this.grafo.degreeOf(this.partenza) == 0
			// in questo caso il vertice di partenza è isolato e quindi non possiamo lanciare la simulazione
			throw new IllegalArgumentException("Vertice di partenza isolato!");
		}
		
		this.queue = new PriorityQueue<>();
	
		// Adesso dobbiamo pre-popolare(inizializzare) la coda degli eventi	
		for (int i = 0; i < this.numIng; i++) {
			this.queue.add(new Event(0, i)); // creiamo un numero di eventi pari al numero di ingegneri
		}

		// Creo un ArrayList con 'numIng' valori tutti pari al gene di partenza perchè 
		// all'inizio tutti gli ingegneri studiano il gene di partenza, selezionato dall'utente
		this.geneStudiato = new ArrayList<>();
		for (int i = 0; i < this.numIng; i++) {
			this.geneStudiato.add(partenza); // in questa lista vado a memorizzare l'ultimo gene studiato da ogni ingegnere
		} 
		
	}
	
	public void run() {

		while (!this.queue.isEmpty()) {
			Event e = this.queue.poll();
			
			int tempo = e.getTempo(); // il tempo in questo caso rappresenta il numero di mesi
			int nIng = e.getNumIngegnere(); 
			Genes ultimoGeneStudiato = geneStudiato.get(nIng); // prendo l'ultimo gene studiato dall'ingegnere in questione
	
			if (tempo < this.mesiMax) { // non devo eccedere il numero massimo di mesi
				
				if (Math.random() < 0.3) {
					// in questo caso l'ingegnere continuerà a studiare il gene corrente
					this.queue.add(new Event(tempo+1, nIng)); // NON modifico l'ultimo gene studiato da tale ingegnere che dunque rimane lo stesso
				} else {
					// in questo caso l'ingegnere cambia il gene da studiare e passa ad uno dei geni ad esso adiacenti
					
					// Dobbiamo però capire quale gene adesso andrà a studiare tra quelli adiacenti...
					Genes daStudiare = null;
						
					double somma = 0.0;
					
					for (DefaultWeightedEdge edge : this.grafo.edgesOf(ultimoGeneStudiato)) {
						somma += this.grafo.getEdgeWeight(edge);
					}
			
					// Dobbiamo estrarre un numero casuale R tra 0 e somma
					double R = Math.random() * somma;
					
					// Adesso dobbiamo confrontare R con le somme parziali dei pesi
					double sommaParz = 0.0;
					for (DefaultWeightedEdge edge : this.grafo.edgesOf(ultimoGeneStudiato)) {
						sommaParz += this.grafo.getEdgeWeight(edge);
						if(sommaParz > R) {
							daStudiare = Graphs.getOppositeVertex(this.grafo, edge, ultimoGeneStudiato);
							break;
						}
					}
					
					geneStudiato.set(nIng, daStudiare);
					this.queue.add(new Event(tempo+1, nIng));
				}
			}
			
		}
	}
	
	public Map<Genes, Integer> getGeniStudiati(){
		Map<Genes, Integer> result = new HashMap<>();
		
		for (int nIng = 0; nIng < this.numIng; nIng++) {
			Genes g = this.geneStudiato.get(nIng); // ultimo gene studiato dall'ing. i-esimo
			if (result.containsKey(g)) {
				result.put(g, result.get(g)+1);
			} else {
				result.put(g, 1);
			}
		}
		
		// SOLUZIONE ALTERNATIVA:
//		for (int i = 0; i < geneStudiato.size(); i++) {
//			int count = 0;
//			for (int j = 0; j < geneStudiato.size(); j++) {
//				if (geneStudiato.get(i).equals(geneStudiato.get(j))) {
//					count++;
//				}
//			}
//			if (!result.containsKey(geneStudiato.get(i))){
//				result.put(geneStudiato.get(i), count);
//			}
//		}
		
		return result;
	}
	
}
