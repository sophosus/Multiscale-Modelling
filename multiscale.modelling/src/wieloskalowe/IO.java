/*import javafx.fxml.FXML;

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
        }*/