package it.polito.tdp.meteo.model;

public class Citta {
	
	
	private String nome;
	private Rilevamento rilevamento;
	private int counter = 0;
	
	
	
	public Citta(String nome) {
		this.nome = nome;
	}
	
	public Citta(String nome, Rilevamento rilevamento, int counter) {
		this.nome = nome;
		this.rilevamento = rilevamento;
		this.counter=counter;
	}
	
	public Citta(String nome, Rilevamento rilevamento) {
		this.nome = nome;
		this.rilevamento = rilevamento;
	}
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Rilevamento getRilevamento() {
		return rilevamento;
	}

	public void setRilevamento(Rilevamento rilevamento) {
		this.rilevamento = rilevamento;
	}

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}
	
	public void increaseCounter() {
		this.counter += 1;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Citta other = (Citta) obj;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return nome;
	}
	

}
