package com.ensharp.kimyejin.voicerecognitiontest;

import java.util.ArrayList;
import java.util.List;

public class InformationVO {
    private static final InformationVO ourInstance = new InformationVO();

    public static InformationVO getInstance() {
        return ourInstance;
    }

    private String departure;
    private String destination;
    private String destinationTag;
    private int destinationCode;
    private List<String> temporaryDestinations = new ArrayList<String>();
    private String intend;
    private int countOfCompletedGuid;

    private InformationVO() {
        clearAllInformations();
    }

    public void setInformations(String departure, String destination, String intend) {
        this.departure = departure;
        this.destination = destination;
        this.intend = intend;
    }

    public void setDetail(String tag, int code) {
        destinationTag = tag;
        destinationCode = code;
    }

    public String getDeparture() {return departure;}
    public String getDestination() {return destination;}
    public String getIntend() {return intend;}
    public List<String> getTemporaryDestinations() {return temporaryDestinations;}
    public String getTemporaryDestination(int i) {return temporaryDestinations.get(i);}
    public String getTemporaryDestination() {return temporaryDestinations.get(countOfCompletedGuid);}
    public int getCountOfCompletedGuid() {return countOfCompletedGuid;}
    public String getTag() {return destinationTag;}
    public int getCode() {return destinationCode;}

    public void setDeparture(String departure) {this.departure = departure;}
    public void setDestination(String destination) {this.destination = destination;}
    public void setIntend(String intend) {this.intend = intend;}
    public void setTemporaryDestinations(List<String> temporaryDestinations) {this.temporaryDestinations = temporaryDestinations;}
    public void addCompletedGuideCount() {countOfCompletedGuid++;}

    public void clearAllInformations() {
        departure = "";
        destination = "";
        intend = "";
        countOfCompletedGuid = 0;
    }
}
