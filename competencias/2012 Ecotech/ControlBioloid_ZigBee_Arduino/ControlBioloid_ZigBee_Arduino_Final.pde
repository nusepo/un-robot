int E1 = 5;         //Controla la velocidad del motor 1.
int M1 = 4;         //Controla el sentido de giro motor 1.
int E2 = 6;         //Controla la velocidad del motor 2.                     
int M2 = 7;         //Controla el sentido de giro motor 2.
int value = 255;    /*Se define el valor de la velocidad de los motores 
                    como el más alto.(255 = Vlr más alto).*/

long timer = 0;      //Controla el tiempo de ejecución de una tarea.

uint8_t boton;       //Almacena el byte que identifíca al botón presionado.

//Variables auxiliares para buscar el start-byte de la trama.
uint8_t aux1;        
uint8_t aux2;

//Método de configutación.
void setup(){
 Serial.begin(57600);     //Velocidad a la cual trabaja el Xbee radio del control del Bioliod.
 pinMode(M1, OUTPUT);     //Pin n4 modo salida.
 pinMode(M2, OUTPUT);     //Pin n7 modo salida.
}

//Bucle infinito para controlar activamente el Arduino.
void loop(){
  
  //Verificar el tiempo permitido para la ejecución de una tarea.
  if(timer < 20000){
    //Verificar si hay datos en el serial-buffer
    if(Serial.available()>6){
      timer=0;                                //Se reinicia el timer.
      //Verificar el inicio de la trama.
      if(searchStartFrame()){
        boton = Serial.read();                //Se lee el byte que identifica al botón presionado.
        //Verificar cuál botón fue presionado.
        if(boton == 0x01){
          //Adelante.
            moveForward();
            timer++;
        }else if(boton == 0x02){
          //Atrás.
            moveBack();
            timer++;
        }else if(boton == 0x04){
          //Izquierda.
            moveLeft();
            timer++;
        }else if(boton == 0x08){
          //Derecha.
            moveRight();
            timer++;
        }
      }
      Serial.flush();        //Limpiar el buffer.
    }else {
      timer++;               //Incrementar el timer.
    } 
  }else {    
    stopRobot();              //Detener el robot.
    timer=0;                  //Reiniciar el contador, para una nueva tarea.
  }  
}

//Método para buscar el inicio de una trama.
boolean searchStartFrame(){
  
    aux1 = Serial.read();
    aux2 = Serial.read();
    
    while(aux1 != 0xFF && aux2 != 0x55){
      aux1 = aux2;
      aux2 = Serial.read();
    }
    
    return true;          
}

//Método para desplazar hacía adelante el robot.
void moveForward(){
   digitalWrite(M1,HIGH);
   digitalWrite(M2, HIGH);
   analogWrite(E1, value);   //PWM Speed Control
   analogWrite(E2, value);   //PWM Speed Control
}

//Método para desplazar hacía atrás el robot.
void moveBack(){
  digitalWrite(M1,LOW);
  digitalWrite(M2, LOW);
  analogWrite(E1, value);   //PWM Speed Control
  analogWrite(E2, value);   //PWM Speed Control
}

//Método para girar hacía la izquierda el robot.
void moveLeft(){
  digitalWrite(M1,LOW);
  digitalWrite(M2, HIGH);
  analogWrite(E1, value);   //PWM Speed Control
  analogWrite(E2, value);   //PWM Speed Control
}

//Método para girar hacía la derecha el robot.
void moveRight(){
  digitalWrite(M1,HIGH);
  digitalWrite(M2, LOW);
  analogWrite(E1, value);   //PWM Speed Control
  analogWrite(E2, value);   //PWM Speed Control
}

//Método paradetener el robot.
void stopRobot(){
  analogWrite(E1, 0);       //PWM Speed Control
  analogWrite(E2, 0);       //PWM Speed Control
}

