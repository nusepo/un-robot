
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ALDAJO
 */
public class ComunicationS {
    private CommPortIdentifier portIdentifier = null;
    private SerialPort puerto = null;
    private OutputStream outputStream = null;

    public ComunicationS() {
    try{
        portIdentifier = CommPortIdentifier.getPortIdentifier(PuertoSerial.identifierPortName());
        //abre el puerto
        puerto = (SerialPort) portIdentifier.open("Mi aplicacion", 10000);

        // Establece todos los parámetros
        puerto.setSerialPortParams(
                9600,
                SerialPort.DATABITS_8,
                SerialPort.STOPBITS_1,
                SerialPort.PARITY_NONE);
        puerto.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
        //IMPORTANTE: definir el timeout
        puerto.enableReceiveTimeout(10000);
        
        //obtiene el flujo de salida
        outputStream = puerto.getOutputStream();

        JOptionPane.showMessageDialog(null, "Conectado Correctamente");
        
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "No se pudo conectar al puerto\nError tipo\n"+ e);
            System.exit(0);
        }
    }
    //con este método enviamos datos por el puerto serial
    public void sendValue(int value){
        try {
            outputStream.write(value);
        } catch (IOException ex) {
            Logger.getLogger(ComunicationS.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            outputStream.flush();
        } catch (IOException ex) {
            Logger.getLogger(ComunicationS.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}