package wieloskalowe;

import javafx.scene.paint.Color;


import java.util.*;

public class CA {
    public int szerokosc, wysokosc, moore4zasada;
    public boolean moore;
    public Komorka[][] komorki, poprzednie;
    public static int inkluzje=0;
    public void initBoard() {
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
        moore4zasada=50;
        this.szerokosc=szerokosc+2;
        this.wysokosc=wysokosc+2;
        initBoard();
    }

    public boolean Check(Komorka poprzednie[][],int x,int y, int a,int b)
    {
        if(!poprzednie[x+a][y+b].stan.equals(Color.WHITE) && !poprzednie[x+a][y+b].stan.equals(Color.BLACK) && poprzednie[x+a][y+b].faza==0 )
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


    public void moore(int x, int y){
        if(poprzednie[x][y].stan.equals(Color.WHITE)){
            boolean done=false;

            //rule 1
            List<Color> kolory=new ArrayList<>();

            if(Check(poprzednie,x,y,1,0))   kolory.add(poprzednie[x+1][y].stan);
            if(Check(poprzednie,x,y,-1,0))  kolory.add(poprzednie[x-1][y].stan);
            if(Check(poprzednie,x,y,0,1))   kolory.add(poprzednie[x][y+1].stan);
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
                if(Collections.max(hashmapa.entrySet(), (entry1, entry2) -> entry1.getValue() - entry2.getValue()).getValue()>=5){
                    done=true;
                    komorki[x][y].stan=(Collections.max(hashmapa.entrySet(), (entry1, entry2) -> entry1.getValue() - entry2.getValue()).getKey());
                }
            }
            //rule 2
            if(!done){
                kolory.clear();
                if(Check(poprzednie,x,y,1,0))   kolory.add(poprzednie[x+1][y].stan);
                if(Check(poprzednie,x,y,-1,0))  kolory.add(poprzednie[x-1][y].stan);
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
            //rule 3
            if(!done){
                kolory.clear();
                if(Check(poprzednie,x,y,1,1))  kolory.add(poprzednie[x+1][y+1].stan);
                if(Check(poprzednie,x,y,1,-1))  kolory.add(poprzednie[x+1][y-1].stan);
                if(Check(poprzednie,x,y,-1,-1))  kolory.add(poprzednie[x-1][y-1].stan);
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

            //rule 4

            if(!done){
                kolory.clear();
                Random random=new Random();
                if(random.nextInt(100)+1<moore4zasada){

                    if(Check(poprzednie,x,y,1,0))   kolory.add(poprzednie[x+1][y].stan);
                    if(Check(poprzednie,x,y,-1,0))  kolory.add(poprzednie[x-1][y].stan);
                    if(Check(poprzednie,x,y,0,1))   kolory.add(poprzednie[x][y+1].stan);
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
        List<Color> colors=new ArrayList<>();

        while(nowe<liczbaziaren) {
            int x=random.nextInt((szerokosc-1));
            int y=random.nextInt((wysokosc-1));
            Color newState= Color.rgb(random.nextInt(256),random.nextInt(256),random.nextInt(256));
            if(!colors.contains(newState) && poprzednie[x][y].stan.equals(Color.WHITE)){
                komorki[x][y].stan=(newState);
                colors.add(newState);
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
                if(moore) moore(i,j);
                else Neumann(i,j);
            }
        }

        for(int i=0;i<szerokosc;i++){
            for(int j=0;j<wysokosc;j++){
                poprzednie[i][j].stan=(komorki[i][j].stan);
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
                        inBoundary=inclusiononBound(x,y,color);
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
                        inBoundary=inclusiononBound(x,y,color);

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

    public void Wyczysc() {
        boolean boundary;
        for (int i = 1; i < szerokosc - 1; i++) {
            for (int j = 1; j < wysokosc - 1; j++) {
                boundary = false;
                boundary = isBoundary(boundary, i, j);
                if (boundary)
                    komorki[i][j].faza = (10);
            }
        }
        for(int i=0;i<szerokosc;i++){
            for(int j=0;j<wysokosc;j++){
                poprzednie[i][j].stan=(komorki[i][j].stan);
                poprzednie[i][j].faza=(komorki[i][j].faza);
            }
        }

        for(int i=0;i<szerokosc;i++){
            for(int j=0;j<wysokosc;j++){
                poprzednie[i][j].stan=(komorki[i][j].stan);
                poprzednie[i][j].faza=(komorki[i][j].faza);
            }
        }
    }
    public void ColorRemover() {
        for(int i=0;i<szerokosc;i++){
            for(int j=0;j<wysokosc;j++){
                if(poprzednie[i][j].faza!=50) {
                    komorki[i][j].stan=(Color.WHITE);
                    komorki[i][j].faza=(0);
                }
                else
                    komorki[i][j].stan=(Color.BLACK);
            }
        }
    }

    public boolean isBoundary(boolean boundary, int i, int j) {
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
    public boolean inclusiononBound(int x, int y,Color color)
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
}
