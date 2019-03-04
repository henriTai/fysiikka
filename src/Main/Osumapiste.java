
package Main;

import java.lang.Math;

public class Osumapiste {
    
    private Kolmio[] kolmiot;
    private boolean[][]m; //mahdolliset osumat
    public int[]osumaSivu; //0 = kolmio, 1 = törmäävä kulma
    public double[]osumakohta;
    private double[][]osuttavanSivunPisteet;
    
    public Osumapiste(Kolmio k1, Kolmio k2, boolean[] m1, boolean[] m2){
        this.kolmiot = new Kolmio[2];
        this.m = new boolean[2][3];
        this.kolmiot[0]=k1;
        this.kolmiot[1]=k2;
        this.m[0]=m1;
        this.m[1]=m2;
        this.osumaSivu= new int[2];
        for (int i=0; i<2;i++)osumaSivu[i]=-1;
        this.osuttavanSivunPisteet = new double[2][2];
        for (int i=0;i<2;i++){
            for (int j=0; j<2; j++){
                osuttavanSivunPisteet[i][j]=0;
            }
        }
        tarkasteleMahdolliset();
    }
    
    private void tarkasteleMahdolliset(){
        //käydään läpi saatu lista mahdollisesti leikkaavista kulmista (boolean[2][3] m)
        for (int i=0;i<2;i++){
            for (int j=0;j<3;j++){
                if (m[i][j]){ //jos on tosi, tarkastetaan
                    tsekkaaLeikkaukset(i,j);
                }
            }
        }
    }
    
    private void tsekkaaLeikkaukset(int kolmio, int kulma){
        //muuttujat tarkasteltavan kolmion muille kahdelle kulmalle
        int toinenKulma;
        int kolmasKulma;
        //riippuen mitä kulmaa tarkastellaan, muut kaksi ovat siis:
        switch (kulma) {
            case 0:
                toinenKulma=1;
                kolmasKulma=2;
                break;
            case 1:
                toinenKulma=2;
                kolmasKulma=0;
                break;
            default:
                toinenKulma=0;
                kolmasKulma=1;
                break;
        }
        //jos kulmasta lähtevät KUMPIKIN sivu leikkaa niin kyseessä on leikkaava kulma
        boolean eka =leikkaus(kolmio, kulma, toinenKulma);
        boolean toka = leikkaus(kolmio, kulma, kolmasKulma);
        if (eka!=true||toka!=true){
            m[kolmio][kulma]=false;
        } else {
            osumaSivu[0]=kolmio;
            osumaSivu[1]=kulma;
            this.osumakohta = leikkausPiste(kolmio, kulma, toinenKulma);
        }
    }
    
