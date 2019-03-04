
package Piirtaja;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import Main.Kolmio;
import Main.LentorataSimulaattori;
import Main.LiikkuvaKappale;

public class Listener implements ActionListener{
    private JTextField kappale1Ax;
    private JTextField kappale1Bx;
    private JTextField kappale1Cx;
    private JTextField kappale1Ay;
    private JTextField kappale1By;
    private JTextField kappale1Cy;
    private JTextField kappale1Kulma;
    private JTextField kappale1Kulmanopeus;
    private JTextField kappale1Nopeus;
    
    private JTextField kappale2Ax;
    private JTextField kappale2Bx;
    private JTextField kappale2Cx;
    private JTextField kappale2Ay;
    private JTextField kappale2By;
    private JTextField kappale2Cy;
    private JTextField kappale2Kulma;
    private JTextField kappale2Kulmanopeus;
    private JTextField kappale2Nopeus;
    
    private JTextField massa1;
    private JTextField massa2;
    
    private JTextField tarkkailuVali;
    private JTextField iteraatioita;
    private JTextField piirtoTiheys;
    private JTextField eArvo;
    
    private JLabel feedback;
    private JButton submit;
    private JButton reset;
    private JButton start;
    
    //****************
    double[] kolmiot;
    double kulma1;
    double kulma2;
    double kk1;
    double kk2;
    double nopeus1;
    double nopeus2;
    double mass1;
    double mass2;
    
    double tarkkailu;
    int iteraatiot;
    int piirtovali;
    double e;
    
    
    
    public Listener(JTextField k1Ax,JTextField k1Bx, JTextField k1Cx,
        JTextField k1Ay,JTextField k1By, JTextField k1Cy,JTextField k1Kulma, JTextField k1kn,
        JTextField n1, JTextField n2, JTextField m1, JTextField m2,
        JTextField k2Ax, JTextField k2Bx, JTextField k2Cx, JTextField k2Ay, JTextField k2By,
        JTextField k2Cy, JTextField k2Kulma, JTextField k2kn, JTextField tv, JTextField it,
        JTextField pt, JLabel fb, JTextField eA, JButton s, JButton r, JButton st){
        
        this.kappale1Ax=k1Ax;
        this.kappale1Bx=k1Bx;
        this.kappale1Cx=k1Cx;
        this.kappale1Ay=k1Ay;
        this.kappale1By=k1By;
        this.kappale1Cy=k1Cy;
        this.kappale1Kulma=k1Kulma;
        this.kappale1Kulmanopeus=k1kn;
        this.kappale2Ax=k2Ax;
        this.kappale2Bx=k2Bx;
        this.kappale2Cx=k2Cx;
        this.kappale2Ay=k2Ay;
        this.kappale2By=k2By;
        this.kappale2Cy=k2Cy;
        this.kappale2Kulma=k2Kulma;
        this.kappale2Kulmanopeus=k2kn;
        this.tarkkailuVali=tv;
        this.iteraatioita=it;
        this.piirtoTiheys=pt;
        this.feedback=fb;
        this.eArvo=eA;
        this.kappale1Nopeus=n1;
        this.kappale2Nopeus=n2;
        
        this.massa1=m1;
        this.massa2=m2;
        
        this.submit=s;
        this.reset=r;
        this.start=st;
        start.setEnabled(true);
        //feedback.setText("Fill in the fields and press SUBMIT");
        
        //lisää vielä oletusarvot
        kolmiot = new double[12];
        resetoi();
        
    }
    
