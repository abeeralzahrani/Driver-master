package objects;

import java.sql.Date;
import java.sql.Time;

/**
 * Created by Najla AlHazzani on 11/19/2016.
 */

public class Comment {

    private String Description;
    private Date dat;
    private Time time;


    //**************************************** Constructor(S) ▼▼▼▼▼

    public Comment(String description, Date dat, Time time) {
        Description = description;
        this.dat = dat;
        this.time = time;
    }

    public Comment(){}//default Constructor.

    //  **************************************** service method HERE ▼▼▼▼▼

    public Comment viewComment(int D_ID){
        // this method will view all comments written in the driver's profile.
        return null;
    }

    public void provideComment(Comment c){
        //this method will insert comment c in the DB.

    }

        /*
    *  **************************************** SETTERS &&& GETTERS HERE ▼▼▼▼▼
    * */
}
