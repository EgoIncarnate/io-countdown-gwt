package com.realityshift.gwtcountdown.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import gwt.ns.gwtbox2d.client.collision.AABB;
import gwt.ns.gwtbox2d.client.collision.shapes.CircleDef;
import gwt.ns.gwtbox2d.client.collision.shapes.PolygonDef;
import gwt.ns.gwtbox2d.client.common.*;
import gwt.ns.gwtbox2d.client.dynamics.*;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.*;
import com.google.gwt.user.client.ui.*;

public class ClockWorld {
	public float gravity_x;
	public float gravity_y;
	public String colors[];
	public float surface_density;
	public float surface_restitution;
	public float surface_friction;
	public String dotType;
	public float dots_density;
	public float dots_restitution;
	public float dots_friction;
	public float dots_high_velocity;
	public float dots_low_veloctity;
	public float bounds_x;
	public float bounds_y;
	public Element ctx;
	public Vec2 gravity;
	public float timeStep;
	public int iterations;
	public HashMap<String,Element> types;
	public World w;
	public Body ground;
	
	public static float Scale = 10;
	
	
    public ClockWorld(
		float _gravity_x,
		float _gravity_y,
		String _colors[],
		float _surface_density,
		float _surface_restitution,
		float _surface_friction,
		String _dotType,
		float _dots_density,
		float _dots_restitution,
		float _dots_friction,
		float _dots_high_velocity,
		float _dots_low_veloctity,
		float _bounds_x,
		float _bounds_y,
		Element _ctx
		) {
    	GWT.log("Clockworld()");
    		gravity_x = _gravity_x / Scale;
    		gravity_y = _gravity_y / Scale;
	    	colors = _colors;
	    	surface_density = _surface_density;
	    	surface_restitution = _surface_restitution;
	    	surface_friction = _surface_friction;
	    	dotType = _dotType;
	    	dots_density = _dots_density;
	    	dots_restitution = _dots_restitution;
	    	dots_friction = _dots_friction;
	    	dots_high_velocity = _dots_high_velocity / Scale;
	    	dots_low_veloctity = _dots_low_veloctity / Scale;
	    	bounds_x = _bounds_x;
	    	bounds_y = _bounds_y;
	    	ctx = _ctx;
	    	    	
	    	gravity = new Vec2(gravity_x, gravity_y);
	    	iterations = 1;
	    	timeStep = 0.05f;
	    	types = new HashMap<String, Element>();
	        for (int index = 0; index < colors.length; index++) {
	            String color = colors[index];
	            if (!types.containsKey(color))
	            	types.put(color,createDotElement(color, dotType, ctx));
	        }
	        types.put("c9c9c9", createDotElement("c9c9c9", dotType, ctx));
	        types.put("d9d9d9", createDotElement("d9d9d9", dotType, ctx));
	        types.put("b6b4b5", createDotElement("b6b4b5", dotType, ctx));
	        createWorld();            
    }
    
	private World createWorld() {
		AABB a = new AABB();
        a.lowerBound.set(-2000/Scale, -2000/Scale);
        a.upperBound.set(2000/Scale, 2000/Scale);
        w = new World(a, new Vec2(gravity_x, gravity_y), true);
        createGround(Math.min(Window.getClientHeight()/*(window.innerHeight || document.documentElement.clientHeight) - 355*/-135,500));
        return w;
	}
	
	public Body createGround(int height) {
        if (ground != null) {
            w.destroyBody(ground);
            ground = null;
        }
        PolygonDef pd = new PolygonDef();
        pd.setAsBox(2000/Scale,50/Scale);
        pd.friction = 0;
        pd.restitution = 0.9f;
        BodyDef bd = new BodyDef();
        bd.position = new Vec2(0, height/Scale);
        Body b = w.createBody(bd);
        b.createShape(pd);
        ground = b;
        return b;
	}

	public Element createDotElement(String color, String dotType, Element ctx) {
    	//GWT.log("Clockdotelement()");
        Element img = DOM.createImg();
        img.setClassName(dotType);
        img.setPropertyString("src", "./static/img/" + dotType + "-" + color + ".png");
        ctx.appendChild(img);
        return img;
	}

	public void step() {
		//GWT.log("Step:"+  timeStep +" "+ iterations + " "+ nDot + " " + Gwtcountdown.numDots);
		w.step(timeStep, iterations);
	}
	
	int nDot = 0;

	public Body createDot(float x, float y, float velocity) {
		nDot++;
		CircleDef c = new CircleDef();
        c.radius = 7/Scale;
        c.restitution = dots_restitution;
        c.density = dots_density;
        c.friction = dots_friction;
        
        BodyDef bd = new BodyDef();
        bd.position = new Vec2(x/Scale,y/Scale);
        bd.linearDamping = 0.0f;
        bd.angularDamping = 0.0f;
       
        Body b = w.createBody(bd);
        b.createShape(c);
        b.setMassFromShapes();
        b.m_linearVelocity.set((float)Math.random() * velocity  - velocity / 2,
        		(float)Math.random() * velocity - velocity / 2); 
        
        return b;
        
        
	}
		

	public void destroyDot(Body ball2d) {
		nDot--;
		
		w.destroyBody(ball2d);
	}    
}
