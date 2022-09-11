package it.polito.tdp.genes.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.genes.db.GenesDao;

public class Model {
	
	GenesDao dao;
	private Graph<Genes, DefaultWeightedEdge> grafo;
	private Map<String, Genes> idMap;
	
	public Model() {
		dao = new GenesDao();
		idMap = new HashMap<>();
		this.dao.getAllGenes(idMap);
	}
	
	public void creaGrafo() {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		// Aggiunta dei vertici
		Graphs.addAllVertices(this.grafo, this.dao.getAllEssentialGenes());
	
		// Aggiunt degli archi
		for (Adiacenza a : this.dao.getAllAdiacenze(idMap)) {
			Graphs.addEdge(this.grafo, a.getG1(), a.getG2(), a.getPeso());
		}
		 
	}
	
	public List<Adiacenza> getVerticiAdiacenti(Genes gene){
		List<Adiacenza> adiacenti = new LinkedList<>();
		for (Genes vicino : Graphs.neighborListOf(this.grafo, gene)) {
			double peso = this.grafo.getEdgeWeight(this.grafo.getEdge(gene, vicino));
			adiacenti.add(new Adiacenza(gene, vicino, peso));
		}
		return adiacenti;
	}
	
	public Map<Genes, Integer> simula(Genes start, int n) {
		try {
			Simulatore sim = new Simulatore(grafo);
			sim.init(n, start);
			sim.run();
			return sim.getGeniStudiati();
		} catch (IllegalArgumentException ex) {
			return null;
		}
	}
	
	public Set<Genes> getAllVertici(){
		return this.grafo.vertexSet();
	}
	
	public int getNumVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int getNumArchi() {
		return this.grafo.edgeSet().size();
	}
}
