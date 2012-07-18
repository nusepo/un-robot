/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pruebaimu.serial;

import gnu.io.*;
import java.io.*;
import java.util.TooManyListenersException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dav
 */
public class TestReadingIMUValues implements SerialPortEventListener {

    SerialPort puerto;
    InputStream inputStream;
    OutputStream os;
    BufferedReader is;

    public TestReadingIMUValues() throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException, IOException {
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier("/dev/ttyUSB0");
        //abre el puerto
        puerto = (SerialPort) portIdentifier.open("Mi aplicacion", 10000);

        // Establece todos los parámetros
        puerto.setSerialPortParams(
                115200,
                SerialPort.DATABITS_8,
                SerialPort.STOPBITS_1,
                SerialPort.PARITY_NONE);
        puerto.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);

        //IMPORTANTE: definir el timeout
        puerto.enableReceiveTimeout(10000);



        //obtiene el flujo
        inputStream = puerto.getInputStream();
        os = puerto.getOutputStream();
        is = new BufferedReader(new InputStreamReader(inputStream));


        try {
            puerto.addEventListener(this);
            puerto.notifyOnDataAvailable(true);
        } catch (TooManyListenersException ex) {
            ex.printStackTrace();
        }

        iniciarIMU();

    }

    public static void main(String[] args) {
        try {
            new TestReadingIMUValues();

        } catch (NoSuchPortException ex) {
            Logger.getLogger(TestReadingIMUValues.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PortInUseException ex) {
            Logger.getLogger(TestReadingIMUValues.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedCommOperationException ex) {
            Logger.getLogger(TestReadingIMUValues.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TestReadingIMUValues.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private int newData;

    @Override
    public void serialEvent(SerialPortEvent spe) {
        //         System.out.println(" -");

        if (spe.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            try {

                while (inputStream.available() > 0) {
                    newData = inputStream.read();
                    System.out.print(Integer.toHexString(newData) + " ");

                    if (newData == 'Z') {
                        System.out.println();
                    }
                }
            } catch (IOException ex) {
            }
        }
    }

    private void iniciarIMU() throws IOException {
        // Menú 3 para activar el modeo automático
        detener();

//        // Leer todo lo que haya en el buffer
//        for (int i = 0; i < 18; i++) {
//            String texto = is.readLine();
//            System.out.println(texto);
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException ex) {
//            }
//
//        }

//        os.write('3');
//        os.flush();

//        // Leer todo lo que haya en el buffer
//        for (int i = 0; i < 18; i++) {
//            String texto = is.readLine();
//            System.out.println(texto);
//        }

        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
        }
        // Menú 9 para iniciar
        os.write('9');
        os.flush();

        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
        }
        // Leer todo lo que haya en el buffer
//        for (int i = 0; i < 2; i++) {
//            String texto = is.readLine();
//            
//            
//            System.out.println(texto);
//        }
        int i = 50;
        String texto;
        while (!(texto = is.readLine()).contains("Exiting")) {
            System.out.println(texto);
            if (i-- == 0) {
                break;
            }
        }

    }

    private void detener() throws IOException {
        os.write(' ');
        os.flush();

        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
        }

        os.write(' ');
        os.flush();

        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
        }
    }
}
