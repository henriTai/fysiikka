
package Piirtaja;

import java.awt.*;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class UI implements Runnable{
    
    private JFrame mainframe;

    @Override
    public void run() {
        mainframe = new JFrame("Lenroratasimulaattori");
        mainframe.setPreferredSize(new Dimension (1200,700));//muuta kolmiossa myös!
        mainframe.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        luoKomponentit(mainframe.getContentPane());

        mainframe.pack();
        mainframe.setVisible(true);
    }
    
    private void luoKomponentit(Container container){
        GridLayout mainLayout = new GridLayout(1,4);
        container.setLayout(mainLayout);
        
        JTextArea selitys = new JTextArea("Define the dimensions of two triangular objects\nby"
                + " giving their dimensions as as xy-coordinates.\nA preferred size"
                + " for a side is 10-150 \n(the width of the screen is 1200 (pixels).");
        
        selitys.setEnabled(false);
        container.add(selitys);
        
        JLabel aLabel = new JLabel("Object 1"); 
        JLabel bLabel = new JLabel("Object 2");
        JLabel pointA = new JLabel("Point A");
        JLabel pointA2 = new JLabel("Point A");
        JLabel pointB = new JLabel ("Point B");
        JLabel pointB2 = new JLabel ("Point B");
        JLabel pointC = new JLabel ("PointC");
        JLabel pointC2 = new JLabel ("PointC");
        
        JTextField k1ax = new JTextField();
        JTextField k1ay = new JTextField();
        JTextField k1bx = new JTextField();
        JTextField k1by = new JTextField();
        JTextField k1cx = new JTextField();
        JTextField k1cy = new JTextField();
        JTextField k2ax = new JTextField();
        JTextField k2ay = new JTextField();
        JTextField k2bx = new JTextField();
        JTextField k2by = new JTextField();
        JTextField k2cx = new JTextField();
        JTextField k2cy = new JTextField();
        
        JPanel panel1= new JPanel(new GridLayout(20,1));
        panel1.add(aLabel);
        panel1.add(pointA);
        panel1.add(k1ax);
        panel1.add(k1ay);
        panel1.add(pointB);
        panel1.add(k1bx);
        panel1.add(k1by);
        panel1.add(pointC);
        panel1.add(k1cx);
        panel1.add(k1cy);
        panel1.add(bLabel);
        panel1.add(pointA2);
        panel1.add(k2ax);
        panel1.add(k2ay);
        panel1.add(pointB2);
        panel1.add(k2bx);
        panel1.add(k2by);
        panel1.add(pointC2);
        panel1.add(k2cx);
        panel1.add(k2cy);
        
        container.add(panel1);
        
        
        //************************
        JLabel speedText = new JLabel("The speed for the objects A and B:");
        JTextField speed1 = new JTextField();
        JTextField speed2 = new JTextField();
        
        JLabel rotationText = new JLabel("Start angles (in degrees)");
        JTextField rot1 = new JTextField();
        JTextField rot2 = new JTextField();
        
        JLabel angularText = new JLabel("Angular speeds (in degrees/second)");
        JTextField ang1 = new JTextField();
        JTextField ang2 = new JTextField();
        
        JLabel massText = new JLabel("The mass of the objects A and B.");
        JTextField mass1 = new JTextField();
        JTextField mass2 = new JTextField();
        
        JPanel panel2= new JPanel(new GridLayout(12,1));
        
        panel2.add(speedText);
        panel2.add(speed1);
        panel2.add(speed2);
        panel2.add(rotationText);
        panel2.add(rot1);
        panel2.add(rot2);
        panel2.add(angularText);
        panel2.add(ang1);
        panel2.add(ang2);
        panel2.add(massText);
        panel2.add(mass1);
        panel2.add(mass2);
        
        container.add(panel2);
        //************************
        JLabel intervalliTeksti = new JLabel("The interval between the iterations.");
        JTextField intervalli = new JTextField();
        JLabel iteraatiotTeksti = new JLabel("The number of the iterations.");
        JTextField iteraatiot = new JTextField();
        JLabel piirtovaliTeksti = new JLabel("The divider of how often the iteration is drawn.");
        JTextField piirtovali = new JTextField();
        JLabel eTeksti = new JLabel("Elasticity (0-1)");
        JTextField eArvo = new JTextField();
        JButton submit = new JButton("SUBMIT");
        JButton reset = new JButton ("RESET");
        JButton start = new JButton ("START");
        JLabel feedback = new JLabel("");
        
        JPanel panel3= new JPanel(new GridLayout(12,1));
        panel3.add(intervalliTeksti);
        panel3.add(intervalli);
        panel3.add(iteraatiotTeksti);
        panel3.add(iteraatiot);
        panel3.add(piirtovaliTeksti);
        panel3.add(piirtovali);
        panel3.add(eTeksti);
        panel3.add(eArvo);
        panel3.add(submit);
        panel3.add(reset);
        panel3.add(start);
        panel3.add(feedback);
        
        container.add(panel3);

        //************************
        Listener kuuntelija = new Listener(k1ax, k1bx, k1cx, k1ay, k1by, k1cy, rot1, ang1,
        speed1, speed2, mass1, mass2, k2ax, k2bx, k2cx, k2ay, k2by, k2cy, rot2, ang2,
        intervalli, iteraatiot, piirtovali, feedback, eArvo,submit, reset, start);
        
        submit.addActionListener(kuuntelija);
        reset.addActionListener(kuuntelija);
        start.addActionListener(kuuntelija);
        //************************                
    }
    
    
}
