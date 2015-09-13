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
public class preference {
    private String sid;
    private MyTime time;
    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public MyTime getTime() {
        return time;
    }

    public void setTime(MyTime time) {
        this.time = time;
    }
    

    public preference(String sid, String timestr) {
        this.sid = sid;
         this.time = new MyTime(timestr);
    }
    public String toString(){
        return sid+ ":"+ time.toString();
    }
    
}
