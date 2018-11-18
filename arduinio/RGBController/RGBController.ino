#include <FastLED.h>
#include "messages.h"

#define VERSION "1.0"
// Serial comms speed
#define SERIAL_SPEED 115200

#define FULL_ON 4095
#define HALF_ON 2048
#define QUARTER_ON 1024
#define EIGHTH_ON 512
#define OFF 0

#define RED_LED 0
#define BLUE_LED 1
#define GREEN_LED 2

// State machine position
#define RESTING 1
#define SETCOLOUR 2
#define SEQUENCE 3

// How many leds are in the strip?
#define NUM_LEDS 16

// Data pin that led data will be written out over
#define DATA_PIN 5

// Clock pin only needed for SPI based chipsets when not using hardware SPI
//#define CLOCK_PIN 8

// This is an array of leds.  One item for each led in your strip.
CRGB leds[NUM_LEDS];

const String MSG_HEADER = "{S ";
const int MSG_HEADER_SIZE = MSG_HEADER.length();

// Changing state of this machine
int state = RESTING;

// Message processor comming in from Rasp Pi
Messages *messages;

void setup()
{
  delay(2000);
  FastLED.addLeds<WS2812B, DATA_PIN, RGB>(leds, NUM_LEDS);

  Serial.begin(SERIAL_SPEED);
  Serial.print("RGB Controller version ");
  Serial.println(VERSION);

  messages = new Messages();

  clearLEDs();
}

void clearLEDs()
{
   for(int led = 0; led < NUM_LEDS; led++) {
      // Turn our current led to black
      leds[led] = CRGB::Black;
      // Show the leds all now set to black
      FastLED.show();
   }
}

void setLED(int LEDIndex, long colour, int brightness)
{
  int red = (colour & 0xFF0000) >> 16;
  int green = (colour & 0x00FF00) >> 8;
  int blue = (colour & 0x0000FF);

  leds[LEDIndex] = CRGB(red, green, blue);
}

long getLong(String info)
{
  long result = 0;
  char carray[20];

  //  Serial.println("info [" + info + "]");

  info.toCharArray(carray, sizeof(carray));
  result = atol(carray);

  return result;
}

static long values[3] = {0, 0, 0};

void processSequence(String sequence) {

  sequence.trim();

  for (int index = 0; index < 3; index++) {
    int nextSpace = sequence.indexOf(" ");
    String numb;
    if (nextSpace == -1) {  // No space found
      numb = sequence.substring(0);
    }
    else {
      numb = sequence.substring(0, nextSpace + 1);
    }
    values[index] = getLong(numb);
    sequence = sequence.substring(nextSpace + 1);
    //    Serial.println("sequence [" + sequence + "]");
  }

  //  for (int i = 0; i < 3; i++) {
  //    Serial.print("Value [");
  //    Serial.print(values[i]);
  //    Serial.println("]");
  //  }
}

int refreshCounter = 0;

void setSequence() {
  setLED((int)values[0], values[1], (int)values[2]);
}

int getNumber(String data)
{
  int result = 0;
  char carray[10];
  data.toCharArray(carray, sizeof(carray));
  result = atoi(carray);

  return result;
}

/* Has the other machine sent a message? */
int readStatus(int currentState)
{
  int result = currentState;

  String msg = messages->read(false);

  if (msg.length() > 0) {
    if ((msg.length() > MSG_HEADER_SIZE) && msg.startsWith(MSG_HEADER) && msg.endsWith("}")) {

      String body = msg.substring(MSG_HEADER_SIZE);
      body.trim();  // Trim off white space from both ends.

      //      Serial.println("Body = [" + body + "]");

      // Available status values coming from other machine
      if (body.startsWith("R")) {      // RESTING   (Normal state, does nothing)
        result = RESTING;
      }
      else if (body.startsWith("Q")) {      // SEQUENCE
        result = SEQUENCE;
      }
      else if (body.startsWith("S")) {      // SETCOLOUR
        String extra = body.substring(1);
        processSequence(extra);   // Pass in string minus the "Q"
        result = SETCOLOUR;
      }
    }
  }

  return result;
}

void loop()
{
  state = readStatus(state);

  switch (state)
  {
    case RESTING :
      break;

    case SETCOLOUR :
      setSequence();
      break;

    case SEQUENCE :
      break;
  }
}

/*
  SerialEvent occurs whenever a new data comes in the
  hardware serial RX.  This routine is run between each
  time loop() runs, so using delay inside loop can delay
  response.  Multiple bytes of data may be available.
*/
void serialEvent() {
  messages->anySerialEvent();
}
