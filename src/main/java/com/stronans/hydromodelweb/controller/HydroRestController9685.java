package com.stronans.hydromodelweb.controller;

import com.stronans.hydromodelweb.colours.ColourSet;
import com.stronans.hydromodelweb.devices.PCA9685GpioRGBLEDs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

import static com.stronans.hydromodelweb.colours.ColourSet.*;
import static com.stronans.hydromodelweb.devices.PCA9685GpioRGBLEDs.FULL_ON;
import static com.stronans.hydromodelweb.utilities.Hardware.delaySeconds;

// @Component
//@RestController
//@RequestMapping("/hydromodel/api")
public class HydroRestController9685 {
    /**
     * The <code>Logger</code> to be used.
     */
    private static Logger log = LogManager.getLogger(HydroRestController9685.class);

    private static final int NUM_LEDS = 16;
    private final PCA9685GpioRGBLEDs provider = new PCA9685GpioRGBLEDs();
    private volatile static boolean inSequence;
    private List<ColourSet> fullColour, purpleColour, cyanColour, greenBlueColour, redBlueColour;
    private final static Object syncVar = new Object();

    public HydroRestController9685() {
        this.inSequence = false;
        fullColour = setFullColour();
        purpleColour = setPurpleColour();
        cyanColour = setCyanColour();
        greenBlueColour = setGreenBlueColour();
        redBlueColour = setRedBlueColour();
    }

    @RequestMapping("/colour")
    public void colourLightsSequence() {
        process(fullColour);
    }

    @RequestMapping("/purple")
    public void purpleLightsSequence() {
        process(purpleColour);
    }

    @RequestMapping("/cyan")
    public void cyanLightsSequence() {
        process(cyanColour);
    }

    @RequestMapping("/greenblue")
    public void greenBlueLightsSequence() {
        process(greenBlueColour);
    }

    @RequestMapping("/redblue")
    public void redBlueLightsSequence() {
        process(redBlueColour);
    }

    @RequestMapping("/off")
    public void allOffSequence() {
        process(redBlueColour);
    }

    @RequestMapping("/wild")
    public void redWildColourSequence() {
        process(redBlueColour);
    }

    private void increment(List<ColourSet> colours) {
        int index = 0;
        ColourSet colour;

        for (int i = 0; i < NUM_LEDS; i++) {
            colour = colours.get(index);
            provider.setLED(i, colour, FULL_ON);

            if (index == colours.size() - 1) {
                index = 0;
            } else {
                index++;
            }
        }
    }

    private void decrement(List<ColourSet> colours) {
        int index = colours.size() - 1;
        ColourSet colour;

        for (int i = 0; i < NUM_LEDS; i++) {
            colour = colours.get(index);
            provider.setLED(i, colour, FULL_ON);

            if (index == 0) {
                index = colours.size() - 1;
            } else {
                index--;
            }
        }
    }

    synchronized void openGate() {
        if (inSequence) {
            return;
        }
        synchronized (syncVar) {
            inSequence = true;
        }
    }

    private void process(List<ColourSet> colours) {
        openGate();
        log.info("Starting lights sequence selected from REST");

        int index = 0;
        ColourSet colour;
        boolean flipflop = true;

        if (flipflop) {
            increment(colours);
            flipflop = false;
        } else {
            decrement(colours);
            flipflop = true;
        }
        delaySeconds(10);
        if (flipflop) {
            increment(colours);
            flipflop = false;
        } else {
            decrement(colours);
            flipflop = true;
        }
        delaySeconds(10);
        if (flipflop) {
            increment(colours);
            flipflop = false;
        } else {
            decrement(colours);
            flipflop = true;
        }
        delaySeconds(10);
        if (flipflop) {
            increment(colours);
            flipflop = false;
        } else {
            decrement(colours);
            flipflop = true;
        }
        delaySeconds(10);
        if (flipflop) {
            increment(colours);
            flipflop = false;
        } else {
            decrement(colours);
            flipflop = true;
        }
        delaySeconds(10);

        for (int i = 0; i < NUM_LEDS; i++) {
            provider.setLEDOff(i);
        }

        log.info("Finished lights sequence");
        inSequence = false;
    }

    private List<ColourSet> setFullColour() {
        List<ColourSet> result = new ArrayList<>();

        result.add(Blue);
        result.add(Green);
        result.add(Magenta);
        result.add(Red);
        result.add(Yellow);

        return result;
    }

    private List<ColourSet> setPurpleColour() {
        List<ColourSet> result = new ArrayList<>();
        result.add(Purple);
        return result;
    }

    private List<ColourSet> setCyanColour() {
        List<ColourSet> result = new ArrayList<>();
        result.add(Cyan);
        return result;
    }

    private List<ColourSet> setGreenBlueColour() {
        List<ColourSet> result = new ArrayList<>();
        result.add(Green);
        result.add(Blue);
        return result;
    }

    private List<ColourSet> setRedBlueColour() {
        List<ColourSet> result = new ArrayList<>();
        result.add(Red);
        result.add(Blue);
        return result;
    }
}
