package it.polito.tdp.meteo.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.meteo.DAO.MeteoDAO;

public class Model {
	
	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;
	private MeteoDAO meteo=new MeteoDAO();

	public Model() {
	}

	
	public Map<String,String> getUmiditaMedia(int mese) {
		Map<String,String> mappa=new HashMap<String,String>();
		
		for(String localita : meteo.getAllLocalita()) {
			int sommatoriaUmidita=0;
			int giorniMese=0;
			for(Rilevamento r : meteo.getAllRilevamentiLocalitaMese(mese, localita)) {
				sommatoriaUmidita+=r.getUmidita();
				giorniMese++;
			}
			Integer umiditaMedia=sommatoriaUmidita/giorniMese;
			mappa.put(localita, umiditaMedia.toString());
		}
		return mappa;
	}
	
	
	public String trovaSequenza(int mese) {
		return "TODO!";
	}
	

}
