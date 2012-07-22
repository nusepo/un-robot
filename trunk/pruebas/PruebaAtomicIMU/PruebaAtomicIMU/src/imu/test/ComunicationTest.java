/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package imu.test;

import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;
import imu.controller.CapturadorIMU;
import imu.controller.CapturadorIMUListener;
import imu.model.InfoIMU;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dav
 */
public class ComunicationTest implements CapturadorIMUListener {

    public ComunicationTest() {
        try {
            CapturadorIMU capturadorIMU = new CapturadorIMU();
            capturadorIMU.setListener(this);


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void nuevaInformacionIMU(InfoIMU nuevaInfo) {
        System.out.println("Nueva info: contador=" + nuevaInfo.getContador() + "acelX=" + nuevaInfo.getAccelerometerX());
    }

    public static void main(String[] args) {
        new ComunicationTest();
    }
}
