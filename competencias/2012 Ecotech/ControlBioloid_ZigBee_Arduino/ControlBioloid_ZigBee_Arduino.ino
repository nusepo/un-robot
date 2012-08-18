int E1 = 5; //controla la velocidad del motor 1
int M1 = 4; //controla el sentido motor 1
int E2 = 6; //controla la velocidad del motor 2                     
int M2 = 7; //controla el sentido motor 2     
uint8_t data;             //Variable para almacenar el byte entrante.
int value = 255;

void setup(){
 Serial.begin(57600);     //Velocidad a la que trabaja el Xbee radio del control del Bioliod.
 pinMode(M1, OUTPUT);   
 pinMode(M2, OUTPUT); 
}

void loop(){
  
 if(Serial.available() > 12){  //Verificando el número de de bytes en el serial data buffer.
    if(Serial.read() == 0xFF && Serial.read() == 0x55){   //Comprobando bytes de inicio.
      data = Serial.read();

      if(data == 0x01){            //Verificando el botón del control que se oprimió (El botón "UP" corresponde a 0x01).
        //Encendiendo LED.
        digitalWrite(M1,HIGH);
        digitalWrite(M2, HIGH);
        analogWrite(E1, value);   //PWM Speed Control
        analogWrite(E2, value);   //PWM Speed Control
      }else if(data == 0x02){      //Verificando el botón del control que se oprimió (El botón "DOWN" corresponde a 0x02).
        digitalWrite(M1,LOW);
        digitalWrite(M2, LOW);
        analogWrite(E1, value);   //PWM Speed Control
        analogWrite(E2, value);   //PWM Speed Control
      }else if(data == 0x00){
        analogWrite(E1, 0);       //PWM Speed Control
        analogWrite(E2, 0);       //PWM Speed Control
      }
      else if(data == 0x04){
        digitalWrite(M1,LOW);
        digitalWrite(M2, HIGH);
        analogWrite(E1, value);   //PWM Speed Control
        analogWrite(E2, value);   //PWM Speed Control
      }
        else if(data == 0x08){
        digitalWrite(M1,HIGH);
        digitalWrite(M2, LOW);
        analogWrite(E1, value);   //PWM Speed Control
        analogWrite(E2, value);   //PWM Speed Control
      }
      }
           Serial.flush();
    } 
 
}
