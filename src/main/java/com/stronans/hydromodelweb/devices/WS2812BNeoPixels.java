package com.stronans.hydromodelweb.devices;

import com.stronans.hydromodelweb.colours.ColourSet;
import com.stronans.hydromodelweb.comms.MessageListener;
import com.stronans.hydromodelweb.comms.SerialComms;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WS2812BNeoPixels implements MessageListener {
    /**
     * The <code>Logger</code> to be used.
     */
    private static Logger log = LogManager.getLogger(WS2812BNeoPixels.class);
    private static final String LED_NANO = "/dev/ttyUSB0";

    private static String NANO_MSG_HEADER = "{S ";
    private static int CONNECTION_SPEED = 115200;

    private static SerialComms LEDNano;

    public WS2812BNeoPixels() {
        try {
            // Setup communications with Nano devices
            LEDNano = new SerialComms(LED_NANO, CONNECTION_SPEED);
            LEDNano.addListener(this);
            LEDNano.startComms();
            LEDNano.sendMessage("REST");

            Runtime.getRuntime().addShutdownHook(new Thread() {
                                                     @Override
                                                     public void run() {
                                                         LEDNano.endComms();
                                                         log.info("Exiting program.");
                                                     }
                                                 }

            );

        } catch (InterruptedException e) {
            log.error(" ==>> PROBLEMS WITH SERIAL COMMUNICATIONS: " + e.getMessage(), e);
        }
    }

    public void setLED(int LEDKey, ColourSet colour, int brightness) {
        int colourVal = colour.getVal();
        String msg = "S " + LEDKey + " " + colourVal + " " + brightness;

//        log.debug("Set Msg : [" + msg + "]");

        LEDNano.sendMessage(msg);
    }

    public void setLEDOff(int LEDKey) {
        String msg = "S " + LEDKey + " " + 0 + " " + 0;

//        log.debug("Set Off Msg : [" + msg + "]");

        LEDNano.sendMessage(msg);
    }

    @Override
    public void messageReceived() {
        try {
            String message = "";
            if (!LEDNano.messages().isEmpty()) {
                message = LEDNano.messages().take();
                log.info("Msg from LED Nano :" + message);
            }
        } catch (InterruptedException e) {
            log.error(" Interrupt during sleep : " + e.getMessage(), e);
        }
    }
}
