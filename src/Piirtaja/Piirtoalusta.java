package Piirtaja;

import Main.LiikkuvaKappale;
import java.awt.Color;
import java.awt.Graphics;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;

public class Piirtoalusta extends JPanel {

    private LiikkuvaKappale kappale1;
    private LiikkuvaKappale kappale2;
    private int korkeus;
    private int piirtoTiheys;

    public Piirtoalusta(LiikkuvaKappale kappale1, LiikkuvaKappale kappale2, int korkeus, int piirtoTiheys) {
        super.setBackground(Color.WHITE);
        this.kappale1 = kappale1;
        this.kappale2=kappale2;
        this.korkeus=korkeus;
        this.piirtoTiheys=piirtoTiheys;
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        
        for (int i=0; i<kappale1.getVaiheet().size()-1;i++){
            double[][] p1 = kappale1.getVaiheet().get(i).getKoordinaatit();
            double[][] p2 = kappale2.getVaiheet().get(i).getKoordinaatit();
            if (i%this.piirtoTiheys==0){
                    graphics.drawLine((int)p1[0][0], korkeus-(int)p1[0][1],
                        (int)p1[1][0], korkeus-(int)p1[1][1]);
                    graphics.drawLine((int)p1[1][0], korkeus-(int)p1[1][1],
                        (int)p1[2][0], korkeus-(int)p1[2][1]);
                    graphics.drawLine((int)p1[2][0], korkeus-(int)p1[2][1],
                        (int)p1[0][0], korkeus-(int)p1[0][1]);
                    graphics.fillOval((int)p1[3][0]-3, korkeus-((int)p1[3][1]+3), 6, 6);
                    graphics.drawLine((int)p2[0][0], korkeus-(int)p2[0][1],
                        (int)p2[1][0], korkeus-(int)p2[1][1]);
                    graphics.drawLine((int)p2[1][0], korkeus-(int)p2[1][1],
                        (int)p2[2][0], korkeus-(int)p2[2][1]);
                    graphics.drawLine((int)p2[2][0], korkeus-(int)p2[2][1],
                        (int)p2[0][0], korkeus-(int)p2[0][1]);
                    graphics.fillOval((int)p2[3][0]-3, korkeus-((int)p2[3][1]+3), 6, 6);
            }
        }

    }
}
