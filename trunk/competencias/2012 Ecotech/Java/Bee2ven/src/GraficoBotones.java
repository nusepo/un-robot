
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ALDAJO
 */
public class GraficoBotones extends JPanel{
    //atributos de la clase
    
    private int width = 0;
    private int height = 0;
    private float diameter = 0;
    private int initialValue = 0;
    private int finalValue = 0;
    
    public void setValue(int value){
        value = 0;
    }
    
    public void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D) g;       
        width = getWidth();
        height = getHeight();
        diameter = Math.min(width, height);
        initialValue = (int)diameter/10;
        finalValue = (int)(9*diameter)/10;
        g2.setStroke(new BasicStroke(diameter/20));
        //dibujo una linea vertical
        g2.drawLine((int)diameter/2, initialValue, (int)diameter/2, (int)finalValue);
    }
}
