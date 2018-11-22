package com.stronans.hydromodelweb.controller;

import com.stronans.hydromodelweb.colours.ColourSet;
import com.stronans.hydromodelweb.devices.WS2812BNeoPixels;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Component
@RestController
@RequestMapping("/hydromodel/api")
public class HydroRestController {
    /**
     * The <code>Logger</code> to be used.
     */
    private static Logger log = LogManager.getLogger(HydroRestController.class);
    private static final String RED_BLUE = "RB";
    private static final String BLUE_GREEN = "BG";
    private static final String CYAN = "CY";
    private static final String PURPLE = "PU";
    private static final String COLOUR = "CO";
    private static final String WILD = "WI";
    private static final String OFF = "OF";

    //    private static final int NUM_LEDS = 16;
    //    private final PCA9685GpioRGBLEDs provider = new PCA9685GpioRGBLEDs();
    private final WS2812BNeoPixels provider = new WS2812BNeoPixels();
    private volatile static boolean inSequence;
    //    private List<ColourSet> fullColour, purpleColour, cyanColour, greenBlueColour, redBlueColour;
    private final static Object syncVar = new Object();

    public HydroRestController() {
        this.inSequence = false;
//        fullColour = setFullColour();
//        purpleColour = setPurpleColour();
//        cyanColour = setCyanColour();
//        greenBlueColour = setGreenBlueColour();
//        redBlueColour = setRedBlueColour();
    }

    @RequestMapping("/colour")
    public void colourLightsSequence() {
//        process(fullColour);
        process(COLOUR);
    }

    @RequestMapping("/purple")
    public void purpleLightsSequence() {
//        process(purpleColour);
        process(PURPLE);
    }

    @RequestMapping("/cyan")
    public void cyanLightsSequence() {
//        process(cyanColour);
        process(CYAN);
    }

    @RequestMapping("/greenblue")
    public void greenBlueLightsSequence() {
//        process(greenBlueColour);
        process(BLUE_GREEN);
    }

    @RequestMapping("/redblue")
    public void redBlueLightsSequence() {
//        process(redBlueColour);
        process(RED_BLUE);
    }

    @RequestMapping("/off")
    public void allOffSequence() {
//        process(redBlueColour);
        process(OFF);
    }

    @RequestMapping("/wild")
    public void redWildColourSequence() {
//        process(redBlueColour);
        process(WILD);
    }

//    private void increment(List<ColourSet> colours) {
//        int index = 0;
//        ColourSet colour;
//
//        for (int i = 0; i < NUM_LEDS; i++) {
//            colour = colours.get(index);
//            provider.setLED(i, colour, FULL_ON);
//
//            if (index == colours.size() - 1) {
//                index = 0;
//            } else {
//                index++;
//            }
//        }
//    }
//
//    private void decrement(List<ColourSet> colours) {
//        int index = colours.size() - 1;
//        ColourSet colour;
//
//        for (int i = 0; i < NUM_LEDS; i++) {
//            colour = colours.get(index);
//            provider.setLED(i, colour, FULL_ON);
//
//            if (index == 0) {
//                index = colours.size() - 1;
//            } else {
//                index--;
//            }
//        }
//    }

    synchronized void openGate() {
        if (inSequence) {
            return;
        }
        synchronized (syncVar) {
            inSequence = true;
        }
    }

    private void process(String sequence) {
        provider.sendSequence(sequence);
    }
//    private void process(List<ColourSet> colours) {
//        openGate();
//        log.info("Starting lights sequence selected from REST");
//
//        int index = 0;
//        ColourSet colour;
//        boolean flipflop = true;
//
//        for (int j = 0; j < 5; j++) {
//            if (flipflop) {
//                increment(colours);
//                flipflop = false;
//            } else {
//                decrement(colours);
//                flipflop = true;
//            }
//            delaySeconds(10);
//        }
//
//        for (int i = 0; i < NUM_LEDS; i++) {
//            provider.setLEDOff(i);
//        }
//
//        log.info("Finished lights sequence");
//        inSequence = false;
//    }
//
//    private List<ColourSet> setFullColour() {
//        List<ColourSet> result = new ArrayList<>();
//
//        result.add(Blue);
//        result.add(Green);
//        result.add(Magenta);
//        result.add(Red);
//        result.add(Yellow);
////        result.add(ForestGreen);
////        result.add(Cyan);
////        result.add(LightBlue);
////        result.add(Purple);
////        result.add(DustyRose);
////        result.add(BananaYellow);
//
//        return result;
//    }
//
//    private List<ColourSet> setPurpleColour() {
//        List<ColourSet> result = new ArrayList<>();
//        result.add(Purple);
//        return result;
//    }
//
//    private List<ColourSet> setCyanColour() {
//        List<ColourSet> result = new ArrayList<>();
//        result.add(Cyan);
//        return result;
//    }
//
//    private List<ColourSet> setGreenBlueColour() {
//        List<ColourSet> result = new ArrayList<>();
//        result.add(Green);
//        result.add(Blue);
//        return result;
//    }
//
//    private List<ColourSet> setRedBlueColour() {
//        List<ColourSet> result = new ArrayList<>();
//        result.add(Red);
//        result.add(Blue);
//        return result;
//    }
}
