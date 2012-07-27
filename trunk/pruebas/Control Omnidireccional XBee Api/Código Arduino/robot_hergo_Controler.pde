/**
 * Copyright (c) 2009 Andrew Rapp. All rights reserved.
 *
 * This file is part of XBee-Arduino.
 *
 * XBee-Arduino is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * XBee-Arduino is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with XBee-Arduino.  If not, see <http://www.gnu.org/licenses/>.
 */
 
#include <XBee.h>
#include <Servo.h> 

/*
This example is for Series 2 XBee
Receives a ZB RX packet and sets a PWM value based on packet data.
Error led is flashed if an unexpected packet is received
*/

XBee xbee = XBee();
XBeeResponse response = XBeeResponse();
// create reusable response objects for responses we expect to handle 
ZBRxResponse rx = ZBRxResponse();
ModemStatusResponse msr = ModemStatusResponse();

int statusLed = 13;
int errorLed = 13;
int dataLed = 13;

//create the servo variables
Servo servo1;
Servo servo2;
Servo servo3;

int velServo1 = 90;
int velServo2 = 90;
int velServo3 = 90;

void flashLed(int pin, int times, int wait) {
    
    for (int i = 0; i < times; i++) {
      digitalWrite(pin, HIGH);
      delay(wait);
      digitalWrite(pin, LOW);
      
      if (i + 1 < times) {
        delay(wait);
      }
    }
}

int velReceive(int vel, int sign){
  int valueSign=0;
  int velReturn = 0;
  if(sign == 0x00){
    valueSign=1;
  }else{
    valueSign=-1;
  }
  velReturn = (vel*valueSign) + 90;
    
  return velReturn;
}

void setup() {
  pinMode(statusLed, OUTPUT);
  pinMode(errorLed, OUTPUT);
  pinMode(dataLed,  OUTPUT);
  
  // start serial
  xbee.begin(9600);
  
  flashLed(statusLed, 3, 50);
  servo1.attach(9);
  servo2.attach(10);
  servo3.attach(11);
}

// continuously reads packets, looking for ZB Receive or Modem Status
void loop() {
    
    xbee.readPacket();
    
    if (xbee.getResponse().isAvailable()) {
      // got something
      
      if (xbee.getResponse().getApiId() == ZB_RX_RESPONSE) {
        // got a zb rx packet
        
        // now fill our zb rx class
        xbee.getResponse().getZBRxResponse(rx);
        
         uint8_t value0 = rx.getData(0);
         uint8_t sign1 = rx.getData(1);
         uint8_t value1 = rx.getData(2);
         uint8_t sign2 = rx.getData(3);
         uint8_t value2 = rx.getData(4);
         uint8_t sign3 = rx.getData(5);
         uint8_t value3 = rx.getData(6);
         
         if(value0 == 0xF8){
           
         velServo1 = velReceive(value1, sign1);
         velServo2 = velReceive(value2, sign2);
         velServo3 = velReceive(value3, sign3);
         
         servo1.write(velServo1);
         servo2.write(velServo2);
         servo3.write(velServo3);
         
        }else{
          servo1.write(90);
          servo2.write(90);
          servo3.write(90);
        }
        
      } else {
          servo1.write(90);
          servo2.write(90);
          servo3.write(90);
      }
    } else if (xbee.getResponse().isError()) {
          servo1.write(90);
          servo2.write(90);
          servo3.write(90);      
    }
    delay(10);
}
