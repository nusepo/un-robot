/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package imu.controller;

import imu.model.InfoIMU;

/**
 * Esta clase está basada en: IMU Guide: http://starlino.com/imu_guide.html
 *
 * @author dav
 */
public class FiltroKalman {
// gyro weight/smooting factor
    private static final double W_GYRO= 10;

    
    //
    boolean firstSample;	  //marks first sample
    //Notation "w" stands for one of the axes, so for example RwAccelerometer[0],RwAccelerometer[1],RwAccelerometer[2] means RxAcc,RyAcc,RzAcc
    //Variables below must be global (their previous value is used in getEstimatedInclination)
    double RwEstimated[] = new double[3];     //Rw estimated from combining RwAccelerometer and RwGyroscope
    long lastMicros;
    
    public FiltroKalman() {
        setup();
    }

    private void setup() {

        firstSample = true;
    }

    public double[] filtrar(InfoIMU dato) {

        //projection of normalized gravitation force vector on x/y/z axis, as measured by accelerometer
        double RwAccelerometer[] = {dato.getAccelerometerX(), dato.getAccelerometerY(),
            dato.getAccelerometerZ()};




        // Estampa de tiempo actual
        long newMicros = System.currentTimeMillis();

        //FIXME falta tener en cuenta el Z dato.getGiroZ()


        // Delta de tiempo
        //compute interval since last sampling time (in seconds) 
        long deltaT = (newMicros - lastMicros) / 1000;
        //save for next loop, please note interval will be invalid in first sample but we don't use it
        lastMicros = newMicros;




        //normalize vector (convert to a vector with same direction and with length 1)
        normalize3DVector(RwAccelerometer);


        // La primer vez solo se mide (measure) el acelerometro
        if (firstSample) {
            //initialize with accelerometer readings
            // La estimación (update) se actualiza con los mismos valores sensados (measure)
            for (int w = 0; w <= 2; w++) {
                RwEstimated[w] = RwAccelerometer[w];    //FIXME cuando se almacenó el valor del acelerometro aquí?
            }

        } else {
            double RwGyroscope[] = new double[3];        //Rw obtained from last estimated value and gyro movement

            //evaluate RwGyroscope vector
            if (Math.abs(RwEstimated[2]) < 0.1) {
                //Rz is too small and because it is used as reference for computing Axz, Ayz it's error fluctuations will amplify leading to bad results
                //in this case skip the gyro data and just use previous estimate

                //Con el giroscopio se estima
                for (int w = 0; w <= 2; w++) {
                    RwGyroscope[w] = RwEstimated[w];
                }


            } else {

                //angles between projection of R on XZ/YZ plane and Z axis (deg)
                double proyectedAnglesOnXZYZ[] = new double[2];
                double[] gyroMedido = {dato.getGiroX(), dato.getGiroY(), dato.getGiroZ()};
                //FIXME se puede usar los tres ejes como en el acelerometro

                //////////// ESTIMATION ///////
                // Calcular los ángulos con respecto al eje X
                //get angles between projection of R on ZX/ZY plane and Z axis, based on last RwEstimated
                for (int w = 0; w <= 1; w++) {
                    double tmpf = gyroMedido[w] * deltaT; //get angle change in deg angle=w*t

                    proyectedAnglesOnXZYZ[w] = Math.atan2(RwEstimated[w], RwEstimated[2]);   //get angle and convert to degrees        
                    proyectedAnglesOnXZYZ[w] += tmpf;                                 //get updated angle according to gyro movement
                }

                // Reverse calculation of RwGyro from Awz angles, for formulas deductions see  http://starlino.com/imu_guide.html
                for (int w = 0; w <= 1; w++) {                   
                    RwGyroscope[0] = Math.sin(proyectedAnglesOnXZYZ[0]);
                    RwGyroscope[0] /= Math.sqrt(
                            1 + Math.pow(Math.cos(proyectedAnglesOnXZYZ[0]), 2)
                            * Math.pow(Math.tan(proyectedAnglesOnXZYZ[1]), 2));

                    RwGyroscope[1] = Math.sin(proyectedAnglesOnXZYZ[1]);
                    RwGyroscope[1] /= Math.sqrt(1
                            + Math.pow(Math.cos(proyectedAnglesOnXZYZ[1]), 2)
                            * Math.pow(Math.tan(proyectedAnglesOnXZYZ[0]), 2));
                }

                // Estimate sign of RzGyro by looking in what qudrant the angle Axz is, 
                // RzGyro is positive if  Axz in range -90 ..90 => cos(proyectedAnglesOnXZYZ) >= 0
                int signRzGyro = (Math.cos(proyectedAnglesOnXZYZ[0]) >= 0) ? 1 : -1;

                //reverse calculation of RwGyro from Awz angles, for formulas deductions see  http://starlino.com/imu_guide.html
                for (int w = 0; w <= 1; w++) {
                    RwGyroscope[0] = Math.sin(proyectedAnglesOnXZYZ[0]);
                    RwGyroscope[0] /= Math.sqrt(1 + Math.pow(Math.cos(proyectedAnglesOnXZYZ[0]), 2)
                            * Math.pow(Math.tan(proyectedAnglesOnXZYZ[1]), 2));
                    RwGyroscope[1] = Math.sin(proyectedAnglesOnXZYZ[1]);
                    RwGyroscope[1] /= Math.sqrt(1 + Math.pow(Math.cos(proyectedAnglesOnXZYZ[1]), 2)
                            * Math.pow(Math.tan(proyectedAnglesOnXZYZ[0]), 2));
                }
                RwGyroscope[2] = signRzGyro * Math.sqrt(1 - Math.pow(RwGyroscope[0], 2) - Math.pow(RwGyroscope[1], 2));
            }


            //////////// ESTIMATION////////////////////
            //combine Accelerometer and gyro readings
            for (int w = 0; w <= 2; w++) {
                // XXX falta sumarle la incertidumbre del medido
                RwEstimated[w] = (RwAccelerometer[w] + W_GYRO * RwGyroscope[w]) / (1 + W_GYRO);
            }

            normalize3DVector(RwEstimated);

        }

        firstSample = false;


        return RwEstimated;
    }

    public static void normalize3DVector(double[] RwAcc) {
        double R;
        R = Math.sqrt(RwAcc[0] * RwAcc[0] + RwAcc[1] * RwAcc[1] + RwAcc[2] * RwAcc[2]);
        RwAcc[0] /= R;
        RwAcc[1] /= R;
        RwAcc[2] /= R;
    }

    
}
