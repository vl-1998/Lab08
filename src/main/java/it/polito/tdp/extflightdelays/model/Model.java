package it.polito.tdp.extflightdelays.model;

import java.util.HashMap;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.extflightdelays.db.ExtFlightDelaysDAO;

public class Model {
	private Graph <Airport, DefaultWeightedEdge> grafo;
	private Map <Integer, Airport> idMap;
	
	public Model () {
		idMap = new HashMap <>();
	}
	
	public void creaGrafo (double X) {
		this.grafo = new SimpleWeightedGraph <>(DefaultWeightedEdge.class);
		ExtFlightDelaysDAO dao = new ExtFlightDelaysDAO ();
		
		dao.loadAllAirports(idMap);
		//Aggiungo i vertici
		Graphs.addAllVertices(this.grafo, idMap.values());
		
		//Aggiungo gli archi
			for (Adiacenza a : dao.getAdiacenza(idMap, X)) {
			
				Graphs.addEdge(this.grafo, a.getPartenza(),  a.getArrivo(), a.getPeso());
			
			}
		}
		
	
	
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public String archi() {
		String result="";
		for (DefaultWeightedEdge e : grafo.edgeSet()) {
			if (result=="") {
				result= "("+ grafo.getEdgeSource(e).getAirportName() + "," + grafo.getEdgeTarget(e).getAirportName() + ")" + " peso: " + grafo.getEdgeWeight(e);
			} else {
				result = result +"\n" + "("+ grafo.getEdgeSource(e).getAirportName() + "," + grafo.getEdgeTarget(e).getAirportName() + ")" + " peso: " + grafo.getEdgeWeight(e) ;
			}
		}
		
		return result;
	}

}
