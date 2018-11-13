package com.stronans.hydromodelweb.pca9685;

import com.pi4j.component.light.LED;
import com.pi4j.gpio.extension.pca.PCA9685GpioProvider;
import com.pi4j.gpio.extension.pca.PCA9685Pin;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinPwmOutput;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CFactory;
import com.stronans.hydromodelweb.colours.ColourSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class PCA9685GpioRGBLEDs {
    public static final int LED1 = 0;
    public static final int LED2 = 1;
    public static final int LED3 = 2;
    public static final int LED4 = 3;
    public static final int LED5 = 4;
    public static final int LED6 = 5;
    public static final int LED7 = 6;
    public static final int LED8 = 7;
    public static final int LED9 = 8;
    public static final int LED10 = 9;

    public static final int FULL_ON = 4095;
    public static final int HALF_ON = 2048;
    public static final int QUARTER_ON = 1024;
    public static final int EIGHTH_ON = 512;
    public static final int OFF = 0;

    private static final int RED_LED = 0;
    private static final int BLUE_LED = 1;
    private static final int GREEN_LED = 2;

    private Map<Integer, PinDetails> mapper = new HashMap<>();
    /**
     * The <code>Logger</code> to be used.
     */
    private static Logger log = LogManager.getLogger(PCA9685GpioRGBLEDs.class);
    //------------------------------------------------------------------------------------------------------------------
    // PCA9685GpioProvider
    //------------------------------------------------------------------------------------------------------------------
    private PCA9685GpioProvider gpioProvider1, gpioProvider2;

    public PCA9685GpioRGBLEDs() {

        try {
            gpioProvider1 = createProvider(0x40);
            gpioProvider2 = createProvider(0x41);
        } catch (I2CFactory.UnsupportedBusNumberException e) {
        } catch (IOException e) {
        }

        // Define outputs in use for this example
        GpioPinPwmOutput[] myOutputs = provisionPwmOutputs();

        // Reset outputs
        gpioProvider1.reset();
        gpioProvider2.reset();

        // Set pin mappings
        mapper.put(0, new PinDetails(0, gpioProvider1));
        mapper.put(1, new PinDetails(3, gpioProvider1));
        mapper.put(2, new PinDetails(6, gpioProvider1));
        mapper.put(3, new PinDetails(9, gpioProvider1));
        mapper.put(4, new PinDetails(12, gpioProvider1));
        mapper.put(5, new PinDetails(0, gpioProvider2));
        mapper.put(6, new PinDetails(3, gpioProvider2));
        mapper.put(7, new PinDetails(6, gpioProvider2));
        mapper.put(8, new PinDetails(9, gpioProvider2));
        mapper.put(9, new PinDetails(12, gpioProvider2));

        //
        // Show PWM values for outputs 0..14
//        for (GpioPinPwmOutput output : myOutputs) {
//            int[] onOffValues = gpioProvider.getPwmOnOffValues(output.getPin());
//            log.info(output.getPin().getName() + " (" + output.getName() + "): ON value [" + onOffValues[0] + "], OFF value [" + onOffValues[1] + "]");
//        }
    }

    private int map(int x, int in_max, int out_max) {
        return x * out_max / in_max;
    }

    private void setLedToColour(PinDetails actualLED, int colour, int brightness, int offset) {
        if (colour == 0) {
            actualLED.getProvider().setAlwaysOff(PCA9685Pin.ALL[actualLED.getActualPin() + offset]);       // Set full OFF
        } else {
            actualLED.getProvider().setPwm(PCA9685Pin.ALL[actualLED.getActualPin() + offset],
                    map(colour, 0xff, brightness));
        }
    }

    public void setLED(int LEDKey, ColourSet colour, int brightness) {
        int colourVal = colour.getVal();
        int red = (int) (colourVal & 0xFF0000) >> 16;
        int green = (int) (colourVal & 0x00FF00) >> 8;
        int blue = (int) (colourVal & 0x0000FF);

        log.debug("red = {}, green = {}, blue = {}", red, green, blue);

        log.debug("mapRed = {}, mapGreen = {}, mapBlue = {}",
                map(red, 0xff, brightness),
                map(green, 0xff, brightness),
                map(blue, 0xff, brightness));

        PinDetails LED = mapper.get(LEDKey);

        setLedToColour(LED, red, brightness, RED_LED);
        setLedToColour(LED, green, brightness, GREEN_LED);
        setLedToColour(LED, blue, brightness, BLUE_LED);
    }

    public void setLEDOff(int LEDKey) {
        PinDetails actualLED = mapper.get(LEDKey);
        int actualPin = actualLED.getActualPin();
        PCA9685GpioProvider provider = actualLED.getProvider();

        provider.setAlwaysOff(PCA9685Pin.ALL[actualPin + RED_LED]);       // Set full OFF
        provider.setAlwaysOff(PCA9685Pin.ALL[actualPin + GREEN_LED]);       // Set full OFF
        provider.setAlwaysOff(PCA9685Pin.ALL[actualPin + BLUE_LED]);       // Set full OFF
    }

    //------------------------------------------------------------------------------------------------------------------
    // Helpers
    //------------------------------------------------------------------------------------------------------------------
    private PCA9685GpioProvider createProvider(int address) throws I2CFactory.UnsupportedBusNumberException, IOException {

//        BigDecimal frequency = PCA9685GpioProvider.ANALOG_SERVO_FREQUENCY;
        BigDecimal frequency = PCA9685GpioProvider.DIGITAL_SERVO_FREQUENCY;
//        BigDecimal frequency = PCA9685GpioProvider.MAX_FREQUENCY;
        BigDecimal frequencyCorrectionFactor = new BigDecimal("1.0578");

        I2CBus bus = I2CFactory.getInstance(I2CBus.BUS_1);
        return new PCA9685GpioProvider(bus, address, frequency, frequencyCorrectionFactor);
    }

    private GpioPinPwmOutput[] provisionPwmOutputs() {
        GpioController gpio = GpioFactory.getInstance();
        GpioPinPwmOutput myOutputs[] = {
                gpio.provisionPwmOutputPin(gpioProvider1, PCA9685Pin.PWM_00, "Servo 00"),
                gpio.provisionPwmOutputPin(gpioProvider1, PCA9685Pin.PWM_01, "Servo 01"),
                gpio.provisionPwmOutputPin(gpioProvider1, PCA9685Pin.PWM_02, "Servo 02"),
                gpio.provisionPwmOutputPin(gpioProvider1, PCA9685Pin.PWM_03, "Servo 03"),
                gpio.provisionPwmOutputPin(gpioProvider1, PCA9685Pin.PWM_04, "Servo 04"),
                gpio.provisionPwmOutputPin(gpioProvider1, PCA9685Pin.PWM_05, "Servo 05"),
                gpio.provisionPwmOutputPin(gpioProvider1, PCA9685Pin.PWM_06, "Servo 06"),
                gpio.provisionPwmOutputPin(gpioProvider1, PCA9685Pin.PWM_07, "Servo 07"),
                gpio.provisionPwmOutputPin(gpioProvider1, PCA9685Pin.PWM_08, "Servo 08"),
                gpio.provisionPwmOutputPin(gpioProvider1, PCA9685Pin.PWM_09, "Servo 09"),
                gpio.provisionPwmOutputPin(gpioProvider1, PCA9685Pin.PWM_10, "Servo 10"),
                gpio.provisionPwmOutputPin(gpioProvider1, PCA9685Pin.PWM_11, "Servo 11"),
                gpio.provisionPwmOutputPin(gpioProvider1, PCA9685Pin.PWM_12, "Servo 12"),
                gpio.provisionPwmOutputPin(gpioProvider1, PCA9685Pin.PWM_13, "Servo 13"),
                gpio.provisionPwmOutputPin(gpioProvider1, PCA9685Pin.PWM_14, "Servo 14"),
                gpio.provisionPwmOutputPin(gpioProvider1, PCA9685Pin.PWM_15, "Servo 15"),

                gpio.provisionPwmOutputPin(gpioProvider2, PCA9685Pin.PWM_00, "Servo 16"),
                gpio.provisionPwmOutputPin(gpioProvider2, PCA9685Pin.PWM_01, "Servo 17"),
                gpio.provisionPwmOutputPin(gpioProvider2, PCA9685Pin.PWM_02, "Servo 18"),
                gpio.provisionPwmOutputPin(gpioProvider2, PCA9685Pin.PWM_03, "Servo 19"),
                gpio.provisionPwmOutputPin(gpioProvider2, PCA9685Pin.PWM_04, "Servo 20"),
                gpio.provisionPwmOutputPin(gpioProvider2, PCA9685Pin.PWM_05, "Servo 21"),
                gpio.provisionPwmOutputPin(gpioProvider2, PCA9685Pin.PWM_06, "Servo 22"),
                gpio.provisionPwmOutputPin(gpioProvider2, PCA9685Pin.PWM_07, "Servo 23"),
                gpio.provisionPwmOutputPin(gpioProvider2, PCA9685Pin.PWM_08, "Servo 24"),
                gpio.provisionPwmOutputPin(gpioProvider2, PCA9685Pin.PWM_09, "Servo 25"),
                gpio.provisionPwmOutputPin(gpioProvider2, PCA9685Pin.PWM_10, "Servo 26"),
                gpio.provisionPwmOutputPin(gpioProvider2, PCA9685Pin.PWM_11, "Servo 27"),
                gpio.provisionPwmOutputPin(gpioProvider2, PCA9685Pin.PWM_12, "Servo 28"),
                gpio.provisionPwmOutputPin(gpioProvider2, PCA9685Pin.PWM_13, "Servo 29"),
                gpio.provisionPwmOutputPin(gpioProvider2, PCA9685Pin.PWM_14, "Servo 30"),
                gpio.provisionPwmOutputPin(gpioProvider2, PCA9685Pin.PWM_15, "Servo 31")};
        return myOutputs;
    }

    private static class PinDetails {
        private final int actualPin;
        private final PCA9685GpioProvider provider;

        PinDetails(int actualPin, PCA9685GpioProvider provider) {
            this.actualPin = actualPin;
            this.provider = provider;
        }

        int getActualPin() {
            return actualPin;
        }

        PCA9685GpioProvider getProvider() {
            return provider;
        }
    }
}