    @Override
    public void actionPerformed(ActionEvent ae){
        
        //***************************************************************
        if (ae.getSource()==submit){
            //tarkistetaan kulmat ja jos mättää annetaan feedback
            boolean kaikkiOK=true;
            if (!tarkastaKaikkiKulmat()){
                feedback.setText("Please submit proper xy-values.");
                kaikkiOK=false;
            }
            if (!onkoDouble(kappale1Kulma.getText())||!onkoDouble(kappale2Kulma.getText())){
                feedback.setText("Please submit proper (positive) angles.");
                kaikkiOK=false;
            } else {
                kulma1 = Double.parseDouble(kappale1Kulma.getText())%360.0;
                kulma2 = Double.parseDouble(kappale2Kulma.getText())%360.0;
            }
            if (!onkoDouble(kappale1Kulmanopeus.getText())||!onkoDouble(kappale2Kulmanopeus.getText())){
                feedback.setText("Please submit values for angular speeds");
                kaikkiOK = false;
            } else {
                kk1=Double.parseDouble(kappale1Kulmanopeus.getText());
                kk2=Double.parseDouble(kappale2Kulmanopeus.getText());
            }
            if (!onkoDouble(massa1.getText())|| !onkoDouble(massa2.getText())){
                feedback.setText("Please give valid values for the mass.");
                kaikkiOK = false;
            } else {
                double a = Double.parseDouble(massa1.getText());
                double b = Double.parseDouble(massa2.getText());
                if (a<= 0 || b <= 0){
                    feedback.setText("Please give valid values for the mass.");
                    kaikkiOK = false;
                } else {
                    mass1 = a;
                    mass2 = b;
                }
            }
            if (!onkoDouble(kappale1Nopeus.getText())||!onkoDouble(kappale2Nopeus.getText())){
                feedback.setText("Please submit proper values for the speeds (above 0).");
                kaikkiOK=false;
            } else {
                nopeus1= Double.parseDouble(kappale1Nopeus.getText());
                nopeus2= Double.parseDouble(kappale2Nopeus.getText());
            }
            if (!onkoDouble(tarkkailuVali.getText())){
                feedback.setText("Please set the interval of the observation (less than a second)");
                kaikkiOK=false;
            } else if (Double.parseDouble(tarkkailuVali.getText())>=1
                    ||Double.parseDouble(tarkkailuVali.getText())<=0){
                feedback.setText("Please set the interval of the observation (less than a second");
                kaikkiOK=false;
            } else {
                tarkkailu=Double.parseDouble(tarkkailuVali.getText());
            }
            if (!onkoInt(iteraatioita.getText())){
                feedback.setText("Please submit a proper value (an integer) for the number of iterations.");
                kaikkiOK=false;
            }else if (Integer.parseInt(iteraatioita.getText())<=0){
                 feedback.setText("The value for the iterations must be a positive integer.");
                 kaikkiOK=false;
            } else {
                iteraatiot =Integer.parseInt(iteraatioita.getText());
            }
            if (!onkoInt(piirtoTiheys.getText())){
                feedback.setText("Please submit an integer value");
                kaikkiOK=false;
            } else if (Integer.parseInt(piirtoTiheys.getText())<=0)  {
                feedback.setText("The frequency of the draws must be a positive integer");
                kaikkiOK=false;
            } else {
                piirtovali = Integer.parseInt(piirtoTiheys.getText());
            }
            if (onkoDouble(eArvo.getText())){
                double a = Double.parseDouble(eArvo.getText());
                if (a>=0 && a<=1){
                    this.e=a;
                } else {
                    feedback.setText("Please submit a valid value for the elasticity.");
                    kaikkiOK=false;
                }
            } else {
                feedback.setText("Please submit a valid value for the elasticity.");
                kaikkiOK=false;
            }
            if (kaikkiOK){
                feedback.setText("OK, press START to simulate.");
                start.setEnabled(true);
            } else {
                start.setEnabled(false);
            }
           
        //*******************************************************************    
        }
        if (ae.getSource()==reset){
            resetoi();
        }
        if (ae.getSource()==start){
            aloitaSimulointi();
            
        }
    }
    
