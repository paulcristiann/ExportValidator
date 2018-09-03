package main;

import java.awt.event.MouseEvent;
import java.beans.EventHandler;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.ControllerMeniuPrincipal;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import static javax.swing.JOptionPane.showMessageDialog;

public class ReadXMLFile {

    public GridPane grid;
    public RadioButton optiune;
    public Button start;
    public Label mesajOptiune;
    public Label labelValidareXSD;
    public AnchorPane pane;
    private Boolean Validare = false;

    @FXML
    private void goBack(){
        ControllerMeniuPrincipal.fisierXML = null;
        ControllerMeniuPrincipal.fisierSemnat = null;

        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("Meniu Principal.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage curenta = (Stage) grid.getScene().getWindow();
        curenta.setTitle("Test");
        curenta.setScene(new Scene(root, 900, 700));
    }

    @FXML
    private void citireXML(){

        validareXSD();

        if(optiune.isSelected()){
            Validare = true;
        }

        /*if(Validare.booleanValue() == false) {
            labelValidareXSD.setText("Doar citire");
        }else{
            labelValidareXSD.setText("Citire si validare cu datele din testul de calcule");
        }*/

        if(ControllerMeniuPrincipal.fisierXML == null)
        {
            mesajOptiune.setText("Nu a fost incarcat niciun fisier!");
            return;
        }

        start.setVisible(false);
        optiune.setVisible(false);
        mesajOptiune.setVisible(false);

        try {

            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder();

            Document doc = dBuilder.parse(ControllerMeniuPrincipal.fisierXML);

            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

            if (doc.hasChildNodes()) {

                printNote(doc.getChildNodes());

            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private void validareXSD(){

        boolean ok = false;

        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

            File f = new File("/Users/paul-cristianvasile/Desktop/ExportValidator/main/xmlUzual.xsd");

            Schema schema = factory.newSchema(f);

            Validator validator = schema.newValidator();

            validator.validate(new StreamSource(ControllerMeniuPrincipal.fisierXML));
            labelValidareXSD.setText("Validat automat cu XSD-ul de AMEF Uzual!");
            ok = true;

        } catch (IOException e){
            System.out.println(e.getMessage());
            //xmlUzualLabel.setText(e.getMessage());
        }catch(SAXException e1){
            //xmlUzualLabel.setText(e1.getMessage());
            System.out.println("SAX Exception: "+e1.getMessage());
        }

        if(ok == false){
            try {
                SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

                File f = new File("/Users/paul-cristianvasile/Desktop/ExportValidator/main/xmlPR.xsd");

                Schema schema = factory.newSchema(f);

                Validator validator = schema.newValidator();

                validator.validate(new StreamSource(ControllerMeniuPrincipal.fisierXML));
                labelValidareXSD.setText("Validat automat cu XSD-ul pentru Perioada de raportare (mesaj registru)!");
                ok = true;

            } catch (IOException e){
                System.out.println(e.getMessage());
                //xmlPRLabel.setText(e.getMessage());
            }catch(SAXException e1){
                //xmlPRLabel.setText(e1.getMessage());
                System.out.println("SAX Exception: "+e1.getMessage());
            }

            if(ok == false){
                try {
                    SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

                    File f = new File("/Users/paul-cristianvasile/Desktop/ExportValidator/main/xmlSV.xsd");

                    Schema schema = factory.newSchema(f);

                    Validator validator = schema.newValidator();

                    validator.validate(new StreamSource(ControllerMeniuPrincipal.fisierXML));
                    labelValidareXSD.setText("Validat cu XSD-ul pentru AMEF Schimb Valutar!");
                    ok = true;

                } catch (IOException e){
                    System.out.println(e.getMessage());
                    //xmlSVLabel.setText(e.getMessage());
                }catch(SAXException e1){
                    //xmlSVLabel.setText(e1.getMessage());
                    System.out.println("SAX Exception: "+e1.getMessage());
                }

                if(ok == false){
                    try {
                        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

                        File f = new File("/Users/paul-cristianvasile/Desktop/ExportValidator/main/xmlTaxi.xsd");

                        Schema schema = factory.newSchema(f);

                        Validator validator = schema.newValidator();

                        validator.validate(new StreamSource(ControllerMeniuPrincipal.fisierXML));
                        labelValidareXSD.setText("Validat!");
                        ok = true;

                    } catch (IOException e){
                        System.out.println(e.getMessage());
                        //xmlTaxiLabel.setText(e.getMessage());
                    }catch(SAXException e1){
                        //xmlTaxiLabel.setText(e1.getMessage());
                        System.out.println("SAX Exception: "+e1.getMessage());
                    }
                }
                }
            }
            if(ok == false){
                labelValidareXSD.setText("Atentie! Eroare la validarea fisierului cu XSD-ul ANAF!");
            }
    }

    private void printNote(NodeList nodeList) {

        for (int count = 0; count < nodeList.getLength(); count++) {

            Node tempNode = nodeList.item(count);

            // make sure it's element node.
            if (tempNode.getNodeType() == Node.ELEMENT_NODE) {

                // get node name and value
                System.out.println("\nNumele Nodului =" + tempNode.getNodeName() + " [OPEN]");

                if(tempNode.getNodeName().equals("mReg")) {
                    Label l = new Label("Informații referitoare la perioada de raportare");
                    l.setFont(new Font("Arial", 22));
                    grid.add(l, 0, 0);
                }

                if(tempNode.getNodeName().equals("msj")) {
                    Label l = new Label("Sectiune referitoare la mesajul semnat");
                    l.setFont(new Font("Arial", 22));
                    grid.add(l, 0, 0);
                }

                if(tempNode.getNodeName().equals("rB")){
                    grid.addColumn(0,new Label(""));
                    grid.addColumn(0,new Label(""));
                    Label l = new Label("Sectiune referitoare la raportul Z");
                    l.setFont(new Font("Arial",22));
                    grid.addColumn(0,l);
                }
                if(tempNode.getNodeName().equals("pl")){
                    grid.addColumn(0,new Label(""));
                    grid.addColumn(0,new Label(""));
                    Label l = new Label("Sectiune referitoare la modalitati de plata");
                    l.setFont(new Font("Arial",22));
                    grid.addColumn(0,l);
                }
                if(tempNode.getNodeName().equals("coteZ")){
                    grid.addColumn(0,new Label(""));
                    grid.addColumn(0,new Label(""));
                    Label l = new Label("Sectiune referitoare cota TVA de pe raportul Z");
                    l.setFont(new Font("Arial",22));
                    grid.addColumn(0,l);
                }
                if(tempNode.getNodeName().equals("mE")){
                    grid.addColumn(0,new Label(""));
                    grid.addColumn(0,new Label(""));
                    Label l = new Label("Sectiune referitoare la evenimente");
                    l.setFont(new Font("Arial",22));
                    grid.addColumn(0,l);
                }
                if(tempNode.getNodeName().equals("av")){
                    grid.addColumn(0,new Label(""));
                    grid.addColumn(0,new Label(""));
                    Label l = new Label("Sectiune referitoare la avarii");
                    l.setFont(new Font("Arial",22));
                    grid.addColumn(0,l);
                }

                System.out.println("Valorea nodului =" + tempNode.getTextContent());

                if (tempNode.hasAttributes()) {

                    // get attributes names and values
                    NamedNodeMap nodeMap = tempNode.getAttributes();

                    //Trecem prin toate atributele
                    for (int i = 0; i < nodeMap.getLength(); i++) {
                        Node node = nodeMap.item(i);
                        System.out.println("attr name : " + node.getNodeName());
                        //verificari de campuri
                        verificaValoare(node);
                        System.out.println("attr value : " + node.getNodeValue());
                    }

                }

                if (tempNode.hasChildNodes()) {
                    // loop again if has child nodes
                    printNote(tempNode.getChildNodes());
                }

                System.out.println("Node Name =" + tempNode.getNodeName() + " [CLOSE]");

            }

        }

    }

    private boolean verificaValoare(Node n){

        Boolean ok = Boolean.TRUE;

        switch (n.getNodeName()){

            case "cif":{
                if(Validare){
                    grid.addColumn(0,new Label("C.I.F.: " + n.getNodeValue() + " De verificat manual!"));
                }
                else {
                    grid.addColumn(0, new Label("C.I.F.: " + n.getNodeValue()));
                }
                break;
            }

            case "nrRapI":{
                grid.addColumn(0,new Label("Numarul primului Z raportat: " + n.getNodeValue()));
                break;
            }

            case "nrRapF":{
                grid.addColumn(0,new Label("Numarul ultimului Z raportat: " + n.getNodeValue()));
                break;
            }

            case "tip_amef":{
                String tip = "nedeclarat";
                switch(n.getNodeValue()){
                    case "U":{ tip = "Uzual"; break;}
                    case "T":{ tip = "Taximetrie"; break;}
                    case "S":{ tip = "Schimb Valutar"; break;}
                    case "A":{ tip = "Magazin aeroporturi"; break;}
                }
                grid.addColumn(0,new Label("Tipul de AMEF: " + tip));
                break;
            }

            case "idM":{
                try {

                    grid.addColumn(0,new Label("Seria Fiscala este : " + n.getNodeValue().substring(0, 10)));

                    SimpleDateFormat parser = new SimpleDateFormat("yyyymmddhhmmss");
                    Date date = parser.parse(n.getNodeValue().substring(10, 24));
                    SimpleDateFormat formatter = new SimpleDateFormat("dd-mm-yyyy HH:mm:ss ");
                    grid.addColumn(0,new Label("Data emiterii raportului este: " + formatter.format(date)));


                    grid.addColumn(0,new Label("Numarul raportului Z verificat este: " + n.getNodeValue().substring(24,28)));

                }catch (Exception e){
                    System.out.println(e);
                }
                break;
            }

            case "idB":{
                try{
                    grid.addColumn(0,new Label(""));
                    grid.addColumn(0,new Label(""));
                    Label l = new Label("Sectiune referitoare la Bonul fiscal numarul " + n.getNodeValue().substring(28,32));
                    l.setFont(new Font("Arial",22));
                    grid.addColumn(0,l);
                    grid.addColumn(0,new Label("Seria Fiscala este : " + n.getNodeValue().substring(0, 10)));
                    SimpleDateFormat parser = new SimpleDateFormat("yyyymmddhhmmss");
                    Date date = parser.parse(n.getNodeValue().substring(10, 24));
                    SimpleDateFormat formatter = new SimpleDateFormat("dd-mm-yyyy HH:mm:ss");
                    grid.addColumn(0,new Label("Data emiterii raportului este: " + formatter.format(date)));
                    grid.addColumn(0,new Label("Numarul raportului Z verificat este: " + n.getNodeValue().substring(24,28)));
                    grid.addColumn(0,new Label("Numarul bonului in ziua respectiva: " + n.getNodeValue().substring(28,32)));
                }catch (Exception e){
                    System.out.println(e);
                }
                break;
            }

            case "totB":{
                grid.addColumn(0,new Label("totB: " + n.getNodeValue()));
                break;
            }

            case "totTva":{
                grid.addColumn(0,new Label("Totalul TVA este: " + n.getNodeValue()));
                break;
            }

            case "cota":{
                grid.addColumn(0,new Label("Raport pe cota: " + n.getNodeValue()));
                //System.out.println("Pentru cota " + n.getNodeValue());
                break;
            }

            case "tva":{
                grid.addColumn(0,new Label("Totalul TVA pe aceasta cota este: " + n.getNodeValue()));
                break;
            }

            case "totTvaC": {
                grid.addColumn(0,new Label("Totalul TVA pe bonurile cu CIF: " + n.getNodeValue()));
                break;
            }

            case "totTaxe": {
                grid.addColumn(0,new Label("Totalul alte taxe: " + n.getNodeValue()));
                break;
            }

            case "totR": {
                grid.addColumn(0,new Label("Totalul reducerilor: " + n.getNodeValue()));
                break;
            }

            case "totNet": {
                grid.addColumn(0,new Label("Totalul operatiuni scutite de TVA: " + n.getNodeValue()));
                break;
            }

            case "totM": {
                grid.addColumn(0,new Label("Total valoare majorari: " + n.getNodeValue()));
                break;
            }

            case "totBC": {
                grid.addColumn(0,new Label("Totalul bonurilor cu CIF: " + n.getNodeValue()));
                break;
            }

            case "totA": {
                grid.addColumn(0,new Label("Totalul operatiunilor de anulare: " + n.getNodeValue()));
                break;
            }

            case "sume_serv_in": {
                grid.addColumn(0,new Label("Totalul sumelor de serviciu (intrare): " + n.getNodeValue()));
                break;
            }

            case "sume_serv_out": {
                grid.addColumn(0,new Label("Totalul sumelor de serviciu (iesire): " + n.getNodeValue()));
                break;
            }

            case "nrR": {
                grid.addColumn(0,new Label("Numarul reducerilor efectuate: " + n.getNodeValue()));
                break;
            }

            case "nrM": {
                grid.addColumn(0,new Label("Numarul de majorari efectuate: " + n.getNodeValue()));
                break;
            }

            case "nrBC": {
                grid.addColumn(0,new Label("Numarul de bonuri cu CIF: " + n.getNodeValue()));
                break;
            }

            case "nrB": {
                grid.addColumn(0,new Label("Numarul de bonuri emise: " + n.getNodeValue()));
                break;
            }

            case "nrAv": {
                grid.addColumn(0,new Label("Numarul de avarii: " + n.getNodeValue()));
                break;
            }

            case "nrA": {
                grid.addColumn(0,new Label("Numarul de anulari de produs: " + n.getNodeValue()));
                break;
            }

            case "monRef": {
                grid.addColumn(0,new Label("Moneda de referinta: " + n.getNodeValue()));
                break;
            }

            case "idR": {
                grid.addColumn(0,new Label("Seria Fiscala este : " + n.getNodeValue().substring(0, 10)));
                SimpleDateFormat parser = new SimpleDateFormat("yyyymmddhhmmss");
                Date date = null;
                try {
                    date = parser.parse(n.getNodeValue().substring(10, 24));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                SimpleDateFormat formatter = new SimpleDateFormat("dd-mm-yyyy HH:mm:ss ");
                grid.addColumn(0,new Label("Data emiterii raportului este: " + formatter.format(date)));
                grid.addColumn(0,new Label("Numarul raportului Z verificat este: " + n.getNodeValue().substring(24,28)));
                break;
            }

            case "valOp": {
                grid.addColumn(0,new Label("Valoarea operatiunilor: " + n.getNodeValue()));
                break;
            }

            case "tipP": {
                grid.addColumn(0,new Label("Tipul de plata: " + n.getNodeValue()));
                break;
            }

            case "valPl": {
                grid.addColumn(0,new Label("Valoare pe aceasta metoda: " + n.getNodeValue()));
                break;
            }

            case "monPl": {
                grid.addColumn(0,new Label("Moneda folosita la aceasta metoda de plata: " + n.getNodeValue()));
                break;
            }

            case "dataI": {
                grid.addColumn(0,new Label("Data de inceput a evenimentului este: " + n.getNodeValue()));
                break;
            }

            case "dataF": {
                grid.addColumn(0,new Label("Data de sfarsit a evenimentului este: " + n.getNodeValue()));
                break;
            }

            case "tipE": {
                grid.addColumn(0,new Label("Tipul evenimentului este: " + n.getNodeValue()));
                break;
            }

            case "data": {
                grid.addColumn(0,new Label("Data avariei este: " + n.getNodeValue()));
                break;
            }
        }

        return ok;
    }
}