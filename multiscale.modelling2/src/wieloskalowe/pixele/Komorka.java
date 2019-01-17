package wieloskalowe.pixele;

import javafx.scene.paint.Color;

public class Komorka {
    public Color stan;
    public int faza;
    public boolean poMC;
    public int h;
    public boolean zrekrystalizowana;


    public Komorka() {
        this.stan = Color.WHITE;
        this.faza = 0;
        this.poMC = false;
        h = 5;
        zrekrystalizowana = false;
    }
}