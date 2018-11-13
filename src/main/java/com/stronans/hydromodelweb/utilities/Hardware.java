package com.stronans.hydromodelweb.utilities;

import static java.lang.Thread.sleep;

public class Hardware {

    static public void delaySeconds(final int seconds) {
        try {
            sleep(seconds * 1000);   // 1000 milliseconds is one second.
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}
