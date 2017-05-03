package objects;

/**
 * Created by hp on 10/12/16.
 */

public class schedule {

    public int S_ID;
    public  String StartDate;
    public  String endDate;
    public  String time ;
    public  int bookedSeat;
    public  int montPrice;
    public  int dayPrice;

    public schedule(int s_ID, String time,String startDate, String endDate, int bookedSeat, int montPrice, int dayPrice) {


        S_ID = s_ID;
        StartDate = startDate;
        this.endDate = endDate;
        this.time = time;
        this.bookedSeat = bookedSeat;
        this.montPrice = montPrice;
        this.dayPrice = dayPrice;

    }

    public int getS_ID() {
        return S_ID;
    }

    public void setS_ID(int s_ID) {
        S_ID = s_ID;
    }

    public int getBookedSeat() {
        return bookedSeat;
    }

    public void setBookedSeat(int bookedSeat) {
        this.bookedSeat = bookedSeat;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getMontPrice() {
        return montPrice;
    }

    public void setMontPrice(int montPrice) {
        this.montPrice = montPrice;
    }

    public int getDayPrice() {
        return dayPrice;
    }

    public void setDayPrice(int dayPrice) {
        this.dayPrice = dayPrice;
    }
}
