package cc.shrestha.assignment1;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

public class MeasurementItem {
    private String date;
    private String time;
    private int systolic_pressure;
    private int diastolic_pressure;
    private int heart_rate;
    private String comment;

    public MeasurementItem(String date, String time, int systolic_pressure, int diastolic_pressure, int heart_rate, String comment) {
        this.date = date;
        this.time = time;
        this.systolic_pressure = systolic_pressure;
        this.diastolic_pressure = diastolic_pressure;
        this.heart_rate = heart_rate;
        this.comment = comment;
    }
    public String getDate(){ return date;}
    public String getTime(){ return time;}
    public int getSystolicPressure(){
        return systolic_pressure;
    }
    public int getDiastolicPressure(){
        return diastolic_pressure;
    }
    public int getHeartRate(){
        return heart_rate;
    }
    public String getComment(){
        return comment;
    }

    public void newDate(String date){
        this.date = date;
    }
    public void newTime(String time){
        this.time = time;
    }
    public void newHeartRate(int heart_rate){
        this.heart_rate = heart_rate;
    }
    public void newSystolicPressure(int systolic_pressure){
        this.systolic_pressure = systolic_pressure;
    }
    public void newDiastolicPressure(int diastolic_pressure){
        this.diastolic_pressure = diastolic_pressure;
    }
    public void newComment(String comment){
        this.comment = comment;
    }

}
