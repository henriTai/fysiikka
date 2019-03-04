
package Main;

import Piirtaja.Kayttoliittyma;
import javax.swing.SwingUtilities;
        
public class LentorataSimulaattori {
    
    LiikkuvaKappale kappale1;
    LiikkuvaKappale kappale2;
    double tarkkailuVali;
    int iteraatiot;
    int piirtoTiheys;
    int[] ruudunMitat;
    private boolean ollaanTormayksessa;
    int osuvaKolmio;
    int osuvaKulma;
    private double kimpoKerroin; //1=täysin kimmoisa törmäys

    
    public LentorataSimulaattori(LiikkuvaKappale kappale1, LiikkuvaKappale kappale2, 
            double tarkkailuVali, int it, int piirto, double ee){
        this.kappale1 = kappale1;
        this.kappale2 = kappale2;
        this.iteraatiot =it;
        this.piirtoTiheys=piirto;
        this.ruudunMitat=kappale1.getRuudunMitat();
        this.tarkkailuVali=tarkkailuVali;
        this.kimpoKerroin =ee;
        
        this.ollaanTormayksessa=false;
        this.osuvaKolmio=-1;
        this.osuvaKulma=-1;
    }
    
    
    public void simuloi(){
        //suoritetaan iteraatiot
        for (int i=0;i<this.iteraatiot;i++){
            //tehdään törmäystarkistus joka ei vielä varsinaisesti liikuta
            boolean tormays = tTarkistus();
            Osumapiste op = null;
            if (tormays){
                op=tOsumapiste();
            }
            
            //sitten liikutaan oikeasti
            kappale1.liiku(this.tarkkailuVali);
            kappale2.liiku(this.tarkkailuVali);
            
            if (!ollaanTormayksessa && tormays){
                ollaanTormayksessa =true;
                laskeTormayksenVaikutus(op);
            }
            if (ollaanTormayksessa && !tormays){
                //kun poistuttiin törmäyksestä
                ollaanTormayksessa=false;
                this.osuvaKolmio=-1;//palautetaan alkuun
                this.osuvaKulma=-1;
            }

        }
        Kayttoliittyma kayttoliittyma = new Kayttoliittyma(kappale1, kappale2, this.piirtoTiheys, this.ruudunMitat);
        SwingUtilities.invokeLater(kayttoliittyma);
    }
    
    private boolean tTarkistus(){ //tekee perustarkistuksen, varsinainen erikseen
        Kolmio k1 = kappale1.testiliiku(tarkkailuVali);
        Kolmio k2 = kappale2.testiliiku(tarkkailuVali);
        
        //seuraavaksi ensin peustarkistus
        //alustetaan lista mahdollisesti osuvista kulmista
        boolean[] tarkasteltavatKulmat1 = {false,false,false};
        boolean[] tarkasteltavatKulmat2 = {false,false,false};
        
        //jos eivät ole x-akselilla samalla alueella ei jatketa tarkastusta
        double x1Min=minimi(k1, 0);
        double x1Max = maksimi(k1,0);
        double x2Min = minimi(k2,0);
        double x2Max = maksimi(k2,0);
        if (x2Min > x1Max || x2Max < x1Min){
            return false;
            //jos eivät ole samalla alueella y-akselin suhteen, palataan
        } else {
            double y1Min = minimi(k1,1);
            double y1Max = maksimi(k1,1);
            double y2Min = minimi(k2,1);
            double y2Max = maksimi(k2,1);
            if (y2Min > y1Max || y2Max < y1Min){
                return false;
                //aletaan tarkastaa kulmia
            } else {
                //kolmion 1 kulman 1 mahdollisuus törmätä kolmioon 2, ts. on samalla alueella
                if (k1.getKoordinaatit()[0][0]>x2Min && k1.getKoordinaatit()[0][0]<x2Max &&
                        k1.getKoordinaatit()[0][1]>y2Min && k1.getKoordinaatit()[0][1]<y2Max){
                    tarkasteltavatKulmat1[0]=true;
                }
                //sama kolmio 1, kulma 2
                if (k1.getKoordinaatit()[1][0]>x2Min && k1.getKoordinaatit()[1][0]<x2Max &&
                        k1.getKoordinaatit()[1][1]>y2Min && k1.getKoordinaatit()[1][1]<y2Max){
                    tarkasteltavatKulmat1[1]=true;
                }
                //kolmio 1, kulma 3
                if (k1.getKoordinaatit()[2][0]>x2Min && k1.getKoordinaatit()[2][0]<x2Max &&
                        k1.getKoordinaatit()[2][1]>y2Min && k1.getKoordinaatit()[2][1]<y2Max){
                    tarkasteltavatKulmat1[2]=true;
                }
                //kolmio 2, kulma 1
                if (k2.getKoordinaatit()[0][0]>x1Min && k2.getKoordinaatit()[0][0]<x1Max &&
                        k2.getKoordinaatit()[0][1]>y1Min && k2.getKoordinaatit()[0][1]<y1Max){
                    tarkasteltavatKulmat2[0]=true;
                }
                //kolmio 2, kulma 2
                if (k2.getKoordinaatit()[1][0]>x1Min && k2.getKoordinaatit()[1][0]<x1Max &&
                        k2.getKoordinaatit()[1][1]>y1Min && k2.getKoordinaatit()[1][1]<y1Max){
                    tarkasteltavatKulmat2[1]=true;
                }
                //kolmio 2, kulma 3
                if (k2.getKoordinaatit()[2][0]>x1Min && k2.getKoordinaatit()[2][0]<x1Max &&
                        k2.getKoordinaatit()[2][1]>y1Min && k2.getKoordinaatit()[2][1]<y1Max){
                    tarkasteltavatKulmat2[2]=true;
                }
                
                //luodaan Osumapisteolio jonka funktiot tarkastelee tilanteen tarkemmin
                // ja funktio osumaTapahtunut palauttaa arvon onko törmäys todella tapahtunut
                Osumapiste op = new Osumapiste(k1,k2,tarkasteltavatKulmat1, tarkasteltavatKulmat2);
                if (op.osumaTapahtunut()){
                    this.osuvaKolmio=op.osuvaKolmio();
                    this.osuvaKulma=op.osuvaKulma();
                }
                return op.osumaTapahtunut();
            }
        }
    }
    