    private boolean leikkaus(int kolmio, int kulma, int toinenKulma){
        //toinen kolmio (int)
        int t;
        if (kolmio==0){
            t=1;
        } else {
            t=0;
        }
        //jana tarkasteltavasta kulmasta sivun toiseen päähän, min ja max
        double suoraxMin = pienempiArvo(kolmio,0,kulma,toinenKulma);
        double suoraxMax = suurempiArvo(kolmio,0,kulma,toinenKulma);
        double suorayMin = pienempiArvo(kolmio,1,kulma,toinenKulma);
        double suorayMax = suurempiArvo(kolmio,1,kulma,toinenKulma);
        
        //toisen kolmion ekan sivun kärkipisteiden koordinaatit jatkolaskuja varten
        double t1xMin = pienempiArvo(t,0,0,1);//kolmio, akseli, kulma1, kulma2
        double t1xMax = suurempiArvo(t,0,0,1);
        double t1yMin = pienempiArvo(t,1,0,1);
        double t1yMax = suurempiArvo(t,1,0,1);
        //tokan sivun
        double t2xMin = pienempiArvo(t,0,1,2);
        double t2xMax = suurempiArvo(t,0,1,2);
        double t2yMin = pienempiArvo(t,1,1,2);
        double t2yMax = suurempiArvo(t,1,1,2);
        //kolmannen sivun
        double t3xMin = pienempiArvo(t,0,0,2);
        double t3xMax = suurempiArvo(t,0,0,2);
        double t3yMin = pienempiArvo(t,1,0,2);
        double t3yMax = suurempiArvo(t,1,0,2);
        //tarkasteltavan suoran kulmakerroin
        double suoranKK=((kolmiot[kolmio].getKoordinaatit()[toinenKulma][1]-
                kolmiot[kolmio].getKoordinaatit()[kulma][1])/
                (kolmiot[kolmio].getKoordinaatit()[toinenKulma][0]-
                kolmiot[kolmio].getKoordinaatit()[kulma][0]));
        //suoran vakio
        double suoranVakio = -kolmiot[kolmio].getKoordinaatit()[kulma][0]*
                suoranKK + kolmiot[kolmio].getKoordinaatit()[kulma][1];
        //toisen kolmion sivun1 kulmakerroin
        double t1KK = ((kolmiot[t].getKoordinaatit()[1][1] - kolmiot[t].getKoordinaatit()[0][1])/
                (kolmiot[t].getKoordinaatit()[1][0] - kolmiot[t].getKoordinaatit()[0][0]));
        //toisen kolmion sivun1 vakio
        double t1Vakio = -kolmiot[t].getKoordinaatit()[0][0]*t1KK +
                kolmiot[t].getKoordinaatit()[0][1];
        //sivun 2 kk
        double t2KK = ((kolmiot[t].getKoordinaatit()[2][1] - kolmiot[t].getKoordinaatit()[1][1])/
                (kolmiot[t].getKoordinaatit()[2][0] - kolmiot[t].getKoordinaatit()[1][0]));
        //sivun 2 vakio
        double t2Vakio = -kolmiot[t].getKoordinaatit()[1][0]*t2KK +
                kolmiot[t].getKoordinaatit()[1][1];
        //sivun 3 kk
        double t3KK = ((kolmiot[t].getKoordinaatit()[0][1] - kolmiot[t].getKoordinaatit()[2][1])/
                (kolmiot[t].getKoordinaatit()[0][0] - kolmiot[t].getKoordinaatit()[2][0]));
        //sivun 3 vakio
        double t3Vakio = -kolmiot[t].getKoordinaatit()[2][0]*t3KK +
                kolmiot[t].getKoordinaatit()[2][1];
        //lasketaan tarkasteltavan sivun ja toisen kolmion sivun 1 leikkauspisteen x ja y
        if (suoranKK != t1KK){ //eli eivät ole samansuuntaisia
            double lpX=(t1Vakio-suoranVakio)/(suoranKK-t1KK);
            double lpY = lpX*suoranKK+suoranVakio;
            //kuuluuko leikkauspiste kummankin janan alueeseen
            if (lpX > suoraxMin && lpX < suoraxMax && lpX > t1xMin && lpX < t1xMax &&
                    lpY > suorayMin && lpY < suorayMax && lpY > t1yMin && lpY < t1yMax){
                return true;
            }
        }
        //sama toiselle sivulle
        if (suoranKK != t2KK){
            double lpX=(t2Vakio-suoranVakio)/(suoranKK-t2KK);
            double lpY = lpX*suoranKK+suoranVakio;
            if (lpX > suoraxMin && lpX < suoraxMax && lpX > t2xMin && lpX < t2xMax &&
                    lpY > suorayMin && lpY < suorayMax && lpY > t2yMin && lpY < t2yMax){
                return true;
            }
        }
        //sama kolmannelle sivulle
        if (suoranKK != t3KK){

            double lpX=(t3Vakio-suoranVakio)/(suoranKK-t3KK);
            double lpY = lpX*suoranKK+suoranVakio;
            if (lpX > suoraxMin && lpX < suoraxMax && lpX > t3xMin && lpX < t3xMax &&
                    lpY > suorayMin && lpY < suorayMax && lpY > t3yMin && lpY < t3yMax){
                return true;
            }
        }
        //jos ei leikannut
        return false;
    }
    
