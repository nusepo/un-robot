#include <SoftwareSerial.h>
#include <DynamixelSerial.h>

int motors[8] = {8, 7, 12, 17, 16, 18, 11, 9};
// Connect Arduino pin 9 to TX of usb-serial device
uint8_t ssRX = 9;
// Connect Arduino pin 10 to RX of usb-serial device
uint8_t ssTX = 10;
SoftwareSerial mySerial(ssRX, ssTX);
//valor del tipo de dato
uint8_t typeData = 0;
//valor de la orden recivida
uint8_t data = 0;
//vaor que acompaña a la orden
uint8_t data2 = 0;
//valor de la velocidad inicial
int value = 255;

int E1 = 5; //controla la velocidad del motor 1
int M1 = 4; //controla el sentido motor 1
int E2 = 6; //controla la velocidad del motor 2                     
int M2 = 7; //controla el sentido motor 2     

void desactivateRotation(int motor){
  Dynamixel.setEndless(motor,OFF);
  delay(1000);
}

void moveMotor1(int value){
  Dynamixel.move(motors[0], value);  // Move the Servo radomly from 200 to 800
  Dynamixel.move(motors[1], value);  // Move the Servo radomly from 200 to 800
}

void moveMotor2(int value){
  Dynamixel.move(motors[2], value);  // Move the Servo radomly from 200 to 800
  Dynamixel.move(motors[3], value);  // Move the Servo radomly from 200 to 800
}

void moveHands1(int value){
  Dynamixel.move(motors[4], value);  // Move the Servo radomly from 200 to 800
  Dynamixel.move(motors[5], 1023-value);  // Move the Servo radomly from 200 to 800
}

void moveHands2(int value){
  Dynamixel.move(motors[6], value);  // Move the Servo radomly from 200 to 800
  Dynamixel.move(motors[7], 1023-value);  // Move the Servo radomly from 200 to 800
}

int scale(int num, int nmin, int nmax){
  int prop = map(num, 0, 100, nmin, nmax);
  return prop;
}

void setup(){
  Dynamixel.begin(50000, 2);  // Inicialize the servo at 1Mbps and Pin Control 2
  mySerial.begin(9600);
  pinMode(M1, OUTPUT);   
  pinMode(M2, OUTPUT); 
  moveMotor1(810);
  moveMotor2(810);
  delay(1000);
}

void loop(){
  if(mySerial.available() > 3){
    digitalWrite(13, LOW);
    //recibimos la trama inicial
     if(mySerial.read() == 0x6E){
       //leemos el tipo de dato (movimiento(0xE1) o brazo(0xE2))
         typeData = mySerial.read();
         //valor que acompaña al dato
         data = mySerial.read();
         //valor transmitido
         data2 = mySerial.read();
           if(typeData == 0xE1){
                   if(data == 0x01){
                  //adelante
                      digitalWrite(M1,HIGH);
                      digitalWrite(M2, HIGH);
                      analogWrite(E1, data2);   //PWM Speed Control
                      analogWrite(E2, data2);   //PWM Speed Control
                  }else if(data == 0x02){
                  //atrás
                      digitalWrite(M1,LOW);
                      digitalWrite(M2, LOW);
                      analogWrite(E1, data2);   //PWM Speed Control
                      analogWrite(E2, data2);   //PWM Speed Control
                  }else if(data == 0x03){
                    //giro en sentido horario
                      digitalWrite(M1,LOW);
                      digitalWrite(M2, HIGH);
                      analogWrite(E1, data2);   //PWM Speed Control
                      analogWrite(E2, data2);   //PWM Speed Control
                  }else if(data == 0x04){
                    //giro en sentido anti-horario
                      digitalWrite(M1,HIGH);
                      digitalWrite(M2, LOW);
                      analogWrite(E1, data2);   //PWM Speed Control
                      analogWrite(E2, data2);   //PWM Speed Control
                  }else if(data == 0x00){
                    //stop
                      analogWrite(E1, 0);       //PWM Speed Control
                      analogWrite(E2, 0);       //PWM Speed Control
                  }
           }else if(typeData == 0xe2){          //close 1st if type data
                 if(data == 0x05){
                       moveMotor1(scale(data2, 810, 341));
                 }else if(data == 0x06){
                       moveMotor2(scale(data2, 810, 200));
                 }else if(data == 0x07){
                   //pendiete
                 }else if(data == 0x08){
                   //pendiente
                 }
           }else if(typeData == 0x00){          //close 3rd if type data
             
           }
  mySerial.flush();
    }
  }
}