    private Osumapiste tOsumapiste(){ //tekee perustarkistuksen, varsinainen erikseen
        Kolmio k1 = kappale1.testiliiku(tarkkailuVali);
        Kolmio k2 = kappale2.testiliiku(tarkkailuVali);
        
        //seuraavaksi ensin peustarkistus
        //alustetaan lista mahdollisesti osuvista kulmista
        boolean[] tarkasteltavatKulmat1 = {false,false,false};
        boolean[] tarkasteltavatKulmat2 = {false,false,false};
        
        //jos eivät ole x-akselilla samalla alueella ei jatketa tarkastusta
        double x1Min = minimi(k1, 0);
        double x1Max = maksimi(k1,0);
        double x2Min = minimi(k2,0);
        double x2Max = maksimi(k2,0);
        double y1Min = minimi(k1,1);
        double y1Max = maksimi(k1,1);
        double y2Min = minimi(k2,1);
        double y2Max = maksimi(k2,1);
        //kolmion 1 kulman 1 mahdollisuus törmätä kolmioon 2, ts. on samalla alueella
        if (k1.getKoordinaatit()[0][0]>x2Min && k1.getKoordinaatit()[0][0]<x2Max &&
            k1.getKoordinaatit()[0][1]>y2Min && k1.getKoordinaatit()[0][1]<y2Max){
            tarkasteltavatKulmat1[0]=true;
        }
        //sama kolmio 1, kulma 2
        if (k1.getKoordinaatit()[1][0]>x2Min && k1.getKoordinaatit()[1][0]<x2Max &&
            k1.getKoordinaatit()[1][1]>y2Min && k1.getKoordinaatit()[1][1]<y2Max){
            tarkasteltavatKulmat1[1]=true;
        }
        //kolmio 1, kulma 3
        if (k1.getKoordinaatit()[2][0]>x2Min && k1.getKoordinaatit()[2][0]<x2Max &&
            k1.getKoordinaatit()[2][1]>y2Min && k1.getKoordinaatit()[2][1]<y2Max){
            tarkasteltavatKulmat1[2]=true;
        }
        //kolmio 2, kulma 1
        if (k2.getKoordinaatit()[0][0]>x1Min && k2.getKoordinaatit()[0][0]<x1Max &&
            k2.getKoordinaatit()[0][1]>y1Min && k2.getKoordinaatit()[0][1]<y1Max){
            tarkasteltavatKulmat2[0]=true;
        }
        //kolmio 2, kulma 2
        if (k2.getKoordinaatit()[1][0]>x1Min && k2.getKoordinaatit()[1][0]<x1Max &&
            k2.getKoordinaatit()[1][1]>y1Min && k2.getKoordinaatit()[1][1]<y1Max){
            tarkasteltavatKulmat2[1]=true;
        }
        //kolmio 2, kulma 3
        if (k2.getKoordinaatit()[2][0]>x1Min && k2.getKoordinaatit()[2][0]<x1Max &&
            k2.getKoordinaatit()[2][1]>y1Min && k2.getKoordinaatit()[2][1]<y1Max){
            tarkasteltavatKulmat2[2]=true;
        }
                
        //luodaan Osumapisteolio jonka funktiot tarkastelee tilanteen tarkemmin
        // ja funktio osumaTapahtunut palauttaa arvon onko törmäys todella tapahtunut
        Osumapiste op = new Osumapiste(k1,k2,tarkasteltavatKulmat1, tarkasteltavatKulmat2);

        return op;
            
    }

    
    private double minimi(Kolmio k, int akseli){
        double min= k.getKoordinaatit()[0][akseli];
        if (k.getKoordinaatit()[1][akseli]<min){
            min=k.getKoordinaatit()[1][akseli];
        }
        if (k.getKoordinaatit()[2][akseli]<min){
            min=k.getKoordinaatit()[2][akseli];
        }
        return min;
    }
    private double maksimi (Kolmio k, int akseli){
        double max = k.getKoordinaatit()[0][akseli];
        if(k.getKoordinaatit()[1][akseli]>max){
            max = k.getKoordinaatit()[1][akseli];
        }
        if(k.getKoordinaatit()[2][akseli]>max){
            max = k.getKoordinaatit()[2][akseli];
        }
        return max;
    }
    
