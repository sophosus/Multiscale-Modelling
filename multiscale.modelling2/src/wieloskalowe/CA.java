package wieloskalowe;

import javafx.scene.paint.Color;
import wieloskalowe.pixele.Komorka;
import wieloskalowe.pixele.Pkt;

import java.util.*;

public class CA {
    public int szerokosc;
    public int wysokosc;
    public boolean moore;
    public int prawdopodobienstwo_moore;
    public Komorka[][] komorki, poprzednie;
    public static int inkluzje;
    List<Color> MCkolor;

    public void Initializuj() {
        komorki=new Komorka[szerokosc][wysokosc];
        poprzednie=new Komorka[szerokosc][wysokosc];
        for(int i=0;i<szerokosc;i++){
            for(int j=0;j<wysokosc;j++){
                komorki[i][j]=new Komorka();
                poprzednie[i][j]=new Komorka();
            }
        }
    }

    public CA(int szerokosc, int wysokosc){
        inkluzje=0;
        moore=false;
        prawdopodobienstwo_moore=90;
        this.szerokosc=szerokosc+2;
        this.wysokosc=wysokosc+2;
        MCkolor=new ArrayList<>();
        Initializuj();
    }

    public boolean Check(Komorka poprzednie[][],int x,int y, int a,int b)
    {
        if(!poprzednie[x+a][y+b].stan.equals(Color.WHITE) && !poprzednie[x+a][y+b].stan.equals(Color.BLACK) && poprzednie[x+a][y+b].faza!=5 )
            return true;
        else return false;
    }
    public void Neumann(int x, int y){
        if(poprzednie[x][y].stan.equals(Color.WHITE))
        {
            List<Color> kolory=new ArrayList<>();
            if(Check(poprzednie,x,y,1,0))   kolory.add(poprzednie[x+1][y].stan);
            if(Check(poprzednie,x,y,-1,0))  kolory.add(poprzednie[x-1][y].stan);
            if(Check(poprzednie,x,y,0,1))   kolory.add(poprzednie[x][y+1].stan);
            if(Check(poprzednie,x,y,0,-1))  kolory.add(poprzednie[x][y-1].stan);
            if(!kolory.isEmpty()){
                Map<Color, Integer> hashmapa=new HashMap<>();
                for(Color color: kolory){
                    if(!hashmapa.containsKey(color))
                        hashmapa.put(color,1);
                    else
                        hashmapa.put(color, hashmapa.get(color)+1);
                }
                komorki[x][y].stan=(Collections.max(hashmapa.entrySet(), (entry1, entry2) -> entry1.getValue() - entry2.getValue()).getKey());
            }
        }
    }
    public void Moore(int x, int y){
        if(poprzednie[x][y].stan.equals(Color.WHITE)){
            boolean done=false;
            List<Color> kolory=new ArrayList<>();
            if(Check(poprzednie,x,y,1,0))   kolory.add(poprzednie[x+1][y].stan);
            if(Check(poprzednie,x,y,-1,0))  kolory.add(poprzednie[x-1][y].stan);
            if(Check(poprzednie,x,y,0,1))   kolory.add(poprzednie[x][y+1].stan);
            if(Check(poprzednie,x,y,0,-1))  kolory.add(poprzednie[x][y-1].stan);            //1
            if(Check(poprzednie,x,y,1,1))  kolory.add(poprzednie[x+1][y+1].stan);
            if(Check(poprzednie,x,y,1,-1))  kolory.add(poprzednie[x+1][y-1].stan);
            if(Check(poprzednie,x,y,-1,-1))  kolory.add(poprzednie[x-1][y-1].stan);
            if(Check(poprzednie,x,y,-1,1))  kolory.add(poprzednie[x-1][y+1].stan);

            if(!kolory.isEmpty()){
                Map<Color, Integer> hashmapa=new HashMap<>();
                for(Color color: kolory){
                    if(!hashmapa.containsKey(color))
                        hashmapa.put(color,1);
                    else
                        hashmapa.put(color, hashmapa.get(color)+1);
                }
                if(Collections.max(hashmapa.entrySet(), (entry1, entry2) -> entry1.getValue() - entry2.getValue()).getValue()>=5){
                    done=true;
                    komorki[x][y].stan=(Collections.max(hashmapa.entrySet(), (entry1, entry2) -> entry1.getValue() - entry2.getValue()).getKey());
                }
            }
            if(!done){
                kolory.clear();
                if(Check(poprzednie,x,y,1,0))   kolory.add(poprzednie[x+1][y].stan);
                if(Check(poprzednie,x,y,-1,0))  kolory.add(poprzednie[x-1][y].stan);            //2
                if(Check(poprzednie,x,y,0,1))   kolory.add(poprzednie[x][y+1].stan);
                if(Check(poprzednie,x,y,0,-1))  kolory.add(poprzednie[x][y-1].stan);
                Map<Color, Integer> hashmapa=new HashMap<>();
                for(Color color: kolory){
                    if(!hashmapa.containsKey(color))
                        hashmapa.put(color,1);
                    else
                        hashmapa.put(color, hashmapa.get(color)+1);
                }
                if(!hashmapa.isEmpty() && Collections.max(hashmapa.entrySet(), (entry1, entry2) -> entry1.getValue() - entry2.getValue()).getValue()>=3){
                    done=true;
                    komorki[x][y].stan=(Collections.max(hashmapa.entrySet(), (entry1, entry2) -> entry1.getValue() - entry2.getValue()).getKey());
                }
            }
            if(!done){
                kolory.clear();
                if(Check(poprzednie,x,y,1,1))  kolory.add(poprzednie[x+1][y+1].stan);
                if(Check(poprzednie,x,y,1,-1))  kolory.add(poprzednie[x+1][y-1].stan);
                if(Check(poprzednie,x,y,-1,-1))  kolory.add(poprzednie[x-1][y-1].stan);         //3
                if(Check(poprzednie,x,y,-1,1))  kolory.add(poprzednie[x-1][y+1].stan);
                Map<Color, Integer> hashmapa=new HashMap<>();
                for(Color color: kolory){
                    if(!hashmapa.containsKey(color))
                        hashmapa.put(color,1);
                    else
                        hashmapa.put(color, hashmapa.get(color)+1);
                }
                if(!hashmapa.isEmpty() && Collections.max(hashmapa.entrySet(), (entry1, entry2) -> entry1.getValue() - entry2.getValue()).getValue()>=3){
                    done=true;
                    komorki[x][y].stan=(Collections.max(hashmapa.entrySet(), (entry1, entry2) -> entry1.getValue() - entry2.getValue()).getKey());
                }
            }
            if(!done){
                kolory.clear();
                Random random=new Random();
                if(random.nextInt(100)+1<prawdopodobienstwo_moore){
                    if(Check(poprzednie,x,y,1,0))   kolory.add(poprzednie[x+1][y].stan);
                    if(Check(poprzednie,x,y,-1,0))  kolory.add(poprzednie[x-1][y].stan);
                    if(Check(poprzednie,x,y,0,1))   kolory.add(poprzednie[x][y+1].stan);                //4
                    if(Check(poprzednie,x,y,0,-1))  kolory.add(poprzednie[x][y-1].stan);
                    if(Check(poprzednie,x,y,1,1))  kolory.add(poprzednie[x+1][y+1].stan);
                    if(Check(poprzednie,x,y,1,-1))  kolory.add(poprzednie[x+1][y-1].stan);
                    if(Check(poprzednie,x,y,-1,-1))  kolory.add(poprzednie[x-1][y-1].stan);
                    if(Check(poprzednie,x,y,-1,1))  kolory.add(poprzednie[x-1][y+1].stan);

                    if(!kolory.isEmpty()){
                        Map<Color, Integer> hashmapa=new HashMap<>();
                        for(Color color: kolory){
                            if(!hashmapa.containsKey(color))
                                hashmapa.put(color,1);
                            else
                                hashmapa.put(color, hashmapa.get(color)+1);
                        }
                        komorki[x][y].stan=(Collections.max(hashmapa.entrySet(), (entry1, entry2) -> entry1.getValue() - entry2.getValue()).getKey());
                        done=true;
                    }
                }
            }
        }
    }

