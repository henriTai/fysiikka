package Main;

public class Kolmio {
    
    private double[][] koordinaatit;
    public boolean oikealta;
    int ruudunLeveys;
    int ruudunKorkeus;
    
    public Kolmio (double[]kulmat){
        this.koordinaatit = new double[4][2];
        this.koordinaatit[0][0]=kulmat[0];
        this.koordinaatit[0][1]=kulmat[1];
        this.koordinaatit[1][0]=kulmat[2];
        this.koordinaatit[1][1]=kulmat[3];
        this.koordinaatit[2][0]=kulmat[4];
        this.koordinaatit[2][1]=kulmat[5];
        asetaMassaKeskipiste();
        
        oikealta =false;
        
        this.ruudunLeveys=ruudunMitat()[0];
        this.ruudunKorkeus=ruudunMitat()[1];
        
    }
    public Kolmio (double[]kulmat, boolean oikealta){
        this.koordinaatit = new double[4][2];
        this.koordinaatit[0][0]=kulmat[0];
        this.koordinaatit[0][1]=kulmat[1];
        this.koordinaatit[1][0]=kulmat[2];
        this.koordinaatit[1][1]=kulmat[3];
        this.koordinaatit[2][0]=kulmat[4];
        this.koordinaatit[2][1]=kulmat[5];
        asetaMassaKeskipiste();
        this.oikealta=oikealta;
        
        this.ruudunLeveys=ruudunMitat()[0];
        this.ruudunKorkeus=ruudunMitat()[1];
        if (oikealta==true){
            siirraOikealle();
        }
        
    }
    
    private int[]ruudunMitat(){
        int[]r = {1300, 700};//muuta ui:ssä myös!
        return r;
    }
    
    private void siirraOikealle(){
        int kulma=0;
        double kulmanArvo = this.koordinaatit[0][0];
        if (this.koordinaatit[1][0]>kulmanArvo){
            kulma = 1;
        }
        if (this.koordinaatit[2][0]>kulmanArvo){
            kulma=2;
        }
        double erotus = (double) this.ruudunLeveys-this.koordinaatit[kulma][0];
        for (int i=0; i<4; i++){
            this.koordinaatit[i][0] += erotus;
        }
        
    }
    public int[] getRuudunMitat(){
        int[] r = {this.ruudunLeveys, this.ruudunKorkeus};
        return r;
    }
    
    public boolean getSuunta(){
        return this.oikealta;
    }
    
    private void asetaMassaKeskipiste(){
        this.koordinaatit[3][0]=laskeMassaKeskipiste()[0];
        this.koordinaatit[3][1]= laskeMassaKeskipiste()[1];
    }
    
    public double[][] getKoordinaatit(){
        return this.koordinaatit;
    }
    
    private double[] laskeMassaKeskipiste(){
        double[] kp = new double[2];
        double jananPituusX = (this.koordinaatit[1][0]+this.koordinaatit[2][0])/2.0
                -this.koordinaatit[0][0];
        double jananPituusY = (this.koordinaatit[1][1]+this.koordinaatit[2][1])/2.0
                -this.koordinaatit[0][1];
        kp[0]=this.koordinaatit[0][0]+jananPituusX*2.0/3.0;
        kp[1]=this.koordinaatit[0][1]+jananPituusY*2.0/3.0;
        return kp;

    }
    
    public void siirraKaikkiaPisteita(double x, double y){
        for (int i=0;i<4;i++){
            this.koordinaatit[i][0] += x;
            this.koordinaatit[i][1] += y;
        }
    }
    
}
