package it.polito.tdp.extflightdelays.model;

public class Adiacenza {
	public Airport partenza;
	public Airport arrivo;
	public double peso;
	
	/**
	 * @param partenza
	 * @param arrivo
	 * @param peso
	 */
	public Adiacenza(Airport partenza, Airport arrivo, double peso) {
		super();
		this.partenza = partenza;
		this.arrivo = arrivo;
		this.peso = peso;
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
	@Override
	public String toString() {
		return "Adiacenza partenza=" + partenza.getAirportName() + ", arrivo=" + arrivo.getAirportName();
	}
	
	
	
}
