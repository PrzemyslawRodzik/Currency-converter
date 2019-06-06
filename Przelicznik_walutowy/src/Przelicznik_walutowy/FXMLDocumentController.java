/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package przelicznik_walutowy;


import java.io.File;
import java.net.URL;


import java.util.*;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import static java.lang.System.in;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.application.Platform;




import javafx.scene.chart.AreaChart;

import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;


import org.xml.sax.SAXException;

/**
 *
 * @author Pszemek
 */
public class FXMLDocumentController extends Przelicznik_walutowy implements Initializable {
    
   //button
    @FXML
    private Button licz;
    @FXML
    private Button btnd;
    
    @FXML
    private TextField pole1;
     @FXML
    private TextField polewynik;
     
    @FXML
    private ComboBox<String> lista_z;
    @FXML
    private ComboBox<String> lista_na;
    
    
   
    private File kursy;
    private String data_kursow;
    private List<Double> rates;
    private List<String> kod_waluty;
    private Document doc;
    private int day;
    private String urls,baza;
    private Date data;
    private boolean nacisniety;
    
    @FXML
    private DatePicker cal;
    private String stringBaza;
   
    //Label
    @FXML
    private Label kod_z;
     @FXML
    private Label info;
    @FXML
    private Label kod_na;
    @FXML
    private Label kursy_show;
     @FXML
    private Label przelicz_label;
    
     
    @FXML
    private AreaChart<?,?> wykres;
    
    
    
   

    
   
  
    
    
    private void init_datapicker(){
        
        
cal.setDayCellFactory(e -> new DateCell() { 
        @Override
        public void updateItem(LocalDate date, boolean empty) {
            super.updateItem(date, empty);
            LocalDate today = LocalDate.now();
            LocalDate year= LocalDate.of(2019, 1, 1); 
            setDisable(empty || date.compareTo(today) > 0 || date.isBefore(year) );
           // setDisable(empty || date.isBefore(year));
            
        }
    });
        
        
    }
    private void dodawanie_wykresu(List<Double>rates,List<String>kod_waluty)
    {   
        wykres.getData().clear();
        
        XYChart.Series series = new XYChart.Series();
        for (int i = 0; i <kod_waluty.size(); i++) {
            series.getData().add(new XYChart.Data(String.valueOf(kod_waluty.get(i)),rates.get(i)));
        }
        
         
       
       
        wykres.setOpacity(0.8);
      
      wykres.setCreateSymbols(false);
        wykres.getData().add(series);
        
        
    }
  
private File pobieranie_archiwum()
{

    File f=new File("dir.txt");
    try (InputStream in = URI.create(stringBaza).toURL().openStream())
    {
        Files.copy(in, Paths.get("dir.txt"),StandardCopyOption.REPLACE_EXISTING);

    }catch( Exception  ex)
    {

        komunikat("Brak dostepu do internetu! Nie udalo sie pobrac pliku.");
    }
return f;

}
    private  void download(String url) throws Exception
    {   String filename="waluty_NBP"+day+".xml";
    

        File f=new File(filename);
        try (InputStream in = URI.create(url).toURL().openStream())
        {
            Files.copy(in, Paths.get(filename),StandardCopyOption.REPLACE_EXISTING);
            doc=inicjalizacja_Dom(filename);
        }catch( Exception  ex)
        {

            komunikat("Nie udalo sie pobrac pliku.");
        }


         
        
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        baza="waluty_NBP29.xml";
         data = new Date();
        day=data.getDate();
        nacisniety=false;
        
        stringBaza="https://www.nbp.pl/kursy/xml/dir.txt";
      

        pobieranie_archiwum();
        
        doc=inicjalizacja_Dom(baza);
        parseXMl();
        lista_z.getSelectionModel().select(7);
        lista_na.getSelectionModel().selectLast();
        kod_z.setText("EUR");
        kod_na.setText("ZL");
        
        
        
        init_datapicker();
        komunikat("Domyslnie wczytano kursy walut z 29 maja 2019r");
        dodawanie_wykresu(rates,kod_waluty);
        
    

        
      
        
      pole1.textProperty().addListener((e) -> {
          if (pole1.getText().isEmpty()) 
              return;
          else
              przelicz();
          
                  
              
          
         // przelicz();
                
          
          
          
    // ...
});
    
        
        
        lista_z.setOnAction((e) -> {
                
            if (nacisniety==true) {
                e.consume();
                return;
            }
             kod_z.setText(kod_waluty.get(lista_z.getSelectionModel().getSelectedIndex()));
             przelicz();
             
        });
        lista_na.setOnAction((e)->{
             
            
                if (nacisniety==true) {
                e.consume();
                return;
            }
            
              kod_na.setText(kod_waluty.get(lista_na.getSelectionModel().getSelectedIndex()));
              przelicz();
        });
        
       
        
       






        
        
    }    

