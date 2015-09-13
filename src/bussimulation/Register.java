/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bussimulation;

import java.sql.SQLException;

/**
 *
 * @author ab-admin
 */
public class Register {
    private String userid,password;
    DatabaseUtil mydb;
    public Register(DatabaseUtil db){
        mydb = db;
    }
    public String registerStudent(String studname, String userid, String passw){
        String insertquery = "insert into login_credentials(usrid,passw,usertype) values ('"+userid+"','"+passw+"','s')";
        String fillstudentdb = "insert into studentdb(sid,sname) values ('"+userid+"','"+studname+"')";
        int res=-1,res2=-1;
        try{
            res= mydb.getUpdateResults(insertquery);
            res2 = mydb.getUpdateResults(fillstudentdb);
            
        }
        catch(SQLException sqle){
            sqle.printStackTrace();
        }
        System.out.println("final results "+res+"  "+res2);
        if(res==1&&res2==1)
            return "Y";
        else
            return "N";
    }
}
