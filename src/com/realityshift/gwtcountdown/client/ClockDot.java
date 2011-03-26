package com.realityshift.gwtcountdown.client;

import java.util.ArrayList;
import java.util.Date;

import gwt.ns.gwtbox2d.client.collision.AABB;
import gwt.ns.gwtbox2d.client.common.*;
import gwt.ns.gwtbox2d.client.dynamics.*;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.*;
import com.google.gwt.user.client.ui.*;

public class ClockDot {
    public float x;
    public float y;
    public Element ctx;
    public String fillStyle;
    public boolean willDraw;
    public boolean isActive;
    public ClockDigit digit;
    public Element element;
    public Body ball2d;

    public ClockDot(
	    float _x,
	    float _y,
	    Element _ctx,
	    String _fillStyle,
	    boolean _willDraw,
	    boolean _isActive,
	    ClockDigit _digit
		) {
    
	    x = _x;
	    y = _y;
	    ctx = _ctx;
	    fillStyle = _fillStyle;
	    willDraw = _willDraw;
	    isActive = _isActive;
	    digit = _digit;
    }

	public void draw() {

        if (willDraw) {
            if (element == null && Gwtcountdown.world.types.containsKey(fillStyle)) {
            	Element cloner = Gwtcountdown.world.types.get(fillStyle);
            	if(cloner != null)
            		element = (Element)cloner.cloneNode(false);
                ctx.appendChild(element);
            }
            if(element != null) {
            	element.getStyle().setLeft(x, Unit.PX);
            	element.getStyle().setTop(y, Unit.PX);
            }
        }
    	//GWT.log("end dot.draw()");
	}
}
