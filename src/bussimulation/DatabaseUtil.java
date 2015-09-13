package bussimulation;

import utils.PrefComp;
import utils.preference;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.MyTime;
import utils.TimeComp;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ab-admin
 */
enum DAYS {tMonday, tTues, tWed, tThur, tFri, tSat, tSun};

public class DatabaseUtil {
    private Connection conn;
    private Statement stmt;
    private PreparedStatement prps;
    private static String dbname;
    private static String port;
    private static String dburl;
    private static String userid;
    private static String pass;
    
    static{
        dbname = "transport";
        port = "3306";
        dburl = "jdbc:mysql://localhost:";
        userid = "root";
        pass = "123";
                
    }
    private void initdatabase(){
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            String actualurl;
            actualurl = dburl + port + "/" + dbname ;
            conn = DriverManager.getConnection(actualurl,userid, pass);
            stmt = conn.createStatement();
            
         }catch(Exception e){
             e.printStackTrace();
             }
        
    }
    public DatabaseUtil(){
        initdatabase();
    }
    public ResultSet getQueryResults(String query){
     try
     {
         return stmt.executeQuery(query);
         
     }catch(SQLException sqle){
         sqle.printStackTrace();
     }
     
        return null;
    }
    public int getUpdateResults(String query) throws SQLException{
         return stmt.executeUpdate(query);
         
         
    }
    public int insertStudents(String sname, String sid) {
        String insertquery = "INSERT INTO studentdb(sname, sid) values('"+sname+"','"+sid+"')";
        int k=-1;
        try{
        k= getUpdateResults(insertquery);
                }catch(Exception e){
                    e.printStackTrace();
                }
        return k;
    }
    
    public int populateStudents(int numstudents, int maxhour, int minhour){
        String randomname;
        String randomid;
        int randomhour,randommins;
        Random picknum=new Random();
        
        String [] timepref = new String[7];
        for(int i=0;i<numstudents;i++){
        randomname = new Integer(10000 + picknum.nextInt(20000)).toString();
        randomid =  new Integer(10 + picknum.nextInt(numstudents)).toString();
        String insertquery = "INSERT INTO studentdb(sname, sid) values('"+randomname+"','"+randomid+"')";
        try{
        getUpdateResults(insertquery);
        
        for(int j=0;j<7;j++){
            randomhour = minhour + picknum.nextInt(maxhour - minhour + 1);
            randommins = picknum.nextInt(4) * 15;
            timepref[j] = randomhour + ":" + randommins;
            }
            populateTimePrefs(randomid, timepref);
        
        }catch(SQLException sqle){
            //System.err.println("Error code-- :"+sqle.getErrorCode());
           int errcode = sqle.getErrorCode();
           if(errcode == 1062)      //Duplicate primary key
           {
               System.err.println("Duplication found... Regenerate");
               i--;
           }
           else{
               System.err.println("Something else happened...Stop the loop");
               sqle.printStackTrace();
               i=numstudents +1;
               
           }
               
        }
        }
        
        return 0;
        
    }
    public int populateTimePrefs(String sid, String[] pref){
        String insertquery = "INSERT INTO timepref values('"+sid+"', ";
        for(int i = 0; i< pref.length; i++){
            if(i == pref.length-1)
            insertquery = insertquery + "'"+pref[i] +"' )";
            else
            insertquery = insertquery + "'"+pref[i] +"', ";
        }
        //Debug
        System.err.println("Inserted query "+insertquery);
        try{
        getUpdateResults(insertquery);
        }catch(SQLException sqle){
            System.err.println("Error code-- :"+sqle.getErrorCode());
            sqle.printStackTrace();
        }
     return 0;
    }
    public List<preference> getScheduleforaDay(DAYS day){
        try {
         //   System.out.println(day.toString());
            String query = "select sid, "+day.toString()+" from timepref";
            ResultSet rs= getQueryResults(query);
            List<preference> ls=new ArrayList<preference>();
            String timestring="0:0";
            preference obj;
            String sid ="0";
            
            while(rs.next()){
                 timestring = rs.getString(day.toString());
                 sid = rs.getString("sid");
       //          System.out.println("Recived time: "+timestring);
                 obj = new preference(sid,timestring);
                 ls.add(obj);
                }
                 //Now sort the list
            PrefComp prefcomparator = new PrefComp();
            Collections.sort(ls, prefcomparator);
          //  System.out.println(ls);
            return ls;
            }catch (SQLException ex){
            ex.printStackTrace();
        }
        return null;
        
    }
