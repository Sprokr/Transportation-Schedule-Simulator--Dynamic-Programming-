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
public class PrefComp implements Comparator<preference>{
    @Override
    public int compare(preference p1, preference p2) {
        MyTime o1 = p1.getTime();
        MyTime o2 = p2.getTime();
        
        if(o1.getHours() > o2.getHours())
            return 1;
        else{
            if(o1.getHours() < o2.getHours())
                return -1;
            else{
                if(o1.getMins() > o2.getMins())
                    return 1;
                else{
                    if(o1.getMins() < o2.getMins())
                        return -1;
                    else
                        return 0;
                }
            }
        }
    
    }
}
