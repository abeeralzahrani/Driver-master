package objects;

import java.sql.Date;
import java.sql.Time;

/**
 * Created by Najla AlHazzani on 11/19/2016.
 */

public class Complaint {

    private String Description;
    private Date dat;
    private Time time;

    //**************************************** Constructor(S) ▼▼▼▼▼

    public Complaint(String description, Date dat, Time time) {
        Description = description;
        this.dat = dat;
        this.time = time;
    }

    public Complaint(){}//default Constructor.

    //  **************************************** service method HERE ▼▼▼▼▼

    public boolean makeComplaint(Complaint c){
        // this method will make an SQL query to insert the complaint to the DB.
        return false;
    }

        /* methods related to the ADMIN , i don't see any point of implementing it here!
        * like:
        * listComplaints.
        * proccessComplaint
        * */


       //**************************************** SETTERS &&& GETTERS HERE ▼▼▼▼▼


}