    private double[] leikkausPiste(int kolmio, int kulma, int toinenKulma){
        double[] paluu = {0,0};
        //toinen kolmio (int)
        int t;
        if (kolmio==0){
            t=1;
        } else {
            t=0;
        }
        //jana tarkasteltavasta kulmasta sivun toiseen päähän, min ja max
        double suoraxMin = pienempiArvo(kolmio,0,kulma,toinenKulma);
        double suoraxMax = suurempiArvo(kolmio,0,kulma,toinenKulma);
        double suorayMin = pienempiArvo(kolmio,1,kulma,toinenKulma);
        double suorayMax = suurempiArvo(kolmio,1,kulma,toinenKulma);
        
        //toisen kolmion ekan sivun kärkipisteiden koordinaatit jatkolaskuja varten
        double t1xMin = pienempiArvo(t,0,0,1);//kolmio, akseli, kulma1, kulma2
        double t1xMax = suurempiArvo(t,0,0,1);
        double t1yMin = pienempiArvo(t,1,0,1);
        double t1yMax = suurempiArvo(t,1,0,1);
        //tokan sivun
        double t2xMin = pienempiArvo(t,0,1,2);
        double t2xMax = suurempiArvo(t,0,1,2);
        double t2yMin = pienempiArvo(t,1,1,2);
        double t2yMax = suurempiArvo(t,1,1,2);
        //kolmannen sivun
        double t3xMin = pienempiArvo(t,0,0,2);
        double t3xMax = suurempiArvo(t,0,0,2);
        double t3yMin = pienempiArvo(t,1,0,2);
        double t3yMax = suurempiArvo(t,1,0,2);
        //tarkasteltavan suoran kulmakerroin
        double suoranKK=((kolmiot[kolmio].getKoordinaatit()[toinenKulma][1]-
                kolmiot[kolmio].getKoordinaatit()[kulma][1])/
                (kolmiot[kolmio].getKoordinaatit()[toinenKulma][0]-
                kolmiot[kolmio].getKoordinaatit()[kulma][0]));
        //suoran vakio
        double suoranVakio = -kolmiot[kolmio].getKoordinaatit()[kulma][0]*
                suoranKK + kolmiot[kolmio].getKoordinaatit()[kulma][1];
        //toisen kolmion sivun1 kulmakerroin
        double t1KK = ((kolmiot[t].getKoordinaatit()[1][1] - kolmiot[t].getKoordinaatit()[0][1])/
                (kolmiot[t].getKoordinaatit()[1][0] - kolmiot[t].getKoordinaatit()[0][0]));
        //toisen kolmion sivun1 vakio
        double t1Vakio = -kolmiot[t].getKoordinaatit()[0][0]*t1KK +
                kolmiot[t].getKoordinaatit()[0][1];
        //sivun 2 kk
        double t2KK = ((kolmiot[t].getKoordinaatit()[2][1] - kolmiot[t].getKoordinaatit()[1][1])/
                (kolmiot[t].getKoordinaatit()[2][0] - kolmiot[t].getKoordinaatit()[1][0]));
        //sivun 2 vakio
        double t2Vakio = -kolmiot[t].getKoordinaatit()[1][0]*t2KK +
                kolmiot[t].getKoordinaatit()[1][1];
        //sivun 3 kk
        double t3KK = ((kolmiot[t].getKoordinaatit()[0][1] - kolmiot[t].getKoordinaatit()[2][1])/
                (kolmiot[t].getKoordinaatit()[0][0] - kolmiot[t].getKoordinaatit()[2][0]));
        //sivun 3 vakio
        double t3Vakio = -kolmiot[t].getKoordinaatit()[2][0]*t3KK +
                kolmiot[t].getKoordinaatit()[2][1];
        //lasketaan tarkasteltavan sivun ja toisen kolmion sivun 1 leikkauspisteen x ja y
        if (suoranKK != t1KK){ //eli eivät ole samansuuntaisia
            double lpX=(t1Vakio-suoranVakio)/(suoranKK-t1KK);
            double lpY = lpX*suoranKK+suoranVakio;
            //kuuluuko leikkauspiste kummankin janan alueeseen
            if (lpX > suoraxMin && lpX < suoraxMax && lpX > t1xMin && lpX < t1xMax &&
                    lpY > suorayMin && lpY < suorayMax && lpY > t1yMin && lpY < t1yMax){
                this.osuttavanSivunPisteet[0][0]=kolmiot[t].getKoordinaatit()[0][0];
                this.osuttavanSivunPisteet[0][1]=kolmiot[t].getKoordinaatit()[0][1];
                this.osuttavanSivunPisteet[1][0]=kolmiot[t].getKoordinaatit()[1][0];
                this.osuttavanSivunPisteet[1][1]=kolmiot[t].getKoordinaatit()[1][1];
                paluu[0]=lpX;
                paluu[1]=lpY;
                return paluu;
            }
        }
        //sama toiselle sivulle
        if (suoranKK != t2KK){
            double lpX=(t2Vakio-suoranVakio)/(suoranKK-t2KK);
            double lpY = lpX*suoranKK+suoranVakio;
            if (lpX > suoraxMin && lpX < suoraxMax && lpX > t2xMin && lpX < t2xMax &&
                    lpY > suorayMin && lpY < suorayMax && lpY > t2yMin && lpY < t2yMax){
                paluu[0]=lpX;
                paluu[1]=lpY;
                this.osuttavanSivunPisteet[0][0]=kolmiot[t].getKoordinaatit()[1][0];
                this.osuttavanSivunPisteet[0][1]=kolmiot[t].getKoordinaatit()[1][1];
                this.osuttavanSivunPisteet[1][0]=kolmiot[t].getKoordinaatit()[2][0];
                this.osuttavanSivunPisteet[1][1]=kolmiot[t].getKoordinaatit()[2][1];
                return paluu;
            }
        }
        //sama kolmannelle sivulle
        if (suoranKK != t3KK){

            double lpX=(t3Vakio-suoranVakio)/(suoranKK-t3KK);
            double lpY = lpX*suoranKK+suoranVakio;
            if (lpX > suoraxMin && lpX < suoraxMax && lpX > t3xMin && lpX < t3xMax &&
                    lpY > suorayMin && lpY < suorayMax && lpY > t3yMin && lpY < t3yMax){
                paluu[0]=lpX;
                paluu[1]=lpY;
                this.osuttavanSivunPisteet[0][0]=kolmiot[t].getKoordinaatit()[2][0];
                this.osuttavanSivunPisteet[0][1]=kolmiot[t].getKoordinaatit()[2][1];
                this.osuttavanSivunPisteet[1][0]=kolmiot[t].getKoordinaatit()[0][0];
                this.osuttavanSivunPisteet[1][1]=kolmiot[t].getKoordinaatit()[0][1];
                return paluu;
            }
        }
        //jos ei leikannut
        return paluu;
    }
    