    public void ziarnowanie(int liczbaziaren){
        Random random = new Random();
        int nowe=0;
        List<Color> kolory=new ArrayList<>();

        while(nowe<liczbaziaren) {
            int x=random.nextInt((szerokosc-1))+1;
            int y=random.nextInt((wysokosc-1))+1;
            Color nowykolor= Color.rgb(random.nextInt(256),random.nextInt(256),random.nextInt(256));
            if(!kolory.contains(nowykolor) && poprzednie[x][y].stan.equals(Color.WHITE)){
                komorki[x][y].stan=(nowykolor);
                kolory.add(nowykolor);
                nowe++;
            }
        }
        for(int i=0;i<szerokosc;i++){
            for(int j=0;j<wysokosc;j++){
                poprzednie[i][j].stan=(komorki[i][j].stan);
            }
        }
    }

    public boolean ZapelnieniePlanszy(){
        boolean status=true;
        for(int i=1;i<szerokosc-1;i++){
            for(int j=1;j<wysokosc-1;j++){
                if(komorki[i][j].stan.equals(Color.WHITE)) {
                    status = false;
                }
                if(!status) break;
            }
            if(!status) break;
        }
        return status;
    }

    public void NastepnyKrok(){
        for(int i=1;i<szerokosc-1;i++){
            for(int j=1;j<wysokosc-1;j++){
                if (moore) Moore(i, j);
                else Neumann(i, j);
            }
        }
        for(int i=0;i<szerokosc;i++){
            for(int j=0;j<wysokosc;j++){
                poprzednie[i][j].stan=komorki[i][j].stan;
            }
        }
    }

