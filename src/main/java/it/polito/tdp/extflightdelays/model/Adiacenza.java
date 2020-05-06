package it.polito.tdp.extflightdelays.model;

public class Adiacenza {
	public Airport partenza;
	public Airport arrivo;
	public double peso;
	public double somma;
	public int count;
	
	
	/**
	 * @param partenza
	 * @param arrivo
	 * @param peso
	 * @param somma
	 * @param count
	 */
	public Adiacenza(Airport partenza, Airport arrivo, double somma, int count) {
		super();
		this.partenza = partenza;
		this.arrivo = arrivo;
		this.somma = somma;
		this.count = count;
		peso=somma/count;
	}

	

	public Airport getPartenza() {
		return partenza;
	}



	public void setPartenza(Airport partenza) {
		this.partenza = partenza;
	}



	public Airport getArrivo() {
		return arrivo;
	}



	public void setArrivo(Airport arrivo) {
		this.arrivo = arrivo;
	}



	public double getPeso() {
		return peso;
	}



	public void setPeso(double peso) {
		this.peso = peso;
	}



	public double getSomma() {
		return somma;
	}



	public void setSomma(double somma) {
		this.somma = somma;
	}



	public int getCount() {
		return count;
	}



	public void setCount(int count) {
		this.count = count;
	}



	@Override
	public String toString() {
		return "Adiacenza partenza=" + partenza.getAirportName() + ", arrivo=" + arrivo.getAirportName();
	}
	
	
	
}
