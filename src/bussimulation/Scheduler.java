/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bussimulation;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.AdminSchedule;
import utils.MyTime;
import utils.PrefComp;
import utils.TimeComp;
import utils.UserSchedule;
import utils.preference;

/**
 *
 * @author ab-admin
 */
public class Scheduler {
private int numbuses;
private int tolerance;
private int capacity;
private List<preference> preflist;
private static DatabaseUtil db;

public Scheduler(DatabaseUtil dbu){
    numbuses = BusConfig.getNumbuses();
    tolerance = BusConfig.getTolerance();
    capacity = BusConfig.getBuscapacity();
    preflist = null;
    Scheduler.db = dbu;
}
private void deleteprevious()
{
    String delString = "delete from schedule";
    try {
        db.getUpdateResults(delString);
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
}
public void generateSchedule(){
//Delete previously existing schedule..
    deleteprevious();
    
    
    
    for(DAYS thisday:DAYS.values())
        createSchedule(thisday);
}
    private void createSchedule(DAYS day)
    {
     
        
        preflist = db.getScheduleforaDay(day);
       // Collections.sort(preflist,new PrefComp());
        String earliest = preflist.get(0).getTime().toString();
        System.out.println("Start a bus at "+earliest);
        List<preference> studlist;
        int index = 0;
        int studstosend;
        int penalty =0;
        String[] busid =new String[BusConfig.getNumbuses()];
        for(int i=0;i<busid.length;i++)
            busid[i]= "B"+i;
        
        MyTime tolerance = new MyTime(BusConfig.getTolerance());
        
        
        for(int buscount=0; buscount < BusConfig.getNumbuses() && index< preflist.size(); buscount++){
            
            

                studlist = getuptotolerance(index,preflist,tolerance);
                studstosend =studlist.size();
                System.out.println("Valid students for bus num "+buscount+ "is " + studlist.size());
                float fillratio = studstosend/(float)BusConfig.getBuscapacity();
                System.out.println("Fill ratio = "+fillratio);


                if(fillratio < 0.5)
                {
                    //increase tolerance 30 mins;
                    //studlist.clear();
                    MyTime adjustedtol = MyTime.add(tolerance, new MyTime(30));
                    studlist = getuptotolerance(index, preflist, adjustedtol);
                    System.out.println("Valid students after adding 30 mins " + studlist.size());
                    int newsize =  studlist.size();
                    int extrastuds = newsize - studstosend;
                    System.out.println("Added"+extrastuds+"extra students.");
                    penalty += (3 * extrastuds);
                    studstosend = newsize;
                }
                else if(fillratio < 0.75)
                {
                    //increase tolerance 15mins;

                    MyTime adjustedtol = MyTime.add(tolerance, new MyTime(15));
                    studlist = getuptotolerance(index, preflist, adjustedtol);
                    //studstosend =studlist.size();
                    System.out.println("Valid students after adding 15 mins " + studlist.size());
                    int newsize =  studlist.size();
                    int extrastuds = newsize - studstosend;
                    System.out.println("Added"+extrastuds+"extra students.");
                    penalty += (2 * extrastuds);
                    studstosend = newsize;
                }

                System.out.println("Valid students for bus num "+buscount+" is " + studstosend);
                allocateBus(studlist,busid[buscount],day);
                index+=studstosend;
                
        }
        
        System.out.println("Penalty incurred "+penalty);
    
    
    }
    
private List<preference> getuptotolerance(int index,List<preference> prefList,MyTime consideredTolerance){
    List<preference> mylist = new ArrayList<>();
    
    MyTime starttime = prefList.get(index).getTime();
    //MyTime toltime = new MyTime(tolerance);
    
    boolean validtime = false;
    MyTime timobj;
    MyTime difference;
    
    TimeComp timecomparator = new TimeComp();
    do{
     timobj = prefList.get(index).getTime();
     difference = MyTime.subtract(timobj,consideredTolerance);
     
     if(timecomparator.compare(starttime,difference) >=0)
     {
//         System.out.println(timobj + " is valid. ");
         mylist.add(prefList.get(index));
         validtime = true;
         index++;
     }
     else
         validtime = false;
             
    
    }while(validtime && mylist.size()<BusConfig.getBuscapacity() && index < prefList.size());
    
    return mylist;
}
private void allocateBus(List<preference> studlist, String busid, DAYS day){
    PreparedStatement prps;
        String writetosched = "insert into schedule values(?,?,?,?,?)";
        try{
        prps = db.getPreparedSt(writetosched);
        prps.setString(2, studlist.get(0).getTime().toString() );
        for(int i = 0;i < studlist.size() ; i++)
        {
            
            prps.setString(1,busid);
            prps.setString(3,studlist.get(i).getSid());
            prps.setInt(4, 0);
            prps.setString(5, day.name());
            prps.executeUpdate();
        }
        }catch(SQLException sqle){
            System.err.println("Sql Exception...");
            sqle.printStackTrace();
        }
        
}
public List<AdminSchedule> viewAdminSchedule(DAYS day){
    List<AdminSchedule> lst =new ArrayList<AdminSchedule>();
    String viewquery= "select distinct(busid),deptime,count(*) as numstuds from schedule where dayofweek = '"+day.name()+"' group by busid";
    ResultSet rst = db.getQueryResults(viewquery);
    AdminSchedule obj;
    try {
        while(rst.next()){
            obj = new AdminSchedule();
            obj.setBusid(rst.getString("busid"));
            obj.setBustime(rst.getString("deptime"));
            obj.setNumstudstravelling(rst.getInt("numstuds"));
            lst.add(obj);
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
 return lst;   
}
public List<UserSchedule> viewUserSchedule(String userid){
    List<UserSchedule> lst =new ArrayList<UserSchedule>();
    String viewquery= " select busid, deptime, studseat, dayofweek from schedule where sid='"+userid+"'";
    ResultSet rst = db.getQueryResults(viewquery);
    UserSchedule obj;
    try {
        while(rst.next()){
            obj = new UserSchedule();
            obj.setBusid(rst.getString("busid"));
            obj.setBustime(rst.getString("deptime"));
            obj.setDayofweek(rst.getString("dayofweek"));
            obj.setStudseat(rst.getInt("studseat"));
            lst.add(obj);
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
 return lst;   
}
}
