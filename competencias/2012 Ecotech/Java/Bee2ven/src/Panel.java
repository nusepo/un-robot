
import javax.swing.JFrame;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ALDAJO
 */
public class Panel {
    public static void main(String[] args) {
        GraficoBotones dibujo = new GraficoBotones();
        JFrame aplicacion = new JFrame();
        aplicacion.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        aplicacion.add(dibujo);
        aplicacion.setSize(250, 250);
        aplicacion.setVisible(true);
    }
}
