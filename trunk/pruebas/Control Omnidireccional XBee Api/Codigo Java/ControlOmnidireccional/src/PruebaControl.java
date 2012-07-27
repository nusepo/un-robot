
import javax.swing.JFrame;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ALDAJO
 */
public class PruebaControl {
    public static void main(String[] args) {
        ControlToMove panel = new ControlToMove();
        
        JFrame aplicacion = new JFrame();
        
        aplicacion.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        aplicacion.add(panel);
        aplicacion.setSize(250, 250);
        aplicacion.setVisible(true);
        
    }
}
