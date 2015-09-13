/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bussimulation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ab-admin
 */
public class BusConfig {

    public static int getNumbuses() {
        return numbuses;
    }

    public static void setNumbuses(int numbuses) {
        BusConfig.numbuses = numbuses;
    }

    public static int getBuscapacity() {
        return buscapacity;
    }

    public static void setBuscapacity(int buscapacity) {
        BusConfig.buscapacity = buscapacity;
    }

    public static int getTolerance() {
        return tolerance;
    }

    public static void setTolerance(int tolerance) {
        BusConfig.tolerance = tolerance;
    }

    public static int getDepStarthour() {
        return depStarthour;
    }

    public static void setDepStarthour(int depStarthour) {
        BusConfig.depStarthour = depStarthour;
    }

    public static int getDepEndhour() {
        return depEndhour;
    }

    public static void setDepEndhour(int depEndhour) {
        BusConfig.depEndhour = depEndhour;
    }
private static int numbuses;
private static int buscapacity;
private static int tolerance;
private static int depStarthour;
private static int depEndhour;
private static DatabaseUtil db;


    static{
        depEndhour = 20;
        depStarthour = 7;
        tolerance = 0;
        buscapacity = 50;
        db = null;
    }

    public static void getValsFromDb()
    {
        String query = "select * from config";
        ResultSet rs =db.getQueryResults(query);
    try {
        if(rs.next())
        {
            numbuses = rs.getInt("numbuses");
            tolerance = rs.getInt("tolerance");
            buscapacity = rs.getInt("seatcapacity");
            depStarthour = 7;
            depEndhour = 20;
            
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
    }
    public static void setDatabase(DatabaseUtil dbu){
        BusConfig.db = dbu;
    }
    
    public static void alterNumBuses(int numbuses){
        BusConfig.numbuses = numbuses;
        String alterbusquery = "UPDATE config set numbuses = "+numbuses;
    try {
        db.getUpdateResults(alterbusquery);
    
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
    }
     public static void alterTolerance(int tolerance){
        BusConfig.tolerance = tolerance;
        String alterbusquery = "UPDATE config set tolerance = "+tolerance;
    try {
        db.getUpdateResults(alterbusquery);
    
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
    }
   
}
