/**
 * Este programa lee las señales enviadas por 5 canales
 * PWM generadas por un receptor inalámbrico de un control
 * remoto. El controlador permite manejar el robot con
 * tres ruedas omnidireccionales, un servo para subir un
 * brazo, una pinza en la parte delantera del brazo y una
 * compuerta en la parte trasera.
 * 
 * @author David Saldaña - Grupo RSR
 * Universidad Nacional de Colombia - Sede Medellín
 * @date 2011
 *
 */

#include <math.h>
#include <Servo.h> 


//Servos
Servo servoPinza;
Servo servoBrazo;                
Servo servoPuerta; 

// Posible error generado por ruido.
double ERROR = 30.0;

// Rudo channel
int chRudo =  2;
// Gear channel
int chGear =  6;
// THRO channel
int chThro = 4;

// Valores
unsigned long chRudoValue;
unsigned long chGearValue;
unsigned long chThroValue;

// Valores viejos
unsigned long oldRudoValue;
unsigned long oldThroValue;

// Valores normalizados
double throNormalizado;
double rudoNormalizado;

// Estado del servo: 0. direccion; 1. brazo
int estado=0;


// Angulos de servos
double anguloPuerta;
double anguloPinza;
double anguloBrazo;


void setup()
{
  pinMode(chRudo, INPUT);
  pinMode(chGear, INPUT);
  pinMode(chThro, INPUT);

  // Open the serial port at 9600 bps:  
  Serial.begin(9600);        


  servoPinza.attach(10);  
  // Attaches the servo on pin 9 to the servo object 
  servoBrazo.attach(9);
  servoPuerta.attach(11);

  // Rele
  pinMode(13, OUTPUT);
}

void loop()
{
  // Leer los tres PWM
  chRudoValue = pulseIn(chRudo, HIGH);
  chGearValue = pulseIn(chGear, HIGH);
  chThroValue = pulseIn(chThro, HIGH);

  Serial.print("Rudo=");
  Serial.print(chRudoValue);
  Serial.print(" Gear=");
  Serial.print(chGearValue);
  Serial.print(" Thro=");
  Serial.print(chThroValue);
  Serial.print(" - ");


  // Se normaliza el Thro si cambio mucho
  if(abs(chThroValue - oldThroValue) > ERROR){
    // Se normaliza el rudo
    throNormalizado = (chThroValue-1100.0)/800.0;

    oldThroValue = chThroValue;
  }




  //Revisar estado del servo
  if(chGearValue > 1500){
    // Estado 1 es decir, chanel=0
    estado = 0;

     
    // Se controla la puerta
   anguloPuerta= 50 + throNormalizado * 70.0;
   servoPuerta.write(anguloPuerta);
    
    
    Serial.print(" AnguloPuerta=");
    Serial.print(anguloPuerta);
  }
  else{
    estado = 1;


    // Se normaliza el Rudo
    if(abs(chRudoValue - oldRudoValue) > ERROR){
      // Se normaliza el rudo
      rudoNormalizado = (chRudoValue - 1100.0) / 800.0;

      oldThroValue = chThroValue;
    }
    
    // Se controla el brazo y la pinza
    // Se controla la puerta
    anguloPinza= 180.0 - rudoNormalizado * 60.0;
    servoPinza.write(anguloPinza);
    
    anguloBrazo = 190.0 - throNormalizado * 95.0;

    servoBrazo.write(anguloBrazo);
    
     Serial.print(" AnguloPinza=");
    Serial.print(anguloPinza);
    
    Serial.print(" AnguloBrazo=");
    Serial.print(anguloBrazo);
    
  }

  // Estado 0 es direccion
  digitalWrite(13, !estado); 

  Serial.print(" estado");
  Serial.print(estado);
  Serial.println(" ---->");
}



