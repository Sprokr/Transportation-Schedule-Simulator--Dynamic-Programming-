/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bussimulation;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author ab-admin
 */
public class Login_Creds {

    private String uid;
    private String passw;
    DatabaseUtil db;
    public Login_Creds(DatabaseUtil db) {
        this.db = db;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPassw() {
        return passw;
    }

    public void setPassw(String passw) {
        this.passw = passw;
    }
    public String checkValidity( String uid, String password)
    {
        String checkquery= "select count(*) as count, usertype from login_credentials where usrid = '"+uid+"' and passw = '"+password+"'";
        ResultSet rs = db.getQueryResults(checkquery);
        
        try{
    String result="0";
            if(rs.next())
             result = rs.getString("count");
            else
                System.err.print("i am done");
        
        if(Integer.parseInt(result) == 1)
        {
            return rs.getString("usertype");
        }
        }catch(SQLException sqle){
            sqle.printStackTrace();
        }
        return "n";
    }
}