    public void Dodajinkluzje(String type, int number, double size) {
        Random random=new Random();
        int currentNumber=0;
        boolean checker=false;
        boolean inBoundary=false;

        if(type.contains("Circle")) {
            int r = (int) (size / 2.0);
            while (currentNumber < number) {
                checker = false;
                inBoundary=false;
                int x = random.nextInt((szerokosc - r * 2-1)) + r;
                int y = random.nextInt((wysokosc - r * 2-1)) +r;
                if (!komorki[x][y].stan.equals(Color.BLACK)) {
                    if(type.contains("Random")) {
                        if (komorki[x][y+r].stan.equals(Color.BLACK) || komorki[x][y-r].stan.equals(Color.BLACK) || komorki[x-r][y].stan.equals(Color.BLACK) || komorki[x+r][y].stan.equals(Color.BLACK) || komorki[x+r][y+r].stan.equals(Color.BLACK) || komorki[x+r][y-r].stan.equals(Color.BLACK) || komorki[x-r][y + r].stan.equals(Color.BLACK) || komorki[x-r][y - r].stan.equals(Color.BLACK))
                            checker = true;
                        currentNumber = getCurrentNumber(currentNumber, checker, r, x, y);
                    }
                    else{
                        Color color=komorki[x][y].stan;
                        inBoundary=inkluzja_granica(x,y,color);
                        if (inBoundary) {
                            if (komorki[x][y+r].stan.equals(Color.BLACK) || komorki[x][y-r].stan.equals(Color.BLACK) || komorki[x-r][y].stan.equals(Color.BLACK) || komorki[x+r][y].stan.equals(Color.BLACK) || komorki[x+r][y+r].stan.equals(Color.BLACK) || komorki[x+r][y-r].stan.equals(Color.BLACK) || komorki[x-r][y + r].stan.equals(Color.BLACK) || komorki[x-r][y - r].stan.equals(Color.BLACK))
                                checker = true;
                            currentNumber = getCurrentNumber(currentNumber, checker, r, x, y);
                        }
                    }
                }
            }
        }
        if(type.contains("Square")) {
            int a = (int) size;
            while (currentNumber < number) {
                checker = false;
                inBoundary=false;
                int x = random.nextInt((szerokosc - a * 2-1)) + 1;
                int y = random.nextInt((wysokosc - a * 2-1)) + 1;
                if (!komorki[x][y].stan.equals(Color.BLACK)) {
                    if(type.contains("Random")) {
                        if (komorki[x + a+1][y].stan.equals(Color.BLACK) || komorki[x +1+a][y + 1+a].stan.equals(Color.BLACK) || komorki[x+1][y + 1+a].stan.equals(Color.BLACK))
                            checker = true;
                        if (!checker) {
                            for(int i=x;i<=x+a;i++) {
                                for (int j = y; j <= y +  a; j++) {
                                    komorki[i][j].stan=(Color.BLACK);
                                }
                            }
                            currentNumber++;
                            inkluzje++;
                        }
                    }
                    else{
                        Color color=komorki[x][y].stan;
                        inBoundary=inkluzja_granica(x,y,color);

                        if (inBoundary) {
                            if (komorki[x- a][y+a].stan.equals(Color.BLACK) || komorki[x + a][y - a].stan.equals(Color.BLACK) || komorki[x+a][y + a].stan.equals(Color.BLACK) || komorki[x-a][y -a].stan.equals(Color.BLACK))
                                checker = true;
                            if (!checker) {
                                for(int i=x-a;i<=x+a;i++) {
                                    for (int j = y-a; j <= y + a; j++) {
                                        komorki[i][j].stan=(Color.BLACK);
                                    }
                                }
                                currentNumber++;
                                inkluzje++;
                            }
                        }
                    }
                }
            }
        }
        for(int i=0;i<szerokosc;i++){
            for(int j=0;j<wysokosc;j++){
                poprzednie[i][j].stan=(komorki[i][j].stan);
            }
        }
    }
    public boolean inkluzja_granica(int x, int y,Color color)
    {
        boolean inBoundary=false;
        for(int i=-1;i<=1;i++) {
            for (int j = -1; j <= 1; j++) {
                if(x+i>0 && x+i<szerokosc-1 && y+j>0 && y+j<wysokosc-1) {
                    if (komorki[x + i][y + j].stan != color){
                        inBoundary = true;
                        break;}
                }
            }
        }
        return inBoundary;
    }
    public int getCurrentNumber(int currentNumber, boolean checker, int r, int x, int y) {    //inclusions
        if (!checker)
        {
            for(int i=x-r;i<=x+r;i++) {
                for (int j = y-r; j <= y + r; j++) {
                    if (((i - x) * (i - x) + (j - y) * (j - y)) <= (r * r)){
                        if( i>0 && i<szerokosc-1 &&j >0 &&  j <wysokosc-1)
                            komorki[i][j].stan=(Color.BLACK);
                    }
                }
            }
            currentNumber++;
            inkluzje++;
        }
        return currentNumber;
    }

