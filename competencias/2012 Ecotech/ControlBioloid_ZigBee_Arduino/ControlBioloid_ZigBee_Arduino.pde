int led1 = 13; 
int led2 = 12;
uint8_t data;             //Variable para almacenar el byte entrante.

void setup(){
 Serial.begin(57600);     //Velocidad a la que trabaja el Xbee radio del control del Bioliod.
 pinMode(led1, OUTPUT);   //pin 13 de salida.
 pinMode(led2, OUTPUT);   //pin 12 de salida.
}

void loop(){
  
 if(Serial.available() > 12){  //Verificando el número de de bytes en el serial data buffer.
    if(Serial.read() == 0xFF && Serial.read() == 0x55){   //Comprobando bytes de inicio.
      data = Serial.read();    
      
      //Ciclo para descartar los bytes restantes de la trama.
      for(int i=0; i<Serial.available(); i++){
        byte discard = Serial.read();
      }
      
      if(data == 0x01){            //Verificando el botón del control que se oprimió (El botón "UP" corresponde a 0x01).
        //Encendiendo LED.
        digitalWrite(led1, HIGH);
        delay(1000);
        digitalWrite(led1, LOW);
      }else if(data == 0x02){      //Verificando el botón del control que se oprimió (El botón "DOWN" corresponde a 0x02).
        //Encendiendo LED.
        digitalWrite(led2, HIGH);
        delay(1000);
        digitalWrite(led2, LOW);
      }
    }
 } 
 
}
