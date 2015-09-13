/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bussimulation;

import java.util.List;
import java.util.Random;
import utils.AdminSchedule;
import utils.UserSchedule;

/**
 *
 * @author ab-admin
 */


public class BusSimulation {

    /**
     * @param args the command line arguments
     */
    private static int minhour;
    private static int maxhour;
    
    
    static{
        minhour = 7;
        maxhour = 20;
                
    }
    private BusConfig config;
    public static void main(String[] args) {
        
        // TODO code application logic here
    int randomhour;     //between minhour to maxhour
    int randommins;     //in multiples of 15.
    DatabaseUtil dbu=new DatabaseUtil();
    BusConfig.setDatabase(dbu);
    BusConfig.getValsFromDb();
    
//        BusConfig.alterNumBuses(12);
    
    
    //dbu.populateStudents(200,maxhour,minhour);
   //dbu.populateGaussianStudents(400, maxhour, minhour);
    
  //  dbu.getScheduleforaDay(DAYS.tTues);
    Scheduler sch=new Scheduler(dbu);
   //for(DAYS d:DAYS.values())
    //sch.createSchedule(d);
    sch.generateSchedule();
    List<AdminSchedule> ls = sch.viewAdminSchedule(DAYS.tSun);
    List<UserSchedule> lst = sch.viewUserSchedule("202");
    }
    
}