    private void resetoi(){
        
        kolmiot[0]=0;
        kolmiot[1]=0;
        kolmiot[2]=100;
        kolmiot[3]=0;
        kolmiot[4]=80;
        kolmiot[5]=50;
        
        kolmiot[6]=0;
        kolmiot[7]=0;
        kolmiot[8]=100;
        kolmiot[9]=0;
        kolmiot[10]=80;
        kolmiot[11]=50;
        
        kulma1=45.0;
        kulma2=45.0;
        kk1=-20.0;
        kk2=10.0;
        nopeus1 =100.0;
        nopeus2=100.0;
        mass1=1.0;
        mass2=1.0;
        
        tarkkailu=0.01;
        iteraatiot=1200;
        piirtovali=30;
        e=1;
        
        feedback.setText("");
        
        kappale1Ax.setText(""+kolmiot[0]);
        kappale1Ay.setText(""+kolmiot[1]);
        kappale1Bx.setText(""+kolmiot[2]);
        kappale1By.setText(""+kolmiot[3]);
        kappale1Cx.setText(""+kolmiot[4]);
        kappale1Cy.setText(""+kolmiot[5]);
        kappale2Ax.setText(""+kolmiot[6]);
        kappale2Ay.setText(""+kolmiot[7]);
        kappale2Bx.setText(""+kolmiot[8]);
        kappale2By.setText(""+kolmiot[9]);
        kappale2Cx.setText(""+kolmiot[10]);
        kappale2Cy.setText(""+kolmiot[11]);
        
        kappale1Nopeus.setText(""+nopeus1);
        kappale2Nopeus.setText(""+nopeus2);
        
        kappale1Kulma.setText(""+kulma1);
        kappale2Kulma.setText(""+kulma2);
        
        kappale1Kulmanopeus.setText(""+kk1);
        kappale2Kulmanopeus.setText(""+kk2);
        
        tarkkailuVali.setText(""+tarkkailu);
        iteraatioita.setText(""+iteraatiot);
        piirtoTiheys.setText(""+piirtovali);
        eArvo.setText(""+e);
        massa1.setText(""+mass1);
        massa2.setText(""+mass2);

    }
    
    void aloitaSimulointi(){
        
        int[]ruudunMitat={1300, 700};
        double[] pisteet1 = {kolmiot[0],kolmiot[1],kolmiot[2],kolmiot[3],kolmiot[4],kolmiot[5]};
        double[] pisteet2 = {kolmiot[6],kolmiot[7],kolmiot[8],kolmiot[9],kolmiot[10],kolmiot[11]};
        
        Kolmio kolmio1 = new Kolmio(pisteet1, false);
        Kolmio kolmio2 = new Kolmio(pisteet2, true);
        
        LiikkuvaKappale kappale1 = new LiikkuvaKappale (kolmio1, mass1, kk1, nopeus1, kulma1);
        LiikkuvaKappale kappale2 = new LiikkuvaKappale (kolmio2, mass2, kk2, nopeus1, kulma1);
        
        LentorataSimulaattori sim = new LentorataSimulaattori(kappale1,kappale2,
        tarkkailu,iteraatiot,piirtovali, e);
        
        sim.simuloi();
    }
    
    boolean tarkastaKaikkiKulmat(){
        if (!tarkastaKulma(kappale1Ax.getText(), 0)){
            return false;
        } else if (!tarkastaKulma(kappale1Ay.getText(), 1)){
            return false;
        } else if (!tarkastaKulma(kappale1Bx.getText(), 2)){
            return false;
        } else if (!tarkastaKulma(kappale1By.getText(), 3)){
            return false;
        } else if (!tarkastaKulma(kappale1Cx.getText(), 4)){
            return false;
        } else if (!tarkastaKulma(kappale1Cy.getText(), 5)){
            return false;
        } else if (!tarkastaKulma(kappale2Ax.getText(), 6)){
            return false;
        } else if (!tarkastaKulma(kappale2Ay.getText(), 7)){
            return false;
        } else if (!tarkastaKulma(kappale2Bx.getText(), 8)){
            return false;
        } else if (!tarkastaKulma(kappale2By.getText(), 9)){
            return false;
        } else if (!tarkastaKulma(kappale2Cx.getText(), 10)){
            return false;
        } else if (!tarkastaKulma(kappale2Cy.getText(), 11)){
            return false;
        } else {
            return true;
        }
    }

    boolean tarkastaKulma(String s, int i){
        if (onkoDouble(s)){
            kolmiot[i] = Double.parseDouble(s);
            return true;
        } else {
            return false;
        }
    }
    
    private boolean onkoInt(String syote) {
        try {
            int a = Integer.parseInt(syote);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    private boolean onkoDouble(String syote) {
        try {
            double a = Double.parseDouble(syote);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    
}
