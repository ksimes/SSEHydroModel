#include <Wire.h>
#include <Adafruit_PWMServoDriver.h>

// called this way, it uses the default address 0x40
Adafruit_PWMServoDriver pwm = Adafruit_PWMServoDriver();
// you can also call it with a different address you want
//Adafruit_PWMServoDriver pwm = Adafruit_PWMServoDriver(0x41);

#define SERVOMIN  0 // 150 // this is the 'minimum' pulse length count (out of 4096)
#define SERVOMAX  255 // this is the 'maximum' pulse length count (out of 4096)

void setup()
{
  Serial.begin(115200);
  Serial.println("16 channel Servo test!");

  pwm.begin();
  pwm.setPWMFreq(60);  // Analog servos run at ~60 Hz updates
}

void loop()
{
 Serial.println("Red");
 pwm.reset();
 pwm.setPWM(0, 0, 255);   //0% duty cycle on red led
 pwm.setPWM(1, 255, 0);   //100% duty cycle on green led
 pwm.setPWM(2, 255, 0);  // 100% duty cycle on blue led
 delay(2000);    

 pwm.setPWM(0, 255, 0);   //100% duty cycle on red led
 pwm.setPWM(1, 255, 0);   //100% duty cycle on green led
 pwm.setPWM(2, 255, 0);  // 100% duty cycle on blue led
 delay(2000);    

// Serial.println("Green");
// pwm.reset();
// pwm.setPWM(1, 255,0);
// delay(2000);    
//
// Serial.println("Blue");
// pwm.reset();
// pwm.setPWM(2, 255,0);
 delay(10000);    
}

