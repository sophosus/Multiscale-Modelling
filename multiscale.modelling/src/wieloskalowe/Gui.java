package wieloskalowe;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.*;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;

import javax.imageio.ImageIO;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;

public class Gui implements Initializable {
    public int szerokosc,wysokosc,ziarna;
    CA ca;
    ObservableList list= FXCollections.observableArrayList();

    @FXML
    public ChoiceBox<String> input_typinkluzji;
    public TextField input_4zasada;
    public TextField input_ziarna;
    public TextField input_wys;
    public TextField input_szer;
    public TextField input_inkluzje;
    public TextField input_rozmiarinkluzji;
    public Canvas canvas;
    public CheckBox input_moore;
    public Pane pane;



    public GraphicsContext graphicsContext;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        graphicsContext = canvas.getGraphicsContext2D();
        loadData();
        input_typinkluzji.setValue("Disable");
        clear();
    }
    public boolean veryfyinput()
    {
        boolean good=false;
        szerokosc=Integer.parseInt(input_szer.getText());
        wysokosc=Integer.parseInt(input_wys.getText());
        ziarna=Integer.parseInt(input_ziarna.getText());
        if (szerokosc<300||wysokosc<300 ||ziarna <1||szerokosc>650||wysokosc>650) {
            System.out.println("verify input");
            return false;

        }

        return true;
    }
    public void loadData(){
        list.removeAll(list);
        list.addAll("Disable", "Square Random", "Square Boundaries", "Circle Random", "Circle Boundaries");
        input_typinkluzji.getItems().addAll(list);
    }
    public void nowyrys()
    {
        veryfyinput();
        canvas = new Canvas(szerokosc, wysokosc);
        canvas.setHeight(wysokosc);
        canvas.setWidth(szerokosc);

        ca = new CA(szerokosc, wysokosc);
        ca.ziarnowanie(ziarna);

        ca.moore4zasada = Integer.parseInt(input_4zasada.getText());
        ca.moore = input_moore.isSelected();
        for (int i = 0; i < szerokosc; i++) {
            for (int j = 0; j < wysokosc; j++) {
                graphicsContext.setFill(ca.komorki[i + 1][j + 1].stan);
                graphicsContext.fillRect(i, j, 1, 1);
            }
        }
    }
    public void clear()
    {
        graphicsContext.clearRect(0, 0, 650, 650);
        nowyrys();

    }
    public void Resetbutton(ActionEvent e){
        clear();
    }



    public void draw()
    {

        if (veryfyinput()) {
            ziarna=Integer.parseInt(input_ziarna.getText());
            if (input_moore.isSelected()) {
                ca.moore = true;
                ca.moore4zasada = Integer.parseInt(input_4zasada.getText());
            } else {
                ca.moore = false;
            }

            for (int i = 0; i < szerokosc; i++) {
                for (int j = 0; j < wysokosc; j++) {
                    graphicsContext.setFill(ca.komorki[i + 1][j + 1].stan);
                    graphicsContext.fillRect(i, j, 1, 1);
                }
            }
            while (!ca.ZapelnieniePlanszy()) {
                ca.NastepnyKrok();
            }
            for (int i = 0; i < szerokosc; i++) {
                for (int j = 0; j < wysokosc; j++) {
                    graphicsContext.setFill(ca.komorki[i + 1][j + 1].stan);
                    graphicsContext.fillRect(i, j, 1, 1);
                }
            }
        }
    }

    public void Startbutton(ActionEvent e) {
        draw();
    }

    public void redrawCells(){
        for (int i = 0; i < szerokosc; i++) {
            for (int j = 0; j < wysokosc; j++) {
                graphicsContext.setFill(ca.poprzednie[i + 1][j + 1].stan);
                graphicsContext.fillRect(i, j, 1, 1);
            }
        }
    }

    @FXML
    public void Inclusionbutton(){
        if(Integer.parseInt(input_szer.getText())!=szerokosc ||Integer.parseInt(input_wys.getText())!=wysokosc)
        {
            System.out.println("verify sizes"); }
        else ca.Dodajinkluzje(input_typinkluzji.getValue(), Integer.parseInt(input_inkluzje.getText()), Integer.parseInt(input_rozmiarinkluzji.getText()));
            redrawCells();

    }

    @FXML
    public void onEntireClearClick(){
        ca.Wyczysc();
        redrawCells();
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
    @FXML
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
                    //if(ca.komorki[i][j].faza!=0) System.out.println("uwaga");
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
    public void TxtImport() throws Exception {
        clear();
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

        redrawCells();
    }
    @FXML
    public void BmpImport() throws Exception {
        clear();
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
        redrawCells();
    }}