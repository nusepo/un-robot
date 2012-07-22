package imu.controller;

import imu.model.InfoIMU;
import gnu.io.*;
import java.io.*;
import java.util.Queue;
import java.util.TooManyListenersException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Esta clase se encarga de capturar las tramas del IMU que llegan al puerto
 * serial.
 *
 * @author David Saldaña
 */
public class CapturadorIMU implements SerialPortEventListener {
    //Analog reference voltage in milivolts

    private final static int VDD = 5;
    // Accelerometer zero level (mV) @ 0 G
    private static final int ZERO_LEVEL_ACC = 1650;
    //  Accelerometer Sensisitivity mV/g
    private static final int SENS_ACC = 478;
    // Gyro Sensitivity mV/deg/ms    
    private static final int ZERO_LEVEL_GYRO = 1230;
    // Gyro Sensitivity mV/deg/ms    
    private static final int SENS_GYRO = 2000;
    // Serial port communication // 
    private SerialPort puerto;
    private InputStream inputStream;
    private OutputStream os;
    private BufferedReader is;
    private int datosRecibidos;
    private CapturadorIMUListener listener;
    // Buffer de tramaBuffer
    int tramaBuffer[] = new int[16];
    int indiceBuffer = 0;

    public CapturadorIMU() {
    }

    public void iniciar() throws Exception {
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

    @Override
    public void serialEvent(SerialPortEvent spe) {
        //         System.out.println(" -");

        if (spe.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            try {

                while (inputStream.available() > 0) {
                    int newData = inputStream.read();
//                    System.out.print(Integer.toHexString(newData) + " ");

                    //////// Hay que leer una tramaBuffer como la siguiente////
                    //41 1 2d 1 fb 2 e 2 a3 1 f2 2 3 2 0 5a 
                    //41 1 2e 1 fb 2 c 2 a3 1 f3 2 3 1 ff 5a 
                    //41 1 2f 1 fb 2 d 2 a3 1 f2 2 3 1 ff 5a 
                    //41 1 30 1 f9 2 f 2 a1 1 f2 2 3 1 ff 5a 
                    //41 1 31 1 fb 2 f 2 a4 1 f4 2 3 2 0 5a 
                    //41 1 32 1 f9 2 e 2 a3 1 f4 2 3 2 0 5a 
                    //41 1 33 1 fa 2 c 2 a4 1 f3 2 3 2 1 5a 
                    //41 1 34 1 f7 2 d 2 a3 1 f3 2 4 1 ff 5a 
                    //41 1 35 1 fb 2 e 2 a7 1 f3 2 4 2 1 5a 



                    // Si es un inicio de tramaBuffer
                    if (newData == 'A') {
                        datosRecibidos++;
                        indiceBuffer = 0;
                    }


                    tramaBuffer[indiceBuffer++] = newData;
//                    indiceBuffer++;


                    // Si la tramaBuffer está completa
                    if (indiceBuffer == 16) {
                        // validar si la tramaBuffer está correcta
                        if (tramaBuffer[0] == 'A' && tramaBuffer[15] == 'Z') {
                            nuevaTrama(tramaBuffer);
                        } else {
                            System.out.println("Trama invalida" + (tramaBuffer[0]) + " " + (tramaBuffer[15]));
                        }

                        indiceBuffer = 0;
                    }




                    if (newData == 'Z') {
//                        System.out.println();
                    }
                }
            } catch (IOException ex) {
            }
        }
    }

    private void iniciarIMU() throws IOException {
        datosRecibidos = 0;

        // Espacio ' ' para detener
        detener();
        // Menú 3 para activar el modeo automático
        toogleAutoMode();
        // Menú 9 para iniciar
        iniciarRecepccion();

        // Si no inició automático, entonces volver a establecer como automático
        if (datosRecibidos < 20) {
            detener();
            toogleAutoMode();
            iniciarRecepccion();
        } else {
            System.out.println("Ya se habìan recibido datos");
        }

    }

    private void detener() throws IOException {
        os.write(' ');
        os.flush();


        os.write(' ');
        os.flush();

        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
        }
    }

    private void toogleAutoMode() throws IOException {

        os.write('3');
        os.flush();
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
        }
    }

    private void iniciarRecepccion() throws IOException {
        os.write('9');
        os.flush();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
        }
    }

    private void nuevaTrama(int[] trama) {
        InfoIMU dato = tramaToDato(trama);

        if (listener != null) {
            listener.nuevaInformacionIMU(dato);
        }

    }

    private InfoIMU tramaToDato(int[] trama) {
        // valores que vienen en la tramaBuffer
        int valores[] = new int[7];



        // Convertir pares de bytes en valores enteros
        int i = 1;
        for (int j = 0; j < valores.length; j++) {
            valores[j] = trama[i++] << 8 | trama[i++];
        }


        // Convertir los valores al tipo de dato IMU.
        double ax = adcToPhysicalVariable(valores[1], ZERO_LEVEL_ACC, SENS_ACC, 1);
        double ay = adcToPhysicalVariable(valores[2], ZERO_LEVEL_ACC, SENS_ACC, 1);
        double az = adcToPhysicalVariable(valores[3], ZERO_LEVEL_ACC, SENS_ACC, 1);

        double gx = adcToPhysicalVariable(valores[4], ZERO_LEVEL_GYRO, SENS_GYRO, 1);
        double gy = adcToPhysicalVariable(valores[5], ZERO_LEVEL_GYRO, SENS_GYRO, 1);
        double gz = adcToPhysicalVariable(valores[6], ZERO_LEVEL_GYRO, SENS_GYRO, 1);



        InfoIMU datoIMU = new InfoIMU(valores[0], ax, ay, az, gx, gy, gz);

        return datoIMU;
    }

    /**
     * @return the listener
     */
    public CapturadorIMUListener getListener() {
        return listener;
    }

    /**
     * @param listener the listener to set
     */
    public void setListener(CapturadorIMUListener listener) {
        this.listener = listener;
    }

    /**
     *
     * Convert ADC value for to physical units see
     * http://starlino.com/imu_guide.html for explanation.
     *
     * @param adcInput adc value to convert.
     * @param zeroLevel voltage relative to zero level (mV)
     * @param inpSens input sensitivity in mV/G(acc) or mV/deg/ms(gyro)
     * @param inpInvert invert axis value according to configuration
     * @return For accelerometer it will return g (acceleration). For gyro it
     * will return deg/ms (rate of rotation).
     *
     */
    private double adcToPhysicalVariable(int adcInput, int zeroLevel, int inpSens, int inpInvert) {
        float tmpf = 0;	        //temporary variable
        tmpf = adcInput * VDD / 1023.0f;  //voltage (mV)
        tmpf -= zeroLevel;  //voltage relative to zero level (mV)
        tmpf /= inpSens;    //input sensitivity in mV/G(acc) or mV/deg/ms(gyro)
        tmpf *= inpInvert;  //invert axis value according to configuration 
        return tmpf;
    }
}
