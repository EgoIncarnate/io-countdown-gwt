package com.realityshift.gwtcountdown.client;

import java.util.ArrayList;
import java.util.Date;

import gwt.ns.gwtbox2d.client.collision.AABB;
import gwt.ns.gwtbox2d.client.common.*;
import gwt.ns.gwtbox2d.client.dynamics.*;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.*;
import com.google.gwt.user.client.ui.*;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */




@SuppressWarnings("deprecation")
public class Gwtcountdown implements EntryPoint{
	
	public Date til = new Date(2011-1900, 4, 10, 9, 0, 0);
		
	public static int numDots = 0;
    public static int maxDots = 150;
            
    public static int solid[] = {1, 1, 1, 1};
    public static int right[] = {0, 0, 0, 1};
    public static int left[] = {1, 0, 0, 0};
    public static int ends[] = {1, 0, 0, 1};
    public static int colonMatrix[][] = {
            {2, 2},
            {2, 2},
            {1, 2},
            {2, 2},
            {1, 2},
            {2, 2},
            {2, 2}
        };
	public static int digitMatrix[][][] = {
        {solid, ends, ends, ends, ends, ends, solid},// 0  
        {right, right, right, right, right, right, right},// 1
        {solid, right, right, solid, left, left, solid},// 3
        {solid, right, right, solid, right, right, solid},// 2
        {ends, ends, ends, solid, right, right, right},// 4
        {solid, left, left, solid, right, right, solid},// 5
        {solid, left, left, solid, ends, ends, solid},// 6
        {solid, right, right, right, right, right, right},// 7
        {solid, ends, ends, solid, ends, ends, solid},// 8
        {solid, ends, ends, solid, right, right, solid} // 9
    };
	
    public ArrayList<ClockDigit> oldClockDigits = new ArrayList<ClockDigit>();
    public ArrayList<ClockDigit> activeClockDigits = new ArrayList<ClockDigit>();
    
	public Element countdownElement;
	
	public static ClockWorld world;
    		
    public String toDigitsNN(long n, int digits) {
    	String out = "" + n;
        if (n < 100 && digits == 3) out = "0" + out;
        if (n < 10) out = "0" + out;
        return out.toString();
    }
    
	public void Tick() {
    	//GWT.log("Tick");
		Date now = new Date();
		DOM.getElementById("clock").setInnerHTML(now.toString()+" "+til.toString());
		
		int remaining = (int) ((til.getTime() - now.getTime())/1000);
		String days = toDigitsNN(remaining / 86400, 3);
		int seccondsAfterDaysRemaining = remaining % 86400;
		String hours = toDigitsNN(seccondsAfterDaysRemaining / 3600, 2);
		String minutes = toDigitsNN(seccondsAfterDaysRemaining % 3600 / 60, 2);
		String seconds = toDigitsNN((seccondsAfterDaysRemaining % 60), 2);
		String sdisplay = days + ":" + hours + ":" + minutes + ":" + seconds;
		DOM.getElementById("countdown_time").setInnerHTML(sdisplay);
		
	    int curX = 0;
	    ArrayList<ClockDigit> newClockDigits = new ArrayList<ClockDigit>();		        
				
		String[] display = sdisplay.split("");
		int nDigit;
		                       
        for (int curDigit = 1; curDigit < display.length; curDigit++) {
        	//GWT.log("curDigit" + curDigit + "=<" + display[curDigit]+ ">");
        	if(display[curDigit].equals(":"))
        		nDigit = -1;
        	else
        		nDigit = Integer.parseInt(display[curDigit]);
            if ((oldClockDigits.size() > 0) && 
            		(nDigit == oldClockDigits.get(curDigit-1).num) ) {
                newClockDigits.add(oldClockDigits.get(curDigit-1));
                curX += (nDigit == -1) ? 36 : 94;
                // otherwise do fancy animation
            } else {      
                if (display[curDigit].equals(":")) {
                	//GWT.log("colon");
                    newClockDigits.add(new ClockDigit(
                        curX,
                        14,
                        countdownElement,
                        colonMatrix,
                        -1,
                        "b6b4b5",
                        "ffffff"
                    ));
                    curX += 36;
                } else {
                	//GWT.log("digit");
                    newClockDigits.add(new ClockDigit(
                        curX,
                        14,
                        countdownElement,
                        digitMatrix[Integer.parseInt(display[curDigit])],
                        Integer.parseInt(display[curDigit]),
                        world.colors[curDigit-1],
                        curDigit < 4 || curDigit > 9 ? "c9c9c9" : "d9d9d9"
                    ));
                    curX += 94;
                }
                newClockDigits.get(curDigit-1).draw();
                if(oldClockDigits.size() > 0)
                	activeClockDigits.add(oldClockDigits.get(curDigit-1));
            }
        }
        world.step();
        for (int index = 0; index < activeClockDigits.size(); index++) 
	        if (activeClockDigits.get(index).done) 
	        	activeClockDigits.remove(index);
	        else {
	            if(!activeClockDigits.get(index).removed)
	            	activeClockDigits.get(index).doremove();
	            activeClockDigits.get(index).draw();
	        }
        oldClockDigits = newClockDigits;      
	}
    		
	public void onModuleLoad() {
		
    	GWT.log("Module Load");

		countdownElement = DOM.getElementById("countdown");
		
        world = new ClockWorld(
                00f,
                200f,
                new String[] {"265897", "265897", "265897", "", "13acfa", "13acfa", "", "c0000b", "c0000b", "", "009a49", "009a49"},
                1f,
                1f,
                0f,
                "ball",
                0.3f,
                0.5f,
                0.1f,
                500f,
                200f,
                Window.getClientWidth(),
                Window.getClientHeight(),
                countdownElement
            );
		
		Timer t = new Timer() {
			@Override
			public void run() {
				Tick();
				schedule(1000/60);
			}
		};
		
		t.schedule(1000/60);

	}

}
