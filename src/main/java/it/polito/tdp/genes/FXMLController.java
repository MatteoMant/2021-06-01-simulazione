/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.genes;

import java.net.URL;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import it.polito.tdp.genes.model.Adiacenza;
import it.polito.tdp.genes.model.Genes;
import it.polito.tdp.genes.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="cmbGeni"
    private ComboBox<Genes> cmbGeni; // Value injected by FXMLLoader

    @FXML // fx:id="btnGeniAdiacenti"
    private Button btnGeniAdiacenti; // Value injected by FXMLLoader

    @FXML // fx:id="txtIng"
    private TextField txtIng; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimula"
    private Button btnSimula; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	this.model.creaGrafo();
    	txtResult.setText("Grafo creato!\n");
    	txtResult.appendText("# Vertici " + this.model.getNumVertici() + "\n");
    	txtResult.appendText("# Archi " + this.model.getNumArchi() + "\n");
    	
    	List<Genes> lista = new LinkedList<Genes>(this.model.getAllVertici());
    	Collections.sort(lista);
    	cmbGeni.getItems().addAll(lista);
    }

    @FXML
    void doGeniAdiacenti(ActionEvent event) {
    	txtResult.clear();
    	Genes gene = cmbGeni.getValue();
    	if (gene == null) {
    		txtResult.setText("Per favore selezionare un gene!\n");
    		return;
    	}
    	List<Adiacenza> vicini = this.model.getVerticiAdiacenti(gene);
    	Collections.sort(vicini);
    	txtResult.setText("Geni adiacenti a: " + gene + "\n");
    	for (Adiacenza a : vicini) {
    		txtResult.appendText(a.getG2() + " " + a.getPeso() + "\n");
    	}
    }

    @FXML
    void doSimula(ActionEvent event) {
    	
    	Genes start = cmbGeni.getValue() ;
    	if(start==null) {
    		txtResult.appendText("ERRORE: scegliere un gene\n");
    		return ;
    	}
    	
    	int n ;
    	try {
    		n = Integer.parseInt(txtIng.getText()) ;
    	} catch(NumberFormatException ex) {
    		txtResult.appendText("ERRORE: numero di ingegneri è obbligatorio e deve essere un numero\n");
    		return ;
    	}
    	
    	Map<Genes, Integer> geniStudiati = this.model.simula(start, n) ;
    	
    	if(geniStudiati==null) {
    		txtResult.appendText("ERRORE: il gene selezionato è isolato\n");
    	} else {
    		txtResult.appendText("Risultato simulazione:\n");
    		for(Genes g: geniStudiati.keySet()) {
    			txtResult.appendText(g+ " "+ geniStudiati.get(g)+ "\n");
    		}
    	}
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbGeni != null : "fx:id=\"cmbGeni\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnGeniAdiacenti != null : "fx:id=\"btnGeniAdiacenti\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtIng != null : "fx:id=\"txtIng\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
    
}
