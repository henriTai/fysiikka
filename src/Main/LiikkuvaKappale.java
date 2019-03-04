
package Main;

import java.util.ArrayList;
import java.lang.Math;


public class LiikkuvaKappale {
    
   private ArrayList<Kolmio> vaiheet;
   private double[] nopeus;
   private double kierto; // astetta sekunnissa
   private int[] ruudunMitat;
   public boolean suunta;
   private double massa;
   private double inertia;
   
   public LiikkuvaKappale(Kolmio alkutilanne, double m, double kk, double nopeus, double kulma){
       this.vaiheet = new ArrayList<>();
       vaiheet.add(alkutilanne);
       this.nopeus =new double[2];
       asetaNopeus(nopeus, kulma, alkutilanne.oikealta);
       this.kierto=kk;
       this.ruudunMitat=alkutilanne.getRuudunMitat();
       this.suunta=alkutilanne.getSuunta();
       this.massa=m;
       this.inertia=0.4;
       
   }
   
   public Kolmio edellinenTilanne(){
       return this.vaiheet.get(this.vaiheet.size()-1);
   }
   
   private void asetaNopeus(double nopeus, double kulma, boolean oikealta){
       this.nopeus[0]=nopeus*Math.cos(kulma/360.0*2.0*Math.PI);
       this.nopeus[1]=nopeus*Math.sin(kulma/360.0*2.0*Math.PI);
       if (oikealta==true){
           this.nopeus[0]= -this.nopeus[0];
       }
   }
   
   public double[] getNopeus(){
       return this.nopeus;
   }
   
   public double getKiertoAsteina(){
       return this.kierto;
   }
   
   public int[] getRuudunMitat(){
       return this.ruudunMitat;
   }
   
   public Kolmio testiliiku(double aika){
       //ensin siirtymä
       Kolmio seuraava = uudetKoordinaatit(this.edellinenTilanne(), aika);
       //sitten kiertoliike
       seuraava = kiertoLiike(seuraava, aika);
       return seuraava;
   }
   
   public void liiku(double aika){
       Kolmio edellinen = this.vaiheet.get(this.vaiheet.size()-1);
       Kolmio seuraava = uudetKoordinaatit(edellinen, aika);
       seuraava = kiertoLiike(seuraava, aika);
       double[] uusi = uusiNopeus(aika);
       this.vaiheet.add(seuraava);
       this.nopeus=uusi;
   }

    private double[] uusiNopeus(double aika) {
        double[] n = new double[2];
        n[0]=this.nopeus[0];
        n[1]= this.nopeus[1]-9.81*aika;
        return n;
    }

    private Kolmio uudetKoordinaatit(Kolmio edellinen, double aika) {
        double aX = edellinen.getKoordinaatit()[0][0]+this.nopeus[0]*aika;
        double aY = edellinen.getKoordinaatit()[0][1]+this.nopeus[1]*aika;
        double bX = edellinen.getKoordinaatit()[1][0]+this.nopeus[0]*aika;
        double bY = edellinen.getKoordinaatit()[1][1]+this.nopeus[1]*aika;
        double cX = edellinen.getKoordinaatit()[2][0]+this.nopeus[0]*aika;
        double cY = edellinen.getKoordinaatit()[2][1]+this.nopeus[1]*aika;
        double[] kulmat ={aX,aY,bX,bY,cX,cY};
        Kolmio uusi = new Kolmio(kulmat);
        
        return uusi;
    }
    
    private Kolmio kiertoLiike(Kolmio k, double aika){
        //ajan aikana tapahtunut kierto keskipisteen ympäri RADIAANEIKSI muunnettuna
        double kierretty = this.kierto*aika*Math.PI/180;
        //vaihe1. siirretään kärkipisteitä massakeskipisteen
        //etäisyyden origoon verran
        double aX = k.getKoordinaatit()[0][0]-k.getKoordinaatit()[3][0];
        double aY = k.getKoordinaatit()[0][1]-k.getKoordinaatit()[3][1];
        
        double bX = k.getKoordinaatit()[1][0]-k.getKoordinaatit()[3][0];
        double bY = k.getKoordinaatit()[1][1]-k.getKoordinaatit()[3][1];
        
        double cX = k.getKoordinaatit()[2][0]-k.getKoordinaatit()[3][0];
        double cY = k.getKoordinaatit()[2][1]-k.getKoordinaatit()[3][1];
        
        //janan origo-cm komponentit
        double kX = k.getKoordinaatit()[3][0];
        double kY = k.getKoordinaatit()[3][1];
        
        //vaihe 2 & 3: kierretään kukin piste ja suoritetaan takaisin siirto heti perään
        
        double aX2= aX*Math.cos(kierretty)-
                aY*Math.sin(kierretty)+kX;
        double aY2= aX*Math.sin(kierretty)+
                aY*Math.cos(kierretty)+kY;
        
        double bX2= bX*Math.cos(kierretty)-
                bY*Math.sin(kierretty)+kX;
        double bY2= bX*Math.sin(kierretty)+
                bY*Math.cos(kierretty)+kY;
        
        double cX2= cX*Math.cos(kierretty)-
                cY*Math.sin(kierretty)+kX;
        double cY2= cX*Math.sin(kierretty)+
                cY*Math.cos(kierretty)+kY;
        
        double[] pisteet = {aX2, aY2, bX2, bY2, cX2, cY2};
        
        Kolmio paluu = new Kolmio(pisteet);
        return paluu;
    }
    
    
    public ArrayList<Kolmio> getVaiheet(){
        return this.vaiheet;
    }
    
    public double getMassa(){
        return this.massa;
    }
    
    public double getInertia(){
        return this.inertia;
    }
    
    
    public void paivitaTormayksenJalkeen (double nopeusX, double nopeusY,double kierto){
        this.nopeus[0]=nopeusX;
        this.nopeus[1]=nopeusY;
        this.kierto=kierto;
    }
    
}
