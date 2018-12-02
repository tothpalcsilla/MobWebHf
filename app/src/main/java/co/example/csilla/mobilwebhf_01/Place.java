package co.example.csilla.mobilwebhf_01;

import io.realm.RealmObject;

public class Place extends RealmObject {

    private String id;  //name+timestamp
    private String placeName;
    private String placeCoordinates;
    private String placeAddress;
    private String alarmDistance;

    public Place() {}

    public Place(String id, String placeName, String placeCoordinates, String placeAddress,
                 String alarmDistance){
        this.id = id;
        this.placeName = placeName;
        this.placeCoordinates = placeCoordinates;
        this.placeAddress = placeAddress;
        this.alarmDistance = alarmDistance;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getPlaceName() {
        return placeName;
    }
    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getPlaceCoordinates() {
        return placeCoordinates;
    }
    public void setPlaceCoordinates(String placeCoordinates) { this.placeCoordinates = placeCoordinates; }

    public String getAlarmDistance() {
        return alarmDistance;
    }
    public void setAlarmDistance(String alarmDistance) {
        this.alarmDistance = alarmDistance;
    }

    public String getPlaceAddress() { return placeAddress; }
    public void setPlaceAddress(String placeAddress) { this.placeAddress = placeAddress; }

    @Override
    public String toString(){
        return "Place{" +
                "Name=" + placeName + '\'' +
                ", Coordinates=" + placeCoordinates +
                ", Address=" + placeAddress +
                ", AlarmPlace=" + alarmDistance +
                "}";
    }


}
