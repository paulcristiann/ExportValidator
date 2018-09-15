package main.Main;

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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import static main.Main.ControllerMeniuPrincipal.fisierXML;


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
        fisierXML = null;
        ControllerMeniuPrincipal.fisierSemnat = null;

        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("MeniuPrincipal.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage curenta = (Stage) grid.getScene().getWindow();
        curenta.setTitle("Test");
        curenta.setScene(new Scene(root, 900, 700));
    }

    @FXML
    private void citireXML() throws ParseException {

        //validareXSD();

        try{

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();

            Document doc = db.parse(fisierXML);

            //Sectiune Mesaj Registru
            Label l = new Label("Informații referitoare la perioada de raportare");
            l.setFont(new Font("Arial", 22));
            grid.add(l, 0, 0);
            NodeList Lista = doc.getElementsByTagName("msj");
            Node MesajRegistru = Lista.item(0);
            NamedNodeMap AtributeMesajRegistru = MesajRegistru.getAttributes();
            Node IdMesaj = AtributeMesajRegistru.getNamedItem("idM");
            String SerieFiscala = IdMesaj.getNodeValue().substring(0, 10);
            grid.addColumn(0,new Label("Seria Fiscala este : " + IdMesaj.getNodeValue().substring(0, 10)));
            SimpleDateFormat parser = new SimpleDateFormat("yyyyMMddHHmmss");
            Date date = parser.parse(IdMesaj.getNodeValue().substring(10, 24));
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            grid.addColumn(0,new Label("Data emiterii raportului este: " + formatter.format(date)));
            grid.addColumn(0,new Label("Numarul raportului Z verificat este: " + IdMesaj.getNodeValue().substring(24,28)));

            //Sectiune Pentru Bonuri Fiscale
            NodeList ElementeDinMesajulTransmis = MesajRegistru.getChildNodes();
            for(int i = 0; i < ElementeDinMesajulTransmis.getLength(); i++) {

                if(ElementeDinMesajulTransmis.item(i).getNodeName().equals("bon")){

                    NamedNodeMap AtributeBonFiscal = ElementeDinMesajulTransmis.item(i).getAttributes();
                    Node IdBon = AtributeBonFiscal.getNamedItem("idB");
                    grid.addColumn(0,new Label(""));
                    grid.addColumn(0,new Label(""));
                    l = new Label("Sectiune referitoare la Bonul fiscal numarul " + IdBon.getNodeValue().substring(28,32));
                    l.setFont(new Font("Arial",22));
                    grid.addColumn(0,l);
                    grid.addColumn(0,new Label("Seria Fiscala a memoriei care a emis bonul este : " + IdBon.getNodeValue().substring(0, 10)));
                    parser = new SimpleDateFormat("yyyyMMddHHmmss");
                    date = parser.parse(IdBon.getNodeValue().substring(10, 24));
                    formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    grid.addColumn(0,new Label("Data emiterii bonului este: " + formatter.format(date)));
                    grid.addColumn(0,new Label("Bonul face parte din ziua fiscala nr.: " + IdBon.getNodeValue().substring(24,28)));
                    grid.addColumn(0,new Label("Numarul bonului in ziua respectiva: " + IdBon.getNodeValue().substring(28,32)));
                    Node TotalBonFiscal = AtributeBonFiscal.getNamedItem("totB");
                    grid.addColumn(0,new Label("Totalul vanzarilor de pe bonul fiscal este: " + TotalBonFiscal.getNodeValue()));
                    Node TotalTvaBonFiscal = AtributeBonFiscal.getNamedItem("totTva");
                    grid.addColumn(0,new Label("Totalul TVA este: " + TotalTvaBonFiscal.getNodeValue()));
                    NodeList CoteleTvaDePeBonulFiscal = ElementeDinMesajulTransmis.item(i).getChildNodes();
                    for(int j = 0; j < CoteleTvaDePeBonulFiscal.getLength(); j++){
                        if(CoteleTvaDePeBonulFiscal.item(j).getNodeType() == Node.ELEMENT_NODE) {

                            grid.addColumn(0, new Label("Totalul TVA pe cota " + CoteleTvaDePeBonulFiscal.item(j)
                                    .getAttributes().getNamedItem("cota").getNodeValue() + " este: " + CoteleTvaDePeBonulFiscal.item(j)
                                    .getAttributes().getNamedItem("tva")
                                    .getNodeValue()));
                        }
                    }
                }

                //Sectiunea Raportului Z
                if(ElementeDinMesajulTransmis.item(i).getNodeName().equals("rB")){
                    grid.addColumn(0,new Label(""));
                    grid.addColumn(0,new Label(""));
                    l = new Label("Sectiune referitoare la raportul Z");
                    l.setFont(new Font("Arial",22));
                    grid.addColumn(0,l);
                    NamedNodeMap AtributeRaportZ = ElementeDinMesajulTransmis.item(i).getAttributes();
                    Node idR = AtributeRaportZ.getNamedItem("idR");
                    grid.addColumn(0,new Label("Seria Fiscala este : " + idR.getNodeValue().substring(0, 10)));
                    parser = new SimpleDateFormat("yyyyMMddhhmmss");
                    date = parser.parse(idR.getNodeValue().substring(10, 24));
                    formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss ");
                    grid.addColumn(0,new Label("Data emiterii raportului Z este: " + formatter.format(date)));
                    grid.addColumn(0,new Label("Numarul raportului Z verificat este: " + idR.getNodeValue().substring(24,28)));
                    Node nrAv = AtributeRaportZ.getNamedItem("nrAv");
                    grid.addColumn(0,new Label("Numarul de avarii: " + nrAv.getNodeValue()));
                    Node nrB = AtributeRaportZ.getNamedItem("nrB");
                    grid.addColumn(0,new Label("Numarul de bonuri emise este: " + nrB.getNodeValue()));
                    Node totB = AtributeRaportZ.getNamedItem("totB");
                    grid.addColumn(0,new Label("Totalul bonurilor de pe Z este: " + totB.getNodeValue()));
                    Node nrBC = AtributeRaportZ.getNamedItem("nrBC");
                    grid.addColumn(0,new Label("Numarul de bonuri cu CIF: " + nrBC.getNodeValue()));
                    Node totBC = AtributeRaportZ.getNamedItem("totBC");
                    grid.addColumn(0,new Label("Totalul vanzarilor pe bonuri cu CIF este: " + totBC.getNodeValue()));
                    Node nrA = AtributeRaportZ.getNamedItem("nrA");
                    grid.addColumn(0,new Label("Numarul de anulari de produs: " + nrA.getNodeValue()));
                    Node totA = AtributeRaportZ.getNamedItem("totA");
                    grid.addColumn(0,new Label("Totalul operatiunilor de anulare este: " + totA.getNodeValue()));
                    Node nrR = AtributeRaportZ.getNamedItem("nrR");
                    grid.addColumn(0,new Label("Numarul reducerilor efectuate este: " + nrR.getNodeValue()));
                    Node totR = AtributeRaportZ.getNamedItem("totR");
                    grid.addColumn(0,new Label("Valoarea totala a reducerilor este: " + totR.getNodeValue()));
                    Node nrM = AtributeRaportZ.getNamedItem("nrM");
                    grid.addColumn(0,new Label("Numarul de majorari efectuate: " + nrM.getNodeValue()));
                    Node totM = AtributeRaportZ.getNamedItem("totM");
                    grid.addColumn(0,new Label("Total valoare majorari: " + totM.getNodeValue()));
                    Node totTva = AtributeRaportZ.getNamedItem("totTva");
                    grid.addColumn(0,new Label("Totalul TVA este: " + totTva.getNodeValue()));
                    Node totTvaC = AtributeRaportZ.getNamedItem("totTvaC");
                    grid.addColumn(0,new Label("Totalul TVA pe bonurile cu CIF: " + totTvaC.getNodeValue()));
                    Node totTaxe = AtributeRaportZ.getNamedItem("totTaxe");
                    grid.addColumn(0,new Label("Totalul altor taxe este: " + totTaxe.getNodeValue()));
                    Node totNet = AtributeRaportZ.getNamedItem("totNet");
                    grid.addColumn(0,new Label("Totalul operatiuni scutite de TVA: " + totNet.getNodeValue()));
                    Node sume_serv_in = AtributeRaportZ.getNamedItem("sume_serv_in");
                    grid.addColumn(0,new Label("Totalul sumelor de serviciu (intrare-sume_serv_in): " + sume_serv_in.getNodeValue()));
                    Node sume_serv_out = AtributeRaportZ.getNamedItem("sume_serv_out");
                    grid.addColumn(0,new Label("Totalul sumelor de serviciu (la finalul zilei" +
                            "-sume_serv_out): " + sume_serv_out.getNodeValue()));
                    Node monRef = AtributeRaportZ.getNamedItem("monRef");
                    grid.addColumn(0,new Label("Moneda de referinta: " + monRef.getNodeValue()));

                    NodeList ElementeDinRaportulZ = ElementeDinMesajulTransmis.item(i).getChildNodes();
                    for(int z=0; z<ElementeDinRaportulZ.getLength(); z++){

                        if(ElementeDinRaportulZ.item(z).getNodeName().equals("pl")){
                            grid.addColumn(0,new Label("Cu modalitatea de plata "+ElementeDinRaportulZ.item(z).getAttributes()
                            .getNamedItem("tipP").getNodeValue()+" s-au efectuat vanzari de "+ElementeDinRaportulZ.item(z).getAttributes()
                                    .getNamedItem("valPl").getNodeValue() + " " + ElementeDinRaportulZ.item(z).getAttributes().getNamedItem("monPl")
                            .getNodeValue()));
                        }

                        if(ElementeDinRaportulZ.item(z).getNodeName().equals("coteZ")){
                            grid.addColumn(0,new Label("Totalul TVA pe cota" +
                                    " " + ElementeDinRaportulZ.item(z).getAttributes().getNamedItem("cota").getNodeValue() +
                                    " este " + ElementeDinRaportulZ.item(z).getAttributes().getNamedItem("tva").getNodeValue() +
                                    ". Totalul vanzarilor pe cota este " +
                                    ElementeDinRaportulZ.item(z).getAttributes().getNamedItem("valOp").getNodeValue()));
                        }

                        if(ElementeDinRaportulZ.item(z).getNodeName().equals("av")){
                            grid.addColumn(0,new Label("In timpul zilei fiscale s-a inregistrat o avarie - " +
                                    ElementeDinRaportulZ.item(z).getAttributes().getNamedItem("data").getNodeValue()));
                        }

                    }
                }

                //Sectiune referitoare la evenimente
                if(ElementeDinMesajulTransmis.item(i).getNodeName().equals("mE")){
                    grid.addColumn(0,new Label(""));
                    grid.addColumn(0,new Label(""));
                    l = new Label("Sectiune referitoare la evenimente");
                    l.setFont(new Font("Arial",22));
                    grid.addColumn(0,l);
                    grid.addColumn(0,new Label("Numarul de bonuri emise in timpul evenimentelor este: " +
                            ElementeDinMesajulTransmis.item(i).getAttributes().getNamedItem("nrB").getNodeValue()));
                    NodeList Evenimente = ElementeDinMesajulTransmis.item(i).getChildNodes();
                    for(int k=0; k < Evenimente.getLength(); k++){

                        grid.addColumn(0,new Label("In timpul zilei fiscale a aparut un eveniment de tip " +
                                Evenimente.item(k).getAttributes().getNamedItem("tipE").getNodeValue()));

                        grid.addColumn(0,new Label("Inceputul evenimentului: " +
                                Evenimente.item(k).getAttributes().getNamedItem("dataI").getNodeValue()));

                        grid.addColumn(0,new Label("Sfarsitul evenimentului: " +
                                Evenimente.item(k).getAttributes().getNamedItem("dataF").getNodeValue()));

                        grid.addColumn(0,new Label("----------------------------------"));

                    }
                }

            }

        }catch(Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("A survenit o eroare");
            alert.setHeaderText("A fost detectata o eroare in program");
            alert.setContentText(e.toString());
            alert.showAndWait();
        }

    }

    private void validareXSD(){

        boolean ok = false;

        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

            File f = new File("XSD/xmlUzual.xsd");

            Schema schema = factory.newSchema(f);

            Validator validator = schema.newValidator();

            validator.validate(new StreamSource(fisierXML));
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

                File f = new File("XSD/xmlPR.xsd");

                Schema schema = factory.newSchema(f);

                Validator validator = schema.newValidator();

                validator.validate(new StreamSource(fisierXML));
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

                    File f = new File("XSD/xmlSV.xsd");

                    Schema schema = factory.newSchema(f);

                    Validator validator = schema.newValidator();

                    validator.validate(new StreamSource(fisierXML));
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

                        File f = new File("XSD/xmlTaxi.xsd");

                        Schema schema = factory.newSchema(f);

                        Validator validator = schema.newValidator();

                        validator.validate(new StreamSource(fisierXML));
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

}