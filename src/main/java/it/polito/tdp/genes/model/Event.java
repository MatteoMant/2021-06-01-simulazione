package it.polito.tdp.genes.model;

public class Event implements Comparable<Event>{
	
	private int tempo;
	private int nIngegnere;
	
	public Event(int tempo, int numIngegnere) {
		super();
		this.tempo = tempo;
		this.nIngegnere = numIngegnere;
	}

	public int getTempo() {
		return tempo;
	}

	public int getNumIngegnere() {
		return nIngegnere;
	}

	@Override
	public int compareTo(Event other) {
		return this.getTempo() - other.getTempo();
	}
	
}
