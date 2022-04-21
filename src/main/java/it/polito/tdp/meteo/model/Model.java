package it.polito.tdp.meteo.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.meteo.DAO.MeteoDAO;

public class Model {
	
	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;
	private MeteoDAO meteo;
	private List<String> listaTutteLocalita;
	private static Integer costoMin;
	private static List<Citta> soluzione;
	
	
	
	public Model() {
		this.meteo=new MeteoDAO();
		listaTutteLocalita=new ArrayList<String>(meteo.getAllLocalita());
		soluzione=new ArrayList<Citta>();
	}


	public Map<String,String> getUmiditaMedia(int mese) {
		Map<String,String> mappa=new HashMap<String,String>();
		
		for(String localita : listaTutteLocalita) {
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
	
	
	public List<Citta> trovaSequenza(int mese) {
		List<Citta> parziale=new ArrayList<Citta>();
		soluzione.clear();
		int costo=0;
		costoMin=Integer.MAX_VALUE;
		cerca(0, parziale, mese, costo);
		return soluzione;
	}
	

	
	private void cerca(int livello, List<Citta> parziale, int mese, int costo) {
		
		if(!soluzioneValida(parziale))          //se soluzione non valida, non ha senso continuare
				return;
		
		if(livello>=NUMERO_GIORNI_TOTALI) {       //condizione uscita
			if(costo<costoMin) {
				soluzione=new ArrayList<Citta>(parziale);
				costoMin=costo;
			}
			return;
		}
		if(livello==0) {
			for(String localita : listaTutteLocalita) {
				Rilevamento r=meteo.getRilevamentoCittaGiornoMese(localita, livello+1, mese);
				Citta citta=new Citta(localita, r);
				parziale.add(citta);
				costo=r.getUmidita();
				livello++;
				cerca(livello, parziale, mese, costo);
				parziale.remove(parziale.size()-1);          //backtracking
				costo=0;
				livello=0;
			}
		} else {
			for(String localita : listaTutteLocalita) {
				Rilevamento r=meteo.getRilevamentoCittaGiornoMese(localita, livello+1, mese);
				Citta citta=new Citta(localita, r);
				parziale.add(citta);
				int costoVecchio=costo;
				costo=costo+r.getUmidita();
				if(parziale.get(parziale.size()-2).getNome().compareTo(localita)!=0)
					costo=costo+100;
				livello++;
				cerca(livello, parziale, mese, costo);
				parziale.remove(parziale.size()-1);          //backtracking
				costo=costoVecchio;
				livello--;
			}
		}
		
	}

	
	
	private boolean soluzioneValida(List<Citta> parziale) {
		if(parziale.size()> NUMERO_GIORNI_TOTALI)                   //cotrollo lunghezza richiesta  
			return false;
		
		for(String s : listaTutteLocalita) {                  //controllo giorni max citta
			int cont=0;
			for(Citta c : parziale)
				if(c.getNome().compareTo(s)==0)
					cont++;
			if(cont> NUMERO_GIORNI_CITTA_MAX)
				return false;
		}
		int cnt=0;               //controllo che ci sia il numero di giorni consecutivi minimi per città
		for(int i=0; i<parziale.size(); i++) {
			if(i==0) {
				cnt++;
			} else {
				if(parziale.get(i).getNome().compareTo(parziale.get(i-1).getNome())==0) {
					cnt++;
				} else {
					if(cnt>=NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN) {
						cnt=1;
					} else {
						return false;
					}
				}
			}
		}
		
		return true;
	}
	
	
	
	
	public void cerca2(int livello, List<Citta> soluzione, List<Citta> parziale, int mese, int costo, int costoMin) {
	
		if(livello>=15) {  //condizione uscita
			for(Citta c : parziale)
				if(c.getCounter()>=7)
					return;
			
			if(costo<costoMin)
				soluzione=new ArrayList<Citta>(parziale);
			return;
		}
		
		
		if(livello==0) {    //al primo giro eseguo questa serie di istruzioni
			for(String localita : listaTutteLocalita) {
				Rilevamento r1=meteo.getRilevamentoCittaGiornoMese(localita, 1, mese);
				Rilevamento r2=meteo.getRilevamentoCittaGiornoMese(localita, 2, mese);
				Rilevamento r3=meteo.getRilevamentoCittaGiornoMese(localita, 3, mese);
				Citta citta1=new Citta(localita, r1, 1);
				Citta citta2=new Citta(localita,r2, 2);
				Citta citta3=new Citta(localita,r3, 3);
				parziale.add(citta1);
				parziale.add(citta2);
				parziale.add(citta3);
				int costoVecchio=costo;
				costo=r1.getUmidita()+r2.getUmidita()+r3.getUmidita();
				cerca2(3, soluzione, parziale, mese, costo, costoMin);
				parziale.clear();   //backtracking
				costo=costoVecchio;
			}
		} else {
			for(String localita : listaTutteLocalita) {
				if(parziale.get(parziale.size() -1 ).getNome().compareTo(localita)==0) {
					Rilevamento r=meteo.getRilevamentoCittaGiornoMese(localita, livello+1, mese);
					Citta citta=new Citta(localita, r, parziale.get(parziale.size()-1).getCounter() +1 );
					parziale.add(citta);
					int costoVecchio=costo;
					cerca2(livello++, soluzione, parziale, mese, costo, costoMin);
					parziale.remove(parziale.size()-1);
					costo=costoVecchio;
				} else {      //TODO inserire controllo sui giorni
						Rilevamento r1=meteo.getRilevamentoCittaGiornoMese(localita, livello+1, mese);
						Rilevamento r2=meteo.getRilevamentoCittaGiornoMese(localita, livello+2, mese);
						Rilevamento r3=meteo.getRilevamentoCittaGiornoMese(localita, livello+3, mese);
						
						int cnt=0;
						for(Citta c : parziale) 
							if(c.getNome().compareTo(localita)==0)
								cnt++;	
						
						Citta citta1=new Citta(localita, r1, cnt+1);
						Citta citta2=new Citta(localita, r2, cnt+2);
						Citta citta3=new Citta(localita, r3, cnt+3);
						
						parziale.add(citta1);
						parziale.add(citta2);
						parziale.add(citta3);
						
						int costoVecchio=costo;
						costo=r1.getUmidita()+r2.getUmidita()+r3.getUmidita()+COST;
						cerca2(livello+3, soluzione, parziale, mese, costo, costoMin);
						parziale.remove(parziale.size()-1);   //backtracking
						parziale.remove(parziale.size()-1);
						parziale.remove(parziale.size()-1);
						costo=costoVecchio;
				}
			}
		}
	}
}
