package com.swahilibox.mobile.vision.qrcode;


public class Contactfavsc {
    String id;
    String image;
    String body;
    String location;
    String campaign_description;
    String drive_date;
    String drive_time;

    public Contactfavsc() {
    }

    public String getCampaign() {
        return body;
    }
    public String getId() {
        return id;
    }
    public String getLocation() {
        return location;
    }
    public String getDescription() {
        return campaign_description;
    }

    public String getImage() {
        return image;
    }

    public String getReg_date() {
        return drive_date;
    }
    public String getTime() {
        return drive_time;
    }

}
