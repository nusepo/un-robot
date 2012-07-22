package imu.controller;

import imu.model.InfoIMU;

/**
 *
 * Por medio de esta interfaz se reporta cuando llega información nueva de una
 * IMU.
 *
 * @author David Saldaña
 */
public interface CapturadorIMUListener {
    
    public void nuevaInformacionIMU(InfoIMU nuevaInfo);
    
}
