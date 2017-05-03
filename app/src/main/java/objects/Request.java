package objects;

import android.location.Location;

import java.sql.Date;
import java.sql.Time;

/**
 * Created by Najla AlHazzani on 11/19/2016.
 */

public class Request {

    private Date StartingDate;
    private Date EndingDate;
    private Time time;
    private Location PickupLoc;// or LatLng
    private Location DropoffLoc;// or LatLng
    private String State;


    //**************************************** Constructor(S) ▼▼▼▼▼
    public Request() {}//default Constructor.

    public Request(Date startingDate, Date endingDate, Time time, Location pickupLoc, Location dropoffLoc, String state) {
        StartingDate = startingDate;
        EndingDate = endingDate;
        this.time = time;
        PickupLoc = pickupLoc;
        DropoffLoc = dropoffLoc;
        State = state;
    }

    //  **************************************** service method HERE ▼▼▼▼▼

    public Request viewRequest(int R_ID){
        // this method will .....
        return null;
    }

    public Request[] getRequests(int S_ID){
        //this method will retreive list/array of request based on scheduleID.
        return null;
    }

    public boolean cancelSubscription(Request r){
        //this method will take request and delete it from DB.
    return false;
    }

    public void setMeAbsent(int R_ID, Date d, Time t){
        //this method will take request id, date and time , and set the passenger absent.

    }

    public void ConfirmRequest(int R_ID){
        //this method will be used by driver to confirm the request and make change to DB.
    }

    //**************************************** SETTERS &&& GETTERS HERE ▼▼▼▼▼
}
