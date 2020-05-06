package it.polito.tdp.extflightdelays.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.extflightdelays.model.Adiacenza;
import it.polito.tdp.extflightdelays.model.Airline;
import it.polito.tdp.extflightdelays.model.Airport;
import it.polito.tdp.extflightdelays.model.Flight;


public class ExtFlightDelaysDAO {

	public List<Airline> loadAllAirlines() {
		String sql = "SELECT * from airlines";
		List<Airline> result = new ArrayList<Airline>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				result.add(new Airline(rs.getInt("ID"), rs.getString("IATA_CODE"), rs.getString("AIRLINE")));
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public void loadAllAirports(Map <Integer, Airport> idMap) {
		String sql = "SELECT * FROM airports";

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				if (!idMap.containsKey(rs.getInt("ID"))) {
				Airport airport = new Airport(rs.getInt("ID"), rs.getString("IATA_CODE"), rs.getString("AIRPORT"),
						rs.getString("CITY"), rs.getString("STATE"), rs.getString("COUNTRY"), rs.getDouble("LATITUDE"),
						rs.getDouble("LONGITUDE"), rs.getDouble("TIMEZONE_OFFSET"));
				idMap.put(airport.getId(), airport);
				}
			}
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<Flight> loadAllFlights() {
		String sql = "SELECT * FROM flights";
		List<Flight> result = new LinkedList<Flight>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Flight flight = new Flight(rs.getInt("ID"), rs.getInt("AIRLINE_ID"), rs.getInt("FLIGHT_NUMBER"),
						rs.getString("TAIL_NUMBER"), rs.getInt("ORIGIN_AIRPORT_ID"),
						rs.getInt("DESTINATION_AIRPORT_ID"),
						rs.getTimestamp("SCHEDULED_DEPARTURE_DATE").toLocalDateTime(), rs.getDouble("DEPARTURE_DELAY"),
						rs.getDouble("ELAPSED_TIME"), rs.getInt("DISTANCE"),
						rs.getTimestamp("ARRIVAL_DATE").toLocalDateTime(), rs.getDouble("ARRIVAL_DELAY"));
				result.add(flight);
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	
	
	/*public double calcolaMedia (int aeroportoP, int aeroportoA) {
		String sql= "select avg(distance) as media " + 
				"from flights as f " + 
				"where (f.ORIGIN_AIRPORT_ID=? and f.DESTINATION_AIRPORT_ID=?) or (f.ORIGIN_AIRPORT_ID=? and f.DESTINATION_AIRPORT_ID=?)";
		
		double media=0.0;
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, aeroportoP);
			st.setInt(2, aeroportoA);
			st.setInt(3, aeroportoA);
			st.setInt(4, aeroportoP);

			ResultSet res = st.executeQuery();
			
			if(res.next()) {
				media= res.getDouble("media");
			}
			
			st.close();
			conn.close();
			
			return media;
	
		}
		catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore di connessione al Database.");			
		}
	
	}
	
	/*public List<Adiacenza> getAdiacenza (Map <Integer, Airport> idMap, double x){
		String sql = "select distinct ORIGIN_AIRPORT_ID, DESTINATION_AIRPORT_ID " + 
				"from flights";
		List <Adiacenza> result = new ArrayList <>();
		boolean flag=false;
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			
			while (res.next()) {
				if (calcolaMedia(res.getInt("ORIGIN_AIRPORT_ID"),res.getInt("DESTINATION_AIRPORT_ID")) >=x ) {
					Adiacenza a = new Adiacenza (idMap.get(res.getInt("ORIGIN_AIRPORT_ID")),
							idMap.get(res.getInt("DESTINATION_AIRPORT_ID")),
							calcolaMedia(res.getInt("ORIGIN_AIRPORT_ID"),res.getInt("DESTINATION_AIRPORT_ID")) );
				
					if (result.isEmpty()) {
						result.add(a);
					} else {
						for (Adiacenza ad : result) {
							if ((ad.getPartenza().getId()!=a.getPartenza().getId()) && (ad.getPartenza().getId()!=a.getArrivo().getId())) {
								flag=true;
							}
						}
					}
					if (flag==true) {
						result.add(a);
					}
				
				}
			}
			st.close();
			conn.close();
			
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore di connessione al Database.");			
		}
	}*/
	
	public List<Adiacenza> getAdiacenza (Map <Integer, Airport> idMap){
		String sql = "select distinct ORIGIN_AIRPORT_ID, DESTINATION_AIRPORT_ID, "+ 
				"SUM(distance) as somma, count(*) as viaggi " + 
				"	from flights as f " +  
				"	group by ORIGIN_AIRPORT_ID, DESTINATION_AIRPORT_ID";
		List <Adiacenza> result = new ArrayList <>();
		boolean flag=false;
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			
			ResultSet res = st.executeQuery();
			
			while (res.next()) {
				Adiacenza a = new Adiacenza (idMap.get(res.getInt("ORIGIN_AIRPORT_ID")),
						idMap.get(res.getInt("DESTINATION_AIRPORT_ID")), res.getDouble("somma"), res.getInt("viaggi") );
				
					if (result.isEmpty()) {
						a.setPeso(res.getDouble("somma")/res.getInt("viaggi"));
						result.add(a);
					} else {
						for (Adiacenza ad : result) {
							if ((ad.getPartenza().getId()!=a.getArrivo().getId()) && (ad.getArrivo().getId()!=a.getPartenza().getId())) {
								flag=true;
						}
					}
					if (flag==true) {
						a.setPeso(res.getDouble("somma")/res.getInt("viaggi"));
						result.add(a);
					} else {
						result.get(result.indexOf(a)).setPeso((result.get(result.indexOf(a)).getSomma()+a.getSomma())/(result.get(result.indexOf(a)).getCount()+a.getCount()));
						
					}
					}
				
			}
			st.close();
			conn.close();
			
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore di connessione al Database.");			
		}
	}
	
	
	
}
