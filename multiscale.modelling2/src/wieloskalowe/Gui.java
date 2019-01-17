package wieloskalowe;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import javafx.scene.control.TextField;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import java.net.URL;
import java.util.*;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

public class Gui implements Initializable {
    public int szerokosc, wysokosc, ziarna;
    CA ca;

    List<Color> wybraneziarna;

    public TextField szer;
    public ChoiceBox<String> boxtypnukleacji;
    public TextField MCiteracje;
    public TextField wys;
    public TextField iloscziaren;
    public TextField iloscinkluzji;
    public TextField rozmiarinkluzji;
    public ChoiceBox<String> boxinkluzje;
    public ChoiceBox<String> boxstruktury;
    public ChoiceBox<String> boxenergie;
    public ChoiceBox<String> boxnukleacje;
    public ChoiceBox<String> boxwidoki;
    public Canvas canvas;
    public Label wybraneziarnaLabel;
    public CheckBox moorecheckbox;
    public TextField recrystallIterationsText;
    public TextField ilosc_rekrystalizacja;
    public TextField prawdopodobienstwo_moore;
    public GraphicsContext graphicsContext;
    public TextField fazyMC;
    public TextField hminText;
    public TextField hmaxText;
    ObservableList listainkluzje = FXCollections.observableArrayList();
    ObservableList listastruktury = FXCollections.observableArrayList();
    ObservableList listaenergie = FXCollections.observableArrayList();
    ObservableList listatypnukleacji = FXCollections.observableArrayList();
    ObservableList listanukleacjie = FXCollections.observableArrayList();
    ObservableList listawidoki = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        graphicsContext = canvas.getGraphicsContext2D();
        loadData();
        wybraneziarna = new ArrayList();
        wybraneziarnaLabel.setText("Selected Grains: 0");
        boxinkluzje.setValue("Disabled");
        boxstruktury.setValue("Dual-Phase");
        boxenergie.setValue("Heterogeneous");
        boxtypnukleacji.setValue("At the begining");
        boxnukleacje.setValue("Random");
        boxwidoki.setValue("Default");
        wyczysc();

    }

    public void loadData() {
        listainkluzje.removeAll(listainkluzje);
        listainkluzje.addAll( "Square Random", "Square GB", "Circle Random", "Circle GB");
        boxinkluzje.getItems().addAll(listainkluzje);
        listastruktury.addAll( "Substructure", "Dual-Phase");
        boxstruktury.getItems().addAll(listastruktury);
        listaenergie.addAll("Homogeneous", "Heterogeneous");
        boxenergie.getItems().addAll(listaenergie);
        listatypnukleacji.addAll( "Constant", "Increasing", "At the begining");
        boxtypnukleacji.getItems().addAll(listatypnukleacji);
        listanukleacjie.addAll( "Grain Boundary", "Random");
        boxnukleacje.getItems().addAll(listanukleacjie);
        listawidoki.addAll("Default", "Energy","Recrystallized");
        boxwidoki.getItems().addAll(listawidoki);

    }

    public void przygotuj(ActionEvent e) {
        szerokosc = Integer.parseInt(szer.getText());
        wysokosc = Integer.parseInt(wys.getText());
        ziarna = Integer.parseInt(iloscziaren.getText());
        canvas = new Canvas(szerokosc, wysokosc);
        graphicsContext.clearRect(0, 0, szerokosc, wysokosc);
        ca = new CA(szerokosc, wysokosc);
        ca.ziarnowanie(ziarna);
        henergy=false;
        ca.prawdopodobienstwo_moore = Integer.parseInt(prawdopodobienstwo_moore.getText());
        ca.moore = moorecheckbox.isSelected();
        for (int i = 0; i < szerokosc; i++) {
            for (int j = 0; j < wysokosc; j++) {
                graphicsContext.setFill(ca.komorki[i + 1][j + 1].stan);
                graphicsContext.fillRect(i, j, 1, 1);
            }
        }
    }

    public void wyczysc()
    {
        graphicsContext.clearRect(0, 0, 650, 650);
        szerokosc = Integer.parseInt(szer.getText());
        wysokosc = Integer.parseInt(wys.getText());
        ziarna = Integer.parseInt(iloscziaren.getText());
        canvas = new Canvas(szerokosc, wysokosc);
        graphicsContext.clearRect(0, 0, szerokosc, wysokosc);
        ca = new CA(szerokosc, wysokosc);

    }

    public void Run(ActionEvent e) {
        if (moorecheckbox.isSelected()) {
            ca.moore = true;
            ca.prawdopodobienstwo_moore = Integer.parseInt(prawdopodobienstwo_moore.getText());
        } else {
            ca.moore = false;
        }
        for (int i = 0; i < szerokosc; i++) {
            for (int j = 0; j < wysokosc; j++) {
                graphicsContext.setFill(ca.komorki[i + 1][j + 1].stan);
                graphicsContext.fillRect(i, j, 1, 1);
            }
        }

        int iter = 0;
        while (!ca.ZapelnieniePlanszy()) {
            iter++;
            if (iter > 1000) break;
            ca.NastepnyKrok();
        }
        for (int i = 0; i < szerokosc; i++) {
            for (int j = 0; j < wysokosc; j++) {
                graphicsContext.setFill(ca.komorki[i + 1][j + 1].stan);
                graphicsContext.fillRect(i, j, 1, 1);
            }
        }
    }

    @FXML
    public void BmpExport() throws Exception {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("export  bmp");
        fileChooser.setInitialDirectory(new File("export/"));
        fileChooser.setInitialFileName("bitmap.bmp");
        File file = fileChooser.showSaveDialog(new Stage());
        WritableImage wim = new WritableImage(szerokosc, wysokosc);
        graphicsContext.getCanvas().snapshot(null, wim);
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(wim, null), "png", file);
        } catch (Exception s) {
        }

    }

    public void TxtExport() throws Exception {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Export txt ");
        chooser.setInitialDirectory(new File("export/"));
        chooser.setInitialFileName("text.txt");
        File file = chooser.showSaveDialog(new Stage());

        if (file != null) {
            List<String> data = new ArrayList<>();
            data.add(String.valueOf(szerokosc)+"\n");
            data.add(String.valueOf(wysokosc)+"\n");
            for(int i=0;i<szerokosc;i++){
                for(int j=0;j<wysokosc;j++){
                    data.add(ca.komorki[i][j].stan+"\n"+ca.komorki[i][j].faza+"\n");
                }
            }
            FileWriter writer = new FileWriter(file);
            for (String item : data) {
                writer.write(item);
            }
            writer.close();
        }
    }

    @FXML
    public void BmpImport() throws Exception {
        wyczysc();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import from bitmap");
        fileChooser.setInitialDirectory(new File("import/"));
        File file = fileChooser.showOpenDialog(new Stage());
        BufferedImage img = null;

        try {
            img=ImageIO.read(file);
        } catch (Exception s) {
        }

        Image image = SwingFXUtils.toFXImage(img, null);
        graphicsContext.getCanvas().getGraphicsContext2D().drawImage(image,0,0);
        ca=new CA(szerokosc,wysokosc);
        for(int i=0;i<szerokosc;i++){
            for(int j=0;j<wysokosc;j++){
                ca.poprzednie[i+1][j+1].stan=(image.getPixelReader().getColor(i,j));
                ca.komorki[i+1][j+1].stan=(image.getPixelReader().getColor(i,j));

            }
        }
        odswiez_komorki();
    }
    public void TxtImport() throws Exception {
        wyczysc();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import txt");
        fileChooser.setInitialDirectory(new File("import/"));
        File file = fileChooser.showOpenDialog(new Stage());

        try {
            List<String> lines = new ArrayList<>();
            try
            {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String st;
                while ((st = br.readLine()) != null){
                    lines.add(st);
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            ca=new CA(Integer.parseInt(lines.get(0)),Integer.parseInt(lines.get(1)));
            int index=0;
            szerokosc=Integer.parseInt(lines.get(0))+1;
            wysokosc=Integer.parseInt(lines.get(1))+1;
            for(int i=0;i<Integer.parseInt(lines.get(0));i++){
                for(int j=0;j<Integer.parseInt(lines.get(1));j++){
                    index+=2;
                    ca.poprzednie[i+1][j+1].stan=(Color.valueOf( lines.get(index)));
                    ca.poprzednie[i+1][j+1].faza=(Integer.parseInt( lines.get(index+1)));
                    ca.komorki[i+1][j+1].stan=(Color.valueOf( lines.get(index)));
                    ca.komorki[i+1][j+1].faza=(Integer.parseInt( lines.get(index+1)));
                }
            }

        } catch (Exception s) {}

        odswiez_komorki();
    }

    public void odswiez_komorki() {
        for (int i = 0; i < szerokosc; i++) {
            for (int j = 0; j < wysokosc; j++) {
                graphicsContext.setFill(ca.poprzednie[i + 1][j + 1].stan);
                graphicsContext.fillRect(i, j, 1, 1);
            }
        }
    }

    public void odswiez_zrekrystalizowane() {
        for (int i = 0; i < szerokosc; i++) {
            for (int j = 0; j < wysokosc; j++) {
                if(!ca.poprzednie[i+1][j+1].zrekrystalizowana)
                    graphicsContext.setFill(ca.poprzednie[i + 1][j + 1].stan);
                else{
                    graphicsContext.setFill(Color.color(ca.poprzednie[i + 1][j + 1].stan.getRed(),0.0,0.0));
                }
                graphicsContext.fillRect(i, j, 1, 1);
            }
        }
    }

    public void dodajinkluzje() {
        ca.Dodajinkluzje(boxinkluzje.getValue(), Integer.parseInt(iloscinkluzji.getText()), Integer.parseInt(rozmiarinkluzji.getText()));
        odswiez_komorki();
    }

    public void dodaj_ziarno(MouseEvent mouseEvent) {
        if (mouseEvent.isAltDown()) {
            if (wybraneziarna.contains(ca.komorki[(int) mouseEvent.getX() + 1][(int) mouseEvent.getY() + 1].stan))
                wybraneziarna.remove(ca.komorki[(int) mouseEvent.getX() + 1][(int) mouseEvent.getY() + 1].stan);
        } else {
            if (!wybraneziarna.contains(ca.komorki[(int) mouseEvent.getX() + 1][(int) mouseEvent.getY() + 1].stan))
                wybraneziarna.add(ca.komorki[(int) mouseEvent.getX() + 1][(int) mouseEvent.getY() + 1].stan);
        }
        wybraneziarnaLabel.setText("Selected Grains: " + wybraneziarna.size());
    }
    public void button_usunziarna() {
        ca.usunziarna(wybraneziarna);
        odswiez_komorki();
    }
    public void po_usunieciu() {
        ziarna = Integer.parseInt(iloscziaren.getText());
        ca.ziarnowanie(ziarna);
        wybraneziarna.clear();
        wybraneziarnaLabel.setText("Selected Grains: " + wybraneziarna.size());
        odswiez_komorki();
        ca.czyscgranice();




    }

    public void Clearentire() {
        ca.czysc_kolory();
        odswiez_komorki();
    }

    public void MCprepare() {
        ziarna = Integer.parseInt(fazyMC.getText());
        ca.MCinit(ziarna);
        odswiez_komorki();
    }

    public void MCrun() {
        int iteration = Integer.parseInt(MCiteracje.getText());
        for (int i = 0; i < iteration; i++) {
            ca.MCnastepny();
        }
        odswiez_komorki();
    }

    public boolean mode = true;
    public boolean henergy = false;

    public void zmienwidok() {
        if (boxwidoki.getValue().equalsIgnoreCase("Recrystallized"))
                odswiez_zrekrystalizowane();
            else if(boxwidoki.getValue().equalsIgnoreCase("Default")) odswiez_komorki();
         else if(boxwidoki.getValue().equalsIgnoreCase("Energy")){
            int hmax;
            int hmin = Integer.parseInt(hminText.getText());

            if( boxenergie.getValue().equalsIgnoreCase("Homogeneous")) hmax=hmin;
            else hmax=Integer.parseInt(hmaxText.getText());

            if(!henergy) {
                henergy=true;
                ca.rekalkulujenergie(hmin, hmax);
            }
            for (int i = 0; i < szerokosc; i++) {
                for (int j = 0; j < wysokosc; j++) {
                    int h = ca.poprzednie[i + 1][j + 1].h;
                    Color c;

                    if (h == hmin) c = Color.DARKBLUE;
                    else if (h == 0) c = Color.RED;                                 //kolory energii
                    else if(h==hmax) c = Color.TURQUOISE;
                    else {
                        c=Color.ORANGE;
                    }
                    graphicsContext.setFill(c);
                    graphicsContext.fillRect(i, j, 1, 1);
                }
            }
        }
    }

    public void Startrekrystalizacja() {
        int hmin = Integer.parseInt(hminText.getText());
        int hmax;
        if( boxenergie.getValue().equalsIgnoreCase("Homogeneous")) hmax=hmin;
        else hmax=Integer.parseInt(hmaxText.getText());
        if(!henergy) {
            henergy=true;
            ca.rekalkulujenergie(hmin, hmax);
        }

        String typnukleacji = boxtypnukleacji.getValue();
        int iloscnukleonow = Integer.parseInt(ilosc_rekrystalizacja.getText());
        int iteracje = Integer.parseInt(recrystallIterationsText.getText());
        String nucleationLocation = boxnukleacje.getValue();

        for(int i=0;i<iteracje;i++) {

            ca.rekrystalizuj(i, iloscnukleonow, typnukleacji, nucleationLocation, iteracje);
        }

        if (boxwidoki.getValue().equalsIgnoreCase("Recrystallized"))
            odswiez_zrekrystalizowane();
        else
            odswiez_komorki();
    }
}