    public void usunziarna(List<Color> selectedGrains){

            for (int i = 1; i < szerokosc - 1; i++) {
                for (int j = 1; j < wysokosc - 1; j++) {
                    if (!selectedGrains.contains(poprzednie[i][j].stan)) {
                        if (!poprzednie[i][j].stan.equals(Color.BLACK)) {
                            komorki[i][j].stan=(Color.WHITE);
                        }
                    } else {
                        komorki[i][j].faza=5;
                        komorki[i][j].stan=(Color.LIGHTPINK);
                    }
                }
            }
            for (int i = 0; i < szerokosc; i++) {
                for (int j = 0; j < wysokosc; j++) {
                    poprzednie[i][j].stan=(komorki[i][j].stan);
                    poprzednie[i][j].faza=(komorki[i][j].faza);
                //}
            }
        }

    }


public void generated_ColorChanger() {
    for(int i=0;i<szerokosc;i++){
        for(int j=0;j<wysokosc;j++){
            if(poprzednie[i][j].faza!=5) {
                komorki[i][j].stan=(Color.WHITE);
                komorki[i][j].faza=(0);

            }
            else
                komorki[i][j].stan=(Color.BLACK);
        }
    }
}

public void czyscgranice(){
    for(int i=0;i<szerokosc;i++) {
        for (int j = 0; j < wysokosc; j++) {
            if (i == 0 || j == 0 || i == (szerokosc - 1) || j == (wysokosc - 1)) {
                poprzednie[i][j].stan = (Color.WHITE);
                komorki[i][j].stan = (Color.WHITE);
                poprzednie[i][j].faza = (0);
                komorki[i][j].faza = (0);
            }
        }
    }

}
    public void czysc_kolory() {
        boolean boundary=false;
        for (int i = 1; i < szerokosc - 1; i++) {
            for (int j = 1; j < wysokosc - 1; j++) {
                boundary=false;
                boundary = isBoundary(boundary, i, j);              //do przycisku clear all
                if(boundary)
                    komorki[i][j].faza=5;
            }
        }
        for(int i=0;i<szerokosc;i++){
            for(int j=0;j<wysokosc;j++){
                poprzednie[i][j].stan=(komorki[i][j].stan);
                poprzednie[i][j].faza=(komorki[i][j].faza);
            }
        }
        generated_ColorChanger();
        for(int i=0;i<szerokosc;i++){
            for(int j=0;j<wysokosc;j++){
                poprzednie[i][j].stan=(komorki[i][j].stan);
                poprzednie[i][j].faza=(komorki[i][j].faza);
            }
        }
    }

    public boolean isBoundary(boolean boundary, int i, int j) {             //sprawdzanie czy punkt jest na granicy
        if(!poprzednie[i][j].stan.equals(poprzednie[i][j+1].stan))
            boundary=true;
        else if(!poprzednie[i][j].stan.equals(poprzednie[i][j-1].stan))
            boundary=true;
        else if(!poprzednie[i][j].stan.equals(poprzednie[i+1][j-1].stan))
            boundary=true;
        else if(!poprzednie[i][j].stan.equals(poprzednie[i+1][j].stan))
            boundary=true;
        else if(!poprzednie[i][j].stan.equals(poprzednie[i+1][j+1].stan))
            boundary=true;
        else if(!poprzednie[i][j].stan.equals(poprzednie[i-1][j-1].stan))
            boundary=true;
        else if(!poprzednie[i][j].stan.equals(poprzednie[i-1][j].stan))
            boundary=true;
        else if(!poprzednie[i][j].stan.equals(poprzednie[i-1][j+1].stan))
            boundary=true;
        return boundary;
    }

    public void MCinit(int numberOfStates){
        Random random=new Random();
        while(MCkolor.size()<numberOfStates) {
            Color nowykolor= Color.rgb(random.nextInt(256),random.nextInt(256),random.nextInt(256));
            if(!MCkolor.contains(nowykolor) && !nowykolor.equals(Color.WHITE) && !nowykolor.equals(Color.BLACK)){               //initialization of MC
                MCkolor.add(nowykolor);
            }
        }
        for(int i=0;i<szerokosc;i++){
            for(int j=0;j<wysokosc;j++){
                if(komorki[i][j].stan.equals(Color.WHITE)) {
                    komorki[i][j].stan=(MCkolor.get(random.nextInt(MCkolor.size())));           //wypelnianie komorek kolorami
                    komorki[i][j].faza=(15);
                }
            }
        }
        for(int i=0;i<szerokosc;i++){
            for(int j=0;j<wysokosc;j++){
                poprzednie[i][j].stan=(komorki[i][j].stan);         //do obliczeniowej
                poprzednie[i][j].faza=(komorki[i][j].faza);
            }
        }
    }
    public void MCnastepny() {
        Random random = new Random();
        List<Pkt> listapunktow = new ArrayList<>();
        for (int i = 1; i < szerokosc - 1; i++) {               //lista punktow ktore moga byc zmienione
            for (int j = 1; j < wysokosc - 1; j++) {
                if(!poprzednie[i][j].stan.equals(Color.LIGHTPINK) && !poprzednie[i][j].stan.equals(Color.BLACK) && !komorki[i][j].stan.equals(Color.LIGHTPINK) && !poprzednie[i][j].stan.equals(Color.BLACK))
                    listapunktow.add(new Pkt(i, j));
            }
        }
        Collections.shuffle(listapunktow);
        for (int i = 0; i < listapunktow.size(); i++) {
            boolean bingo=false;
            Pkt pkt = listapunktow.get(i);
            int x=0, y=0;
            while (!bingo) {
                x = random.nextInt(3) - 1;
                y = random.nextInt(3) - 1;
                if (x != 0 && y != 0 && !poprzednie[pkt.x + x][pkt.y + y].stan.equals(Color.LIGHTPINK))         //random wybor sasiedniej
                    bingo=true;
            }
            int staraenergia = obliczenergie(pkt.x, pkt.y);
            Color starystan = poprzednie[pkt.x][pkt.y].stan;
            poprzednie[pkt.x][pkt.y].stan=(poprzednie[pkt.x + x][pkt.y + y].stan);                          //nowy stan od sasiada do temp

            int nowaenergia = obliczenergie(pkt.x, pkt.y);
            if (nowaenergia > staraenergia) {
                poprzednie[pkt.x][pkt.y].stan=(starystan);                                   //jesli energia wyzsza to powrtot do starej
            }

        }
    }
    public int obliczenergie(int x, int y){
        int energy=0;
        for(int i=-1;i<2;i++){
            for(int j=-1;j<2;j++){
                if(i==0 && j==0) continue;
                if(!poprzednie[x+i][y+j].stan.equals(poprzednie[x][y].stan))                //sumowanie energii
                    energy++;
            }
        }
        return energy;
    }
    public boolean nagranicy(int x,int y){                                              //sprawdza czy komorka jest na granicy
        boolean test=false;
        Color color=poprzednie[x][y].stan;
        for(int i=x-1;i<=x+1;i++){
            for(int j=y-1;j<=y+1;j++){
                if(i!=0 && j!=0 && i!=szerokosc-1 && j!=wysokosc-1) {
                    if (!poprzednie[i][j].stan.equals(color)) {
                        test = true;
                        break;
                    }
                }
            }
        }
        return test;
    }
    public void rekalkulujenergie(int min, int max){                                //ustawianie energii po obliczeniach
        for(int i=1;i<szerokosc-1;i++) {
            for (int j = 1; j < wysokosc - 1; j++) {
                if(poprzednie[i][j].zrekrystalizowana) poprzednie[i][j].h=0;
                else if(nagranicy(i,j)) poprzednie[i][j].h=(max);
                else poprzednie[i][j].h=(min);
            }
        }
    }
    public void rekrystalizowane_ziarnowanie(List<Pkt> niezrekrystalizowane, String typnukleacji, int number){
        if(typnukleacji.equalsIgnoreCase("Random")){
            for(int i=0;i<number;i++){
                Pkt pkt =niezrekrystalizowane.get(i);                   //tworzenie nowych ziaren randomowo
                poprzednie[pkt.x][pkt.y].zrekrystalizowana=(true);
                poprzednie[pkt.x][pkt.y].h=0;
            }
        }
        if(typnukleacji.equalsIgnoreCase("Grain Boundary")){
            int j=0;
            for(int i=0;i<niezrekrystalizowane.size();i++){
                Pkt pkt =niezrekrystalizowane.get(i);
                if(nagranicy(pkt.x, pkt.y)) {                       //tworzenie ziaren na granicach
                    poprzednie[pkt.x][pkt.y].zrekrystalizowana=(true);
                    poprzednie[pkt.x][pkt.y].h=0;
                    j++;
                }
                if(j>=number) break;
            }
        }
    }

