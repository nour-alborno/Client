package com.example.client.Model;

public class ArichivedJourney {

      String driver ;
        String region ;
        String start ;
        String end ;
        String organization ;
        String date;
        String journeyId ;

        String driveProfileImg;
        String driverName;


    public ArichivedJourney(String driver, String region, String start, String end, String organization, String date, String journeyId) {
        this.driver = driver;
        this.region = region;
        this.start = start;
        this.end = end;
        this.organization = organization;
        this.date = date;
        this.journeyId = journeyId;
    }

    public ArichivedJourney() {
    }

    public ArichivedJourney(String driver, String region, String start, String end, String organization, String date, String journeyId, String driveProfileImg, String driverName) {
        this.driver = driver;
        this.region = region;
        this.start = start;
        this.end = end;
        this.organization = organization;
        this.date = date;
        this.journeyId = journeyId;
        this.driveProfileImg = driveProfileImg;
        this.driverName = driverName;
    }

    public String getDriveProfileImg() {
        return driveProfileImg;
    }

    public void setDriveProfileImg(String driveProfileImg) {
        this.driveProfileImg = driveProfileImg;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }



    public String getJourneyId() {
        return journeyId;
    }

    public void setJourneyId(String journeyId) {
        this.journeyId = journeyId;
    }
}