    @FXML
    private void handlePrzeliczAction(ActionEvent event)  {
       przelicz();
        
    }
   
    private void otwieranie_startowe(){
      
        
    }
    private Document inicjalizacja_Dom(String nazwa_pliku){
        
        File f=new File(nazwa_pliku);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder dBuilder;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            
                return dBuilder.parse(f);
            
        } 
        catch (ParserConfigurationException | SAXException | IOException ex) 
        {
            
            
                return null;
        
        
        }
        
         
          
         
        
        
    }
    private void parseXMl() {
        
         rates= new ArrayList<>();
         kod_waluty=new ArrayList<>();
         
        lista_na.getItems().clear();
       lista_z.getItems().clear();
       
            
        
      
         
         
        
        
       
         PrintWriter zapis = null;
        try {
            zapis = new PrintWriter("zapis_po_parsie");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
         doc.getDocumentElement().normalize();
         
        zapis.println("Root element :" + doc.getDocumentElement().getNodeName());
         NodeList nList = doc.getElementsByTagName("pozycja");
        data_kursow=doc.getElementsByTagName("data_publikacji").item(0).getTextContent();
         
         
         
         for (int selectionDate = 0; selectionDate < nList.getLength(); selectionDate++) {
            Node nNode = nList.item(selectionDate);
            zapis.println("\nCurrent Element :" + nNode.getNodeName());
            
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
               Element eElement = (Element) nNode;
               String inp=eElement.getElementsByTagName("nazwa_waluty").item(0).getTextContent();
               lista_na.getItems().add(inp);
               lista_z.getItems().add(inp);
               zapis.println("Nazwa waluty : " 
                  + eElement.getElementsByTagName("nazwa_waluty").item(0).getTextContent());
              
               zapis.println("przelicznik : " 
                  + eElement
                  .getElementsByTagName("przelicznik")
                  .item(0)
                  .getTextContent());
               String kurs=eElement.getElementsByTagName("kurs_sredni").item(0).getTextContent();
               
               rates.add(Double.valueOf(kurs.replace(',','.')));
               zapis.println("kod_waluty : " 
                  + eElement
                  .getElementsByTagName("kod_waluty")
                  .item(0)
                  .getTextContent());
               kod_waluty.add(eElement.getElementsByTagName("kod_waluty").item(0).getTextContent());
               zapis.println("kurs_sredni : " 
                  + eElement
                  .getElementsByTagName("kurs_sredni")
                  .item(0)
                  .getTextContent());
            }
         }
         lista_na.getItems().add("Zloty polski");
               lista_z.getItems().add("Zloty polski");
               rates.add(1.0);
               kod_waluty.add("ZL");
               
               lista_z.getSelectionModel().select(7);
        lista_na.getSelectionModel().selectLast();
               
        dodawanie_wykresu(rates,kod_waluty);
         zapis.close();
         nacisniety=false;
         kursy_show.setText("Kursy z dnia "+data_kursow);
      } 
      
        
        
        
    
 private double zamiana_na_zl(double liczba,double rate1)
{


return rate1*liczba;
}


