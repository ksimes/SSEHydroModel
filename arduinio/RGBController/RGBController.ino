#include <FastLED.h>
#include "messages.h"

#define VERSION "1.0"
// Serial comms speed
#define SERIAL_SPEED 115200

#define FULL_ON 255
#define HALF_ON 128
#define QUARTER_ON 64
#define EIGHTH_ON 32
#define OFF 0

// #define BRIGHTNESS  64
#define BRIGHTNESS  128

#define LED_TYPE    WS2812B
#define COLOR_ORDER GRB
// How many leds are in the strip?
#define NUM_LEDS 16

// Data pin that led data will be written out over
#define DATA_PIN 5

// Clock pin only needed for SPI based chipsets when not using hardware SPI
//#define CLOCK_PIN 8

CRGBPalette16 currentPalette;
TBlendType    currentBlending;

// This is an array of leds.  One item for each led in your strip.
CRGB leds[NUM_LEDS];

const String MSG_HEADER = "{S ";
const int MSG_HEADER_SIZE = MSG_HEADER.length();

// Changing state of this machine
boolean state = true;
boolean showing = false;
int count = 0;
boolean wild = false;
static uint8_t startIndex = 0;

// Message processor comming in from Rasp Pi
Messages *messages;

void setup()
{
  delay(2000);
  FastLED.addLeds<LED_TYPE, DATA_PIN, COLOR_ORDER>(leds, NUM_LEDS);

  Serial.begin(SERIAL_SPEED);
  Serial.print("RGB Controller version ");
  Serial.println(VERSION);

  messages = new Messages();

  clearLEDs();
}

void setTwoTone(CRGB first, CRGB second) {
  currentPalette = CRGBPalette16(
                     first, second, first,  second,
                     first, second, first,  second,
                     first, second, first,  second,
                     first, second, first,  second );
}

void setColourPalette() {

  currentPalette = CRGBPalette16(
                     CRGB::Olive, CRGB::YellowGreen, CRGB::Silver,  CRGB::Ivory,
                     CRGB::Magenta, CRGB::Purple, CRGB::White,  CRGB::Red,
                     CRGB::Blue, CRGB::Orange, CRGB::Yellow,  CRGB::Green,
                     CRGB::Navy, CRGB::DarkRed, CRGB::SaddleBrown,  CRGB::Teal );
}

void setRedBluePalette()
{
  setTwoTone(CRGB::Blue, CRGB::Red);
}

void setBlueGreenPalette()
{
  setTwoTone(CRGB::Blue, CRGB::Green);
}

void setPurplePalette()
{
  setTwoTone(CRGB::Indigo, CRGB::Purple);
}

void setCyanPalette()
{
  setTwoTone(CRGB::Cyan, CRGB::LightSkyBlue);
}

// This function fills the palette with totally random colors.
void setTotallyRandomPalette()
{
  for ( int i = 0; i < 16; i++) {
    currentPalette[i] = CHSV( random8(), 255, random8());
  }
}

void clearLEDs()
{
  //    fill_solid( currentPalette, 16, CRGB::Black);
  for (int led = 0; led < NUM_LEDS; led++) {
    // Turn our current led to black
    leds[led] = CRGB::Black;
  }
  // Show the leds all now set to black
  FastLED.show();
}

/* Has the other machine sent a message? */
void processMessage()
{
  String msg = messages->read(false);

  if (msg.length() > 0) {
    if ((msg.length() > MSG_HEADER_SIZE) && msg.startsWith(MSG_HEADER) && msg.endsWith("}")) {

      //      Serial.println("msg = [" + msg + "]");

      String body = msg.substring(MSG_HEADER_SIZE);
      body.trim();  // Trim off white space from both ends.

      //      Serial.println("Body = [" + body + "]");

      // Available status values coming from other machine
      if (body.startsWith("RB")) {    // RedBlue
        showing = true;
        count = 5;
        setRedBluePalette();
      }
      else if (body.startsWith("BG")) {      // BlueGreen
        showing = true;
        count = 5;
        setBlueGreenPalette();
      }
      else if (body.startsWith("PU")) {      // Purple
        showing = true;
        count = 5;
        setPurplePalette();
      }
      else if (body.startsWith("CY")) {      // Cyan
        showing = true;
        count = 5;
        setCyanPalette();
      }
      else if (body.startsWith("CO")) {      // Colour
        showing = true;
        count = 5;
        setColourPalette();
      }
      else if (body.startsWith("OF")) {      // OFF
        showing = false;
        wild = false;
        clearLEDs();
      }
      else if (body.startsWith("WI")) {      // Wild
        showing = true;
        count = 1000;
        setTotallyRandomPalette();
        wild = true;
      }

      if (showing) {
        Serial.println("Ack");
      }
    }
  }
}

void FillLEDsFromPaletteColorsInc()
{
  for ( int i = 0; i < NUM_LEDS; i++) {
    leds[i] = currentPalette.entries[i];
  }
}

void FillLEDsFromPaletteColorsDec()
{
  int paletteSize = 15;
  for ( int i = 0; i < NUM_LEDS; i++) {
    leds[i] = currentPalette.entries[paletteSize--];
  }
}
void FillLEDsFromPaletteColorsWild( uint8_t colorIndex)
{
  uint8_t brightness = 255;

  for ( int i = 0; i < NUM_LEDS; i++) {
    leds[i] = ColorFromPalette( currentPalette, colorIndex, brightness, LINEARBLEND);
    colorIndex += 3;
  }
}

void loop()
{
  messages->anySerialEvent();
  processMessage();

  if (showing) {
    if (count > 0) {
      if (wild) {
        startIndex = startIndex + 1; /* motion speed */
        setTotallyRandomPalette();
        FillLEDsFromPaletteColorsWild(startIndex);

      } else {

        if (state) {
          FillLEDsFromPaletteColorsInc();
        } else {
          FillLEDsFromPaletteColorsDec();
        }
      }

      FastLED.show();
      
      if (!wild) {
        delay(5000);
      } else {
        delay(500);
      }
      
      state = !state;
      count--;
    } else {
      clearLEDs();
      showing = false;
      wild = false;
    }
  }
}


