
package Piirtaja;

import Main.LiikkuvaKappale;
import java.awt.Container;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class Kayttoliittyma implements Runnable {

    private JFrame frame;
    private Piirtoalusta piirtoalusta;
    private LiikkuvaKappale kappale1;
    private LiikkuvaKappale kappale2;
    private int piirtoTiheys;
    public int korkeus;
    public int leveys;
    
    public Kayttoliittyma (LiikkuvaKappale kappale1, LiikkuvaKappale kappale2, int piirtoTiheys, int[]ruudunMitat){
        this.kappale1=kappale1;
        this.kappale2=kappale2;
        this.piirtoTiheys=piirtoTiheys;
        this.leveys=ruudunMitat[0];
        this.korkeus=ruudunMitat[1];
    }
    
    int getLeveys(){
        return this.leveys;
    }

    @Override
    public void run() {
        frame = new JFrame("Mallinnus");
        frame.setPreferredSize(new Dimension(leveys, korkeus));
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        luoKomponentit(frame.getContentPane());

        frame.pack();
        frame.setVisible(true);
    }

    private void luoKomponentit(Container container) {
        piirtoalusta = new Piirtoalusta(this.kappale1, this.kappale2, korkeus, piirtoTiheys);
        container.add(piirtoalusta);
    }

    public JFrame getFrame() {
        return frame;
    }
}
