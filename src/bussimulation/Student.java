/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bussimulation;

import utils.MyTime;

/**
 *
 * @author ab-admin
 */
public class Student {
private String name;
private String id;
private MyTime[] timeprefs;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MyTime[] getTimeprefs() {
        return timeprefs;
    }

    public void setTimeprefs(MyTime[] timeprefs) {
        this.timeprefs = timeprefs;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

public Student(String id, String name){
    this.id = id;
    this.name = name;
    timeprefs = new MyTime[7];
}
public void setATimePref(MyTime preference, int index){
    if(index >= 7)
        System.err.println("Error... index greater than 7");
    else{
       // System.out.println("time set");
        timeprefs[index] = preference;
    }   
    
}

    
}
