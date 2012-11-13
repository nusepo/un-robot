/*
 *
 * The ProebaAtomicIMU Project is free software; you can redistribute it
 * and/or modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * The MobSim Project  is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 */

package imu.test;

import imu.controller.CapturadorIMU;
import imu.controller.CapturadorIMUListener;
import imu.model.InfoIMU;

/**
 * This class aims to test the communication with the IMU module. Every package
 * is received and shown in the console.
 * 
 * @author David Saldaña
 */
public class CommunicationTest implements CapturadorIMUListener {

    /**
     * Default constructor.
     */
    public CommunicationTest() {
        try {
            // A controller
            CapturadorIMU capturadorIMU = new CapturadorIMU();
            // Add this class as a listener.
            capturadorIMU.setListener(this);


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void nuevaInformacionIMU(InfoIMU nuevaInfo) {
        System.out.println("New info: counter=" + nuevaInfo.getContador() + "acelX=" + nuevaInfo.getAccelerometerX());
    }

    public static void main(String[] args) {
        new CommunicationTest();
    }
}
