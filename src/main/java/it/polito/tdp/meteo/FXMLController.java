/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.meteo;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.meteo.model.Citta;
import it.polito.tdp.meteo.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class FXMLController {
	
	Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxMese"
    private ChoiceBox<Integer> boxMese; // Value injected by FXMLLoader

    @FXML // fx:id="btnUmidita"
    private Button btnUmidita; // Value injected by FXMLLoader

    @FXML // fx:id="btnCalcola"
    private Button btnCalcola; // Value injected by FXMLLoader
    
    @FXML // fx:id="lblComunicazioni"
    private Label lblComunicazioni; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCalcolaSequenza(ActionEvent event) {
    	int mese;
    	try{
    		mese=boxMese.getValue();
    	} catch (Exception e) {
    		txtResult.clear();
    		txtResult.setText("Per favore, inserire il mese\n");
    		return;
      	}
    	List<Citta> soluzione= new ArrayList<Citta>(model.trovaSequenza(mese));    	
    	if(soluzione.size()==0) {
    		txtResult.setText("Abbiamo un problema");
    		return;
    	}
    	txtResult.setText("La soluzione migliore è: \n");
    	for(Citta c : soluzione)
    		txtResult.appendText(c.getNome()+"\n");
    }
    

    @FXML
    void doCalcolaUmidita(ActionEvent event) {
    	int mese;
    	try{
    		mese=boxMese.getValue();
    	} catch (Exception e) {
    		txtResult.clear();
    		txtResult.setText("Per favore, inserire il mese\n");
    		return;
      	}
    	HashMap<String,String> umiditaMedia=new HashMap<String,String>(model.getUmiditaMedia(mese));
    	txtResult.clear();
    	txtResult.setText("Le umidità medie per il "+mese+"° mese sono le seguenti:\n");
    	for(String s:umiditaMedia.keySet()) {
    		txtResult.appendText(s+" "+umiditaMedia.get(s)+"\n");
    	}
    }
    

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxMese != null : "fx:id=\"boxMese\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnUmidita != null : "fx:id=\"btnUmidita\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCalcola != null : "fx:id=\"btnCalcola\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model m) {
    	this.model=m;
    	boxMese.getItems().clear();
    	for(int i=1;i<13;i++) {
    		boxMese.getItems().add(i);    //metto degli integer, se voglio cambiare devo cambiare qua e sopra dove definisco la comboBox  
    	}
    }
}

