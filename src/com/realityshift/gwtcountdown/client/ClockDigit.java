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

public class ClockDigit {

	public float x;
	public float y;
	public Element ctx;
	public int matrix[][];
	public int num;
	public String blankColor;
	public String activeColor;

	public ArrayList<ClockDot> dots;
	public boolean done = false;;
	public float velocity = 0f;
	public boolean removed = false;
	public int removedDotCount = 0;

	public void draw() {
		if (dots.size() == 0) {
			Gwtcountdown.numDots -= removedDotCount;
			done = true;
		} else {
			for (int i = 0; i < dots.size();)
				if (removed)
					if (dots.get(i).isActive) {
						if (dots.get(i).ball2d == null) {
							dots.get(i).ball2d = Gwtcountdown.world.createDot(
									dots.get(i).x, dots.get(i).y, velocity);
									dots.get(i).element.getStyle().setZIndex(99);
						}
						//if(true || dots.get(i).x != dots.get(i).ball2d.getPosition().x ||
						//		dots.get(i).y != dots.get(i).ball2d.getPosition().y) {
						//	
						//	GWT.log(dots.get(i).x + "," + dots.get(i).y + "  " + 
						//			dots.get(i).ball2d.getPosition().x + " , " +
						//			dots.get(i).ball2d.getPosition().y + " " +
						//			dots.get(i).ball2d.getLinearVelocity().x + ". " +
						//			dots.get(i).ball2d.getLinearVelocity().y + ". "
						//			);
						//	Gwtcountdown.world.step();
						//
						//	GWT.log("after" +dots.get(i).x + "," + dots.get(i).y + "  " + 
						//			dots.get(i).ball2d.getPosition().x + " , " +
						//			dots.get(i).ball2d.getPosition().y + " " +
						//			dots.get(i).ball2d.getLinearVelocity().x + ". " +
						//			dots.get(i).ball2d.getLinearVelocity().y + ". "
						//			);
						//}
								
						dots.get(i).x = dots.get(i).ball2d.getPosition().x * ClockWorld.Scale;
						dots.get(i).y = dots.get(i).ball2d.getPosition().y * ClockWorld.Scale;
						if ((dots.get(i).y < -350)
								|| (dots.get(i).x < Gwtcountdown.world.bounds_x
										* -1 - 20)
								|| (dots.get(i).x > Window.getClientWidth())
								|| (Gwtcountdown.numDots > Gwtcountdown.maxDots)) {
							//GWT.log("destory dot "+Gwtcountdown.numDots);
							Gwtcountdown.world.destroyDot(dots.get(i).ball2d);
							ctx.removeChild(dots.get(i).element);
							dots.remove(i);
							Gwtcountdown.numDots--;
							removedDotCount--;
						} else {
							dots.get(i).draw();
							i++;
						}
					// not active, just kill it
					} else {
						assert(dots.get(i).ball2d == null);
						ctx.removeChild(dots.get(i).element);
						dots.remove(i);
					}
				else {
					dots.get(i).draw();
					i++;
				}
					
			if (removed && removedDotCount == 0) {
				Gwtcountdown.numDots += dots.size();
				removedDotCount = dots.size();
			}
		}
	}

	public ClockDigit(float _x, float _y, Element _ctx, int _matrix[][],
			int _num, String _activeColor, String _blankColor) {

		x = _x;
		y = _y;
		ctx = _ctx;
		matrix = _matrix;
		num = _num;
		blankColor = _blankColor;
		activeColor = _activeColor;

		dots = new ArrayList<ClockDot>();

		for (int index = 0; index < matrix.length; index++)
			for (int row = 0; row < matrix[index].length; row++)
				dots.add(new ClockDot(
						x + 19 * row,
						y + 19 * index
						, ctx,
						(matrix[index][row] == 1) ? activeColor : blankColor, // fill color
						matrix[index][row] != 2,  // will draw
						matrix[index][row] == 1,  // is active
						this));
	}

	public boolean doremove() {
		if (num >= 0) {
			int newNum = (num +9) % 10;
			
			//newNum = num;

			int[][] newMatrix = Gwtcountdown.digitMatrix[newNum];
			int[][] curMatrix = Gwtcountdown.digitMatrix[num];

			int dotCnt = 0;

			//if (!(num == 0) || (num == 1)) {
				GWT.log("remove " + num);
				for (int row = 0; row < curMatrix.length; row++)
					for (int col = 0; col < curMatrix[row].length; col++) {
						if (curMatrix[row][col] == newMatrix[row][col])
							if (true) {
								assert(dots.get(dotCnt).ball2d == null);
								assert(dots.get(dotCnt).element != null);
								ctx.removeChild(dots.get(dotCnt).element);
								dots.remove(dotCnt);
								dotCnt--;
							}
						dotCnt++;
					}
				

			velocity = num == 0 ? Gwtcountdown.world.dots_high_velocity
					: Gwtcountdown.world.dots_high_velocity;
			removed = true;
		}
		return true;
	}
}