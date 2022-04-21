package it.polito.tdp.meteo.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import it.polito.tdp.meteo.model.Rilevamento;

public class MeteoDAO {
	
	public List<Rilevamento> getAllRilevamenti() {
		final String sql = "SELECT Localita, Data, Umidita FROM situazione ORDER BY data ASC";
		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Rilevamento r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data"), rs.getInt("Umidita"));
				rilevamenti.add(r);
			}
			conn.close();
			return rilevamenti;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public List<Rilevamento> getAllRilevamentiLocalitaMese(Integer mese, String localita) {
		final String sql = "SELECT Localita, Data, Umidita FROM situazione WHERE MONTH(Data)=? AND Localita=? ";
		List<Rilevamento> listaUmidita=null;

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, mese);
			st.setString(2, localita);
			ResultSet rs = st.executeQuery();

			listaUmidita=new LinkedList<Rilevamento>();
			while (rs.next()) {
				Rilevamento r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data"), rs.getInt("Umidita"));
				listaUmidita.add(r);
			}
			rs.close();
			conn.close();
			return listaUmidita;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}//basandomi su questo metodo ne posso creare un altro 
	//che ritorna una mappa con chiave la città e come valore l'umidità media della città

	public List<String> getAllLocalita() {
		final String sql = "SELECT DISTINCT Localita FROM situazione";
		List<String> localita = new LinkedList<String>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				localita.add(rs.getString("Localita"));
			}
			conn.close();
			return localita;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public Rilevamento getRilevamentoCittaGiornoMese(String localita, int giorno, int mese) {
		final String sql = "SELECT Localita, Data, Umidita FROM situazione WHERE Localita=? AND DAY(Data)=? AND MONTH(Data)=? ";
		Rilevamento rilevamento=null;

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, localita);
			st.setInt(2, giorno);
			st.setInt(3,  mese);
			ResultSet rs = st.executeQuery();

			if(rs.next())
				rilevamento = new Rilevamento(rs.getString("Localita"), rs.getDate("Data"), rs.getInt("Umidita"));
			rs.close();
			conn.close();
			return rilevamento;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
