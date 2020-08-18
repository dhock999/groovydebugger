package com.boomi.execution;

import java.io.PrintStream;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;

public class CustomStreamHandler extends StreamHandler {

    private Level maxlevel = Level.SEVERE;  // by default, put out everything

    /**
     * The only method we really change to check whether the message
     * is smaller than maxlevel.
     * We also flush here to make sure that the message is shown immediately.
     */
    @Override
    public synchronized void publish(LogRecord record) {
        if (record.getLevel().intValue() > this.maxlevel.intValue()) {
            // do nothing if the level is above maxlevel
        } else {
            // if we arrived here, do what we always do
            super.publish(record);
            super.flush();
        }
    }

    /**
     * getter for maxlevel
     * @return
     */
    public Level getMaxlevel() {
        return maxlevel;
    }

    /**
     * Setter for maxlevel. 
     * If a logging event is larger than this level, it won't be displayed
     * @param maxlevel
     */
    public void setMaxlevel(Level maxlevel) {
        this.maxlevel = maxlevel;
    }

    /** Constructor forwarding */
    public CustomStreamHandler(PrintStream out, Formatter formatter) {
        super(out, formatter);
    }

    /** Constructor forwarding */
    public CustomStreamHandler() {
        super();
    }
}