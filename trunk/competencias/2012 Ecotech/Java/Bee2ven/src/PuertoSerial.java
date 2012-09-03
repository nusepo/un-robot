/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import gnu.io.*;
import java.util.Enumeration;
import javax.swing.JOptionPane;

/**
 *
 * @author ALDAJO
 */
public class PuertoSerial {
    //Enlista los puertos
    public static String identifierPortName(){
        Enumeration commports = CommPortIdentifier.getPortIdentifiers();
        CommPortIdentifier myCPI;
        String[] portsName = null;
        String SelectedPortName = null;
        
        int counter = 0;
        while(commports.hasMoreElements()){
            myCPI = (CommPortIdentifier) commports.nextElement();
            if(counter == 0){
                portsName = new String[1];
                portsName[counter] = myCPI.getName();
            }else{
                String[] temp = portsName;
                portsName = null;
                portsName = new String[counter+1];
                for(int i = 0; i<temp.length; i++){
                    portsName[i] = temp[i];
                }
                portsName[counter] = myCPI.getName();
            }
            counter++;
        }
        
        if(portsName == null){
            //en caso de que no hayan puertos
            JOptionPane.showMessageDialog(null, "Lo lamentamos, no hay puertos habilitados");
            JOptionPane.showMessageDialog(null, "El programa se cerrará");
            System.exit(0);
            return null;
            //imprimimos los puertos en pantalla
        }else{
            System.out.println("puertos disponibles:");
            for(int i=0; i<portsName.length; i++){
                System.out.println(i+". "+portsName[i]);
            }
            //le decimos al usuario que seleccione un puerto
             SelectedPortName = (String) JOptionPane.showInputDialog(null,
            "Choose One Port", "Input",
            JOptionPane.INFORMATION_MESSAGE, null,
            portsName, portsName[0]);
            if(SelectedPortName == null){
                JOptionPane.showMessageDialog(null, "El programa se cerrará");
                System.exit(0);
            }
            return SelectedPortName;
        }
        
    }
}