public int populateGaussianStudents(int numstudents, int maxhour, int minhour){    
        Random rnd = new Random();
        int numgaussians = 3 + rnd.nextInt(4);
        int studspergaussian = numstudents/numgaussians;
        //String[] studnames =new String[numstudents];
        String studname,sid;
        Student[] stud= new Student[numstudents];
         Set<String> allsids = new HashSet<String>();
        //generate ids
         boolean dup =false;
         
         deleteprevious("timepref");
         deleteprevious("studentdb");
         deletecreds();
         
        for(int i=0; i<numstudents; i++)
        {
            do{
            sid =  new Integer(10 + rnd.nextInt(numstudents)).toString();
            dup = allsids.contains(sid);
            }while(dup);
            allsids.add(sid);
            dup=false;
            studname = new Integer(10000 + rnd.nextInt(20000)).toString();
            
            stud[i]= new Student(sid,studname);
            generateCredentials(sid);
            addstudentdetails(sid,studname);
        }
        
        
        List<MyTime> timelist= new ArrayList<MyTime>();
        int meanhour, meanmins,studctr=0;
        System.err.println("Num gaussians:"+numgaussians);
        for(int weekday =0;weekday < 7;weekday++)
        {
            for(int gauss =0;gauss<numgaussians;gauss++)
            {
            //SELECT RANDOM HOUR
            meanhour = minhour + rnd.nextInt(maxhour - minhour);
            //select random min dur
            meanmins = 15 * rnd.nextInt(4);
            studspergaussian = numstudents/numgaussians;
            if(gauss == numgaussians-1)
                   studspergaussian += numstudents%numgaussians;
            
            MyTime meantme=new MyTime(meanhour, meanmins);
            MyTime tmobj;
            int minutesadjust;
           //     System.err.println("Week "+ weekday+ "This time gaussians = "+studspergaussian);
            for(int i=0; i< studspergaussian; i++){
                double dble = rnd.nextGaussian();
                minutesadjust =  (int)(2 * dble);
                minutesadjust *=15;
                if(minutesadjust !=0){
                    if(minutesadjust > 0)
                        tmobj = MyTime.add(meantme, new MyTime(0,minutesadjust));
                    else
                        tmobj = MyTime.subtractMinutes(meantme, -minutesadjust);
                    timelist.add(tmobj);
                    }
                else
                    timelist.add(meantme);    
                }

            }
            
            for(studctr =0; studctr < timelist.size();studctr++){
                stud[studctr].setATimePref(timelist.get(studctr), weekday);
            }
            System.out.println(timelist);
         //   Collections.sort(timelist, new TimeComp());
         //   System.out.println(timelist);
            timelist.clear();
           
        }
        System.out.println("Write student vals to database");
        String writetostud = "insert into timepref values(?,?,?,?,?,?,?,?)";
        try{
        prps = conn.prepareStatement(writetostud);
        
        for(studctr = 0;studctr < numstudents; studctr++)
        {
            prps.setString(1,stud[studctr].getId());   
            for(int i=0;i<7;i++)
                prps.setString(i+2, stud[studctr].getTimeprefs()[i].toString());
            prps.executeUpdate();
        }
        }catch(SQLException sqle){
            System.err.println("Sql Exception...");
            sqle.printStackTrace();
        }
            
    return 0;
}
private void generateCredentials(String sid){
StringBuilder randompassword=new StringBuilder("p");
String usertype = "s";
String somepass;
String credentialsinsert;    
Random rnd= new Random();


randompassword.append('A');
randompassword.append("1000"+(1000+rnd.nextInt(10000)));
somepass = randompassword.toString();
    System.out.println("Generated pass is :"+somepass);
    
credentialsinsert = "insert into login_credentials values('"+sid+"','"+somepass+"','"+usertype+"')";
        try {        
            int success = getUpdateResults(credentialsinsert);
        } catch (SQLException ex) {
          ex.printStackTrace();
        }
            
        
}
private void addstudentdetails(String sid,String studname){
String studentdetinsert = "insert into studentdb values('"+sid+"','"+studname+"')";
    try {        
        getUpdateResults(studentdetinsert);
        } catch (SQLException ex) {
          ex.printStackTrace();
        }
        
}
public PreparedStatement getPreparedSt(String sql) throws SQLException
{
    return conn.prepareStatement(sql);
}
private void deleteprevious(String tablename)
{
    String delString = "delete from "+tablename;
    try {
        getUpdateResults(delString);
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
}
private void deletecreds()
{
    String delString = "delete from login_credentials where usertype ='s'";
    try {
        getUpdateResults(delString);
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
}
}


