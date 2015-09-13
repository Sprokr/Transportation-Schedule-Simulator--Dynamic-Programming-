/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package utils;

/**
 *
 * @author ab-admin
 */
public class UserSchedule {

    public String getBusid() {
        return busid;
    }

    public void setBusid(String busid) {
        this.busid = busid;
    }

    public String getBustime() {
        return bustime;
    }

    public void setBustime(String bustime) {
        this.bustime = bustime;
    }

    public int getStudseat() {
        return studseat;
    }

    public void setStudseat(int studseat) {
        this.studseat = studseat;
    }

    public String getDayofweek() {
        return dayofweek;
    }

    public void setDayofweek(String dayofweek) {
        this.dayofweek = dayofweek;
    }
    String busid;
    String bustime;
    int studseat;
    String dayofweek;
    
    
}

    
    