    private double pienempiArvo(int kolmio,int akseli,  int kulma1, int kulma2){
        double pienin = kolmiot[kolmio].getKoordinaatit()[kulma1][akseli];
        if (kolmiot[kolmio].getKoordinaatit()[kulma2][akseli]<pienin){
            pienin = kolmiot[kolmio].getKoordinaatit()[kulma2][akseli];
        }
        return pienin;
    }
    private double suurempiArvo(int kolmio,int akseli, int kulma1, int kulma2){
        double suurin = kolmiot[kolmio].getKoordinaatit()[kulma1][akseli];
        if (kolmiot[kolmio].getKoordinaatit()[kulma2][akseli]>suurin){
            suurin = kolmiot[kolmio].getKoordinaatit()[kulma2][akseli];
        }
        return suurin;
    }
    
    public boolean osumaTapahtunut(){
        for (int i=0;i<2;i++){
            for (int j=0;j<3;j++){
                if (m[i][j]){
                    return true;
                }
            }
        }
        return false;
    }
    public int osuvaKolmio(){
        for (int i=0;i<2;i++){
            for (int j=0;j<3;j++){
                if (m[i][j]){
                    return i;
                }
            }
        }
        return -1;
    }
    public int osuvaKulma(){
        for (int i=0;i<2;i++){
            for (int j=0;j<3;j++){
                if (m[i][j]){
                    return j;
                }
            }
        }
        return -1;
    }
    
    
    public double[] normaaliVektori(){
        double xSuuntaan = osuttavanSivunPisteet[1][0] - osuttavanSivunPisteet[0][0];
        double ySuuntaan = osuttavanSivunPisteet[1][1] - osuttavanSivunPisteet[0][1];
        double x = xSuuntaan/Math.sqrt(xSuuntaan*xSuuntaan+ySuuntaan*ySuuntaan);
        double y = ySuuntaan/Math.sqrt(xSuuntaan*xSuuntaan+ySuuntaan*ySuuntaan);
        double normaali[] ={-y, x};
        return normaali;
        
    }
    
    
}
