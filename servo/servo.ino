/* Sweep
 by BARRAGAN <http://barraganstudio.com>
 This example code is in the public domain.

 modified 8 Nov 2013
 by Scott Fitzgerald
 http://www.arduino.cc/en/Tutorial/Sweep
*/

#include <Servo.h>

Servo myservo;  // create servo object to control a servo
// twelve servo objects can be created on most boards
int inPin = 13;     // pushbutton connected to digital pin 7
int pos = 90;    // variable to store the servo position
int val = 0;

void setup() {
  myservo.attach(9);  // attaches the servo on pin 9 to the servo object
  pinMode(inPin, INPUT);        // sets the digital pin 7 as input

}



void loop() {
  myservo.write(90);
  delay(1000);
  pos = 90;
  myservo.write(180);
  delay(1000);

//  while(true){
//    val = digitalRead(inPin);     // read the input pin
//    if(val == LOW){
//      delay(10);
//      val = digitalRead(inPin);
//      if(val == HIGH){
//        if(pos == 90){
//          myservo.write(180);
//          pos = 180;
//        }
//        else{
//          myservo.write(90);
//          pos = 90;
//        }
//
//      }
//    }
//  }
}

