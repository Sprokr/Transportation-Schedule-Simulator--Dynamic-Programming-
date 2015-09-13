/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package utils;

import java.util.Comparator;

/**
 *
 * @author ab-admin
 */
public class TimeComp implements Comparator<MyTime>{
    @Override
    public int compare(MyTime t1, MyTime t2) {
        
        
        if(t1.getHours() > t2.getHours())
            return 1;
        else{
            if(t1.getHours() < t2.getHours())
                return -1;
            else{
                if(t1.getMins() > t2.getMins())
                    return 1;
                else{
                    if(t1.getMins() < t2.getMins())
                        return -1;
                    else
                        return 0;
                }
            }
        }
    
    }
}
