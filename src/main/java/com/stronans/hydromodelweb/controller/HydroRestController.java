package com.stronans.hydromodelweb.controller;

import com.stronans.hydromodelweb.devices.WS2812BNeoPixels;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

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

    private final WS2812BNeoPixels provider = new WS2812BNeoPixels();

    public HydroRestController() {
    }

    @RequestMapping("/colour")
    public void colourLightsSequence() {
        process(COLOUR);
    }

    @RequestMapping("/purple")
    public void purpleLightsSequence() {
        process(PURPLE);
    }

    @RequestMapping("/cyan")
    public void cyanLightsSequence() {
        process(CYAN);
    }

    @RequestMapping("/greenblue")
    public void greenBlueLightsSequence() {
        process(BLUE_GREEN);
    }

    @RequestMapping("/redblue")
    public void redBlueLightsSequence() {
        process(RED_BLUE);
    }

    @RequestMapping("/off")
    public void allOffSequence() {
        process(OFF);
    }

    @RequestMapping("/shutdown")
    public void shutdownApplication() {
        try {
            Runtime.getRuntime().exec("sudo shutdown -h -P now");
        } catch (IOException ioe) {
            log.error(" ==>> PROBLEMS WITH CALLING SUDO SHUTDOWN: " + ioe.getMessage(), ioe);
        }
    }

    @RequestMapping("/wild")
    public void redWildColourSequence() {
        process(WILD);
    }

    private void process(String sequence) {
        provider.sendSequence(sequence);
    }
}
