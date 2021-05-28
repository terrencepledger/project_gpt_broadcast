package org.gptchurch.broadcast.model;

import java.text.DateFormat;
import java.util.Date;

public class SermonLog {

    public Sermon sermon;
    public Date sentDate;

    public SermonLog(Sermon sermon, Date sentDate) {
        this.sermon = sermon;
        this.sentDate = sentDate;
    }

    public String getPrettyDate() {
        return DateFormat.getDateInstance().format(sentDate);
    }

}
