#include <DynamixelSoftSerial.h>
#include <SoftwareSerial.h>

void setup(){
Dynamixel.begin(9600,5,6,7);  // Inicialize the servo at 1Mbps and Pin Control 2
delay(1000);
for(int i=1; i<=20; i++){
Dynamixel.reset(i);
}
Dynamixel.setBD(4, 9600);
Dynamixel.setBD(5, 9600);
}

void loop(){

  Dynamixel.move(4,random(200,800));  // Move the Servo radomly from 200 to 800
  delay(1000);
    Dynamixel.move(5,random(200,800));  // Move the Servo radomly from 200 to 800
  delay(1000);
  Dynamixel.moveSpeed(4,random(200,800),random(200,800));
  delay(2000);
  Dynamixel.moveSpeed(5,random(200,800),random(200,800));
  delay(2000);
  Dynamixel.setEndless(4,ON);
  Dynamixel.turn(4,RIGTH,1000);
  delay(3000);
  Dynamixel.turn(4,LEFT,1000);
  delay(3000);
  Dynamixel.setEndless(4,OFF);
  Dynamixel.setEndless(5,ON);
  Dynamixel.turn(5,RIGTH,1000);
  delay(3000);
  Dynamixel.turn(5,LEFT,1000);
  delay(3000);
  Dynamixel.setEndless(5,OFF);
  Dynamixel.ledStatus(4,ON);
  Dynamixel.ledStatus(5,ON);
  Dynamixel.moveRW(4,512);
  Dynamixel.moveRW(5,512);
  delay(1000);
  Dynamixel.action();
  Dynamixel.ledStatus(4,OFF);
  Dynamixel.ledStatus(5,OFF); 
delay(1000);

}