private void komunikat(String kom)
{
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setHeaderText(null);
    alert.setContentText(kom);
    alert.showAndWait();
}
private double zamiana_koncowa(double y,double rate2)
{


return y/rate2;
}
   private void przelicz()
    {
        
        
        int temp1=lista_z.getSelectionModel().getSelectedIndex();
        int temp2=lista_na.getSelectionModel().getSelectedIndex();
         
        
       
        double rate1,rate2,liczba,y,wynik;
        try {
            String liczbaString=pole1.getText().replaceAll(",",".");
            liczba=Double.valueOf(liczbaString);
             
             rate1=rates.get(temp1);
             rate2=rates.get(temp2);
             
                
            
            
        
        y=zamiana_na_zl(liczba,rate1);
        wynik=zamiana_koncowa(y,rate2);
        
            
                polewynik.setText(String.valueOf(wynik));
                info.setText("1 "+kod_waluty.get(temp1)+" = "+wynik/liczba+" "+kod_waluty.get(temp2)+", wedlug sredniego kursu NBP z dn. "+data_kursow);
                
            
        
        } 
        catch(NumberFormatException ex){
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Nie ladnie");
    alert.setHeaderText("Error");
    alert.setContentText("Nie wpisano liczby!");
    alert.showAndWait().ifPresent(e->{
        if(e==ButtonType.OK)
        {
            Platform.runLater(() -> {         // dlaczego to musi byc : https://stackoverflow.com/questions/30465313/javafx-textfield-with-listener-gives-java-lang-illegalargumentexception-the-s
            pole1.clear(); 
        }); 
            
            polewynik.setText("0,00");
            
        }
        
    });
                
           
        }
       
        
        
        
        
      
        
        
        System.out.println(temp1);

   
    }
  

    @FXML
    private void handle_pobierz(ActionEvent event) throws Exception {
        
        nacisniety=true;
        if (!Confirmation())
            return;
        
        
        
        String zmienna=null;
        int jaki_dzien_tygodnia,godzina;
        String selectionDate=null;
        
        try {
            cal.getValue().toString();
        }catch(Exception ex){
            komunikat("Nie wprowadziles zadnej daty!");
            return;
        }
        selectionDate=cal.getValue().toString();
        
        
      
        
        



        Calendar c =  Calendar.getInstance();
        String tempGodzina=c.getTime().toString().substring(11,13);
        if (tempGodzina.startsWith("0")){
            godzina=Integer.valueOf(tempGodzina.replaceAll("0",""));
        }
        else
        {
            godzina=Integer.valueOf(c.getTime().toString().substring(11,13));
        }


        jaki_dzien_tygodnia=cal.getValue().getDayOfWeek().getValue();


        
        if( jaki_dzien_tygodnia==6 || jaki_dzien_tygodnia==7)
        {

            komunikat("W soboty i w niedziele nie sa aktualizowane nowy kursy. Wybierz tylko dzien roboczy.");
             return;
        }
        String obecnyDzien=c.getTime().toString();
        
        System.out.println(obecnyDzien);
        int i=5;
        if (godzina<13 && i==1 ){
            komunikat("Kursy z dzisiejszego dnia sa dodawane po godzinie 13:00");
            return;
        }

        selectionDate=selectionDate.replaceAll("-","").substring(2);
        obecnyDzien=obecnyDzien.replaceAll("-","").substring(2);
      



        day=Integer.valueOf(selectionDate);

        Scanner fileScanner = new Scanner(new File("dir.txt"));


        Pattern pattern =  Pattern.compile("a.*"+selectionDate);
        Matcher matcher = null;
        while(fileScanner.hasNextLine()){
            String line = fileScanner.nextLine();

            matcher = pattern.matcher(line);
            if(matcher.find()){

                zmienna=line;
                System.out.println(zmienna);

            }


        }




        String tempUrl="http://www.nbp.pl/kursy/xml/"+zmienna+".xml";




        
        
        
        
        download(tempUrl);
        parseXMl();

        
        
        
    }
    private boolean Confirmation() {
        Alert alert = new Alert(AlertType.NONE);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Jestes pewny ze chcesz pobrac plik z kursami? Obecne kursy zostana nadpisane!");
        ButtonType bTAK = new ButtonType("TAK");
        ButtonType bNIE = new ButtonType("NIE");
        alert.getButtonTypes().addAll(bTAK, bNIE);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == bTAK) {
            return true;
        }
        else

            return false;


    }

    //  dodawanie tych kursow do bazy danych + ladniejsze okienka itp. / + moze wykres! / dodac komentarze

       private  boolean ContainsDuplicateCh(String s, char c)
{
   
   char[] tablica=s.toCharArray();
    for (int i = 0; i <s.length(); i++) {
        System.out.println(tablica[i]);
    }
   
    int ile=0;
    for (int i = 0; i < s.length(); i++)
    {
        
        if (tablica[i] != c)
        { }
        else{
            ile=ile+1;
        }
        if(ile>=2)
            return true;
            
    }
     
    return false;
}
@FXML
    private void walidac(javafx.scene.input.KeyEvent event) {
         
    int c = event.getCharacter().hashCode();
    String input=event.getCharacter();
  
 
   
   
    if( !("0123456789,.".contains(input) || c==8  ) ) 
    {   
        System.out.print("%");
        event.consume();
        
        
    }
     
    
    
        
      
        
        
    

    }
   

   

   


    

    
    
}
