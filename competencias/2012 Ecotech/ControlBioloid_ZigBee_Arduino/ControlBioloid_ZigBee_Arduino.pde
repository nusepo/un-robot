int led1 = 13;
int led2 = 12;
uint8_t data;

void setup(){
 Serial.begin(57600);
 pinMode(led1, OUTPUT); 
 pinMode(led2, OUTPUT);
}

void loop(){
  
  
 if(Serial.available() > 0){
    if(Serial.read() == 0xFF && Serial.read() == 0x55){
      data = Serial.read();
      
      if(data == 0x01){
        digitalWrite(led1, HIGH);
        delay(50);
        digitalWrite(led1, LOW);
      }else if(data == 0x02){
        digitalWrite(led2, HIGH);
        delay(50);
        digitalWrite(led2, LOW);
      }
    }
 } 
}