    public void rekrystalizuj(int licznik, int iloscnukleonow, String nukleacja_tryb, String typnukleacji, int maxiteracje){
        List<Pkt> niezrekrystalizowanepunkty = new ArrayList<>();
        for (int i = 1; i < szerokosc - 1; i++) {
            for (int j = 1; j < wysokosc - 1; j++) {                                //tworzenie bazy punktow do rekrystalizacji
                if(!poprzednie[i][j].zrekrystalizowana)
                    niezrekrystalizowanepunkty.add(new Pkt(i, j));
            }
        }
        if(niezrekrystalizowanepunkty.size()>iloscnukleonow){
            Collections.shuffle(niezrekrystalizowanepunkty);
            if(nukleacja_tryb.equalsIgnoreCase("At the begining") && licznik==0){
                rekrystalizowane_ziarnowanie(niezrekrystalizowanepunkty,typnukleacji,iloscnukleonow);
            }
            else if(nukleacja_tryb.equalsIgnoreCase("Constant")){                      //warunki rekrystalizacji z gui
                int constantnukleony=(int)(iloscnukleonow/maxiteracje);
                rekrystalizowane_ziarnowanie(niezrekrystalizowanepunkty,typnukleacji,constantnukleony);
            }
            else{
                int constantnukleony=(int)(iloscnukleonow/maxiteracje);
                rekrystalizowane_ziarnowanie(niezrekrystalizowanepunkty,typnukleacji,(int)(constantnukleony*(licznik+1)));
//                }
            }
        }
        Collections.shuffle(niezrekrystalizowanepunkty);                //mieszanie kolejnosci listy
        for(int i=0;i<niezrekrystalizowanepunkty.size();i++){
            Pkt pkt =niezrekrystalizowanepunkty.get(i);
            if(sasiadjestzrekrystalizowany(pkt)){
                int staraenergia=obliczenergie(pkt.x, pkt.y)+poprzednie[pkt.x][pkt.y].h;

                Color starykolor=poprzednie[pkt.x][pkt.y].stan;
                Color nowykolor=losowystanrekrystalizacja(pkt);

                poprzednie[pkt.x][pkt.y].stan=(nowykolor);
                int nowaenergia=obliczenergie(pkt.x, pkt.y);
                if(nowaenergia<=(staraenergia) ){                                        //zamiana
                    poprzednie[pkt.x][pkt.y].zrekrystalizowana=(true);
                    poprzednie[pkt.x][pkt.y].h=0;
                }
                else{
                    poprzednie[pkt.x][pkt.y].stan=(starykolor);
                }
            }
        }
    }
    public boolean sasiadjestzrekrystalizowany(Pkt pkt){
        boolean wynik=false;
        for(int i = pkt.x-1; i<= pkt.x+1; i++){
            for(int j = pkt.y-1; j<= pkt.y+1; j++){
                if(i!=0 && j!=0 && i!=szerokosc-1 && j!=wysokosc-1) {
                    if (poprzednie[i][j].zrekrystalizowana) {
                        wynik = true;
                        break;
                    }
                }
            }
        }
        return wynik;
    }
    public Color losowystanrekrystalizacja(Pkt pkt){            //dodawanie nowego koloru do zrekrystalizowanych
        List<Color> kolory=new ArrayList<>();
        for(int i = pkt.x-1; i<= pkt.x+1; i++) {
            for (int j = pkt.y - 1; j <= pkt.y + 1; j++) {
                if(!kolory.contains(poprzednie[i][j].stan) && poprzednie[i][j].zrekrystalizowana)
                    kolory.add(poprzednie[i][j].stan);
            }
        }
        if(kolory.size()>0){
            Collections.shuffle(kolory);
            return kolory.get(0);
        }
        else
            return poprzednie[pkt.x][pkt.y].stan;
    }

}