    private void laskeTormayksenVaikutus(Osumapiste osumapiste){ 
 
        LiikkuvaKappale osuja;
        LiikkuvaKappale osuttu;
        if (osumapiste.osumaSivu[0]==0){
            osuja = kappale1;
            osuttu = kappale2;
            
        } else {
            osuja = kappale2;
            osuttu = kappale1;
        }
        //*****************
        Kolmio A = osuja.edellinenTilanne();
        Kolmio B = osuttu.edellinenTilanne();
        double[] normaali = osumapiste.normaaliVektori();
        
        //**************** kiertonopeudet
        double kiertoA = Math.PI*osuja.getKiertoAsteina()/180.0;
        double kiertoB = Math.PI*osuttu.getKiertoAsteina()/180.0;

        
        // osumapiste (x,y)
        double[] p = osumapiste.osumakohta;

        
        // vektorit kappaleiden törmäypisteestä massakeskipisteisiin:
        double APx = -(A.getKoordinaatit()[3][0]-p[0]);
        double APy = -(A.getKoordinaatit()[3][1]-p[1]);
        
        double BPx = -(B.getKoordinaatit()[3][0]-p[0]);
        double BPy = -(B.getKoordinaatit()[3][1]-p[1]);
        //näissä oli väärät etumerkit, piti olla mkp:istä törmäyspisteeseen
        
        //*************** edellisten vektoreiden ristitulot normaalin kanssa:
        double APn = APx*normaali[1]-APy*normaali[0];
        double BPn = BPx*normaali[1]-BPy*normaali[0];
        
        //*************** pisteen p nopeudet ennen törmäystä
        double nopeusAx = osuja.getNopeus()[0]-kiertoA*APy;
        double nopeusAy = osuja.getNopeus()[1]+kiertoA*APx;
        
        double nopeusBx = osuttu.getNopeus()[0]-kiertoB*BPy;
        double nopeusBy = osuttu.getNopeus()[1]+kiertoB*BPx;
        
        //*************** suhteellinen nopeus
        double nopeusABx = nopeusAx - nopeusBx;
        double nopeusABy = nopeusAy - nopeusBy; 

        //*************** Impulssi
        
        double impulssi =-(1.0+kimpoKerroin)*(nopeusABx*normaali[0]+
                nopeusABy*normaali[1])/((1/osuja.getMassa())+(1/osuttu.getMassa())+
                ((APn*APn)/osuja.getInertia())+((BPn*BPn)/osuttu.getInertia()));
        
        //*************** törmäyksen jälkeiset nopeudet
        double uusNopeusAx = nopeusAx + impulssi*normaali[0]/osuja.getMassa();
        double uusNopeusAy = nopeusAy + impulssi*normaali[1]/osuja.getMassa();
        double uusNopeusBx = nopeusBx - impulssi*normaali[0]/osuttu.getMassa();
        double uusNopeusBy = nopeusBy - impulssi*normaali[1]/osuttu.getMassa();
        //**************** uudet kulmanopeudet
        
        double uusKulmaA = (180/Math.PI)*(kiertoA+APn*impulssi/osuja.getInertia());
        double uusKulmaB = (180/Math.PI)*(kiertoB-BPn*impulssi/osuttu.getInertia());
     
        
        if (osumapiste.osumaSivu[0]==0){
            kappale1.paivitaTormayksenJalkeen(uusNopeusAx, uusNopeusAy, uusKulmaA);
            kappale2.paivitaTormayksenJalkeen(uusNopeusBx, uusNopeusBy, uusKulmaB);
        } else {
            kappale2.paivitaTormayksenJalkeen(uusNopeusAx, uusNopeusAy, uusKulmaA);
            kappale1.paivitaTormayksenJalkeen(uusNopeusBx, uusNopeusBy, uusKulmaB); 
        }
        
    }
       
    
}
