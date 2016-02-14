package com.studioidan.turaco.entities;

import java.io.Serializable;

/**
 * Created by PopApp_laptop on 26/11/2015.
 */
public class PushLog implements Serializable {
    public String date;
    public String opCode;

    public PushLog() {
    }

    public PushLog(String date, String opCode) {
        this.date = date;
        this.opCode = opCode;
    }
}
