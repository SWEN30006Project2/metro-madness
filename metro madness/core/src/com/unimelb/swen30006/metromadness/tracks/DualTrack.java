package com.unimelb.swen30006.metromadness.tracks;

import java.awt.geom.Point2D.Float;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.unimelb.swen30006.metromadness.trains.Train;

/*
 * This is class is the subclass of the Track class
 */
public class DualTrack extends Track {
	public boolean forwardOccupied;
	public boolean backwardOccupied;
	
	/*
	 * Constructor
	 * @param The start point
	 * @param The end point
	 * @param the track color 
	 */
	public DualTrack(Float start, Float end, Color col) {
		super(start, end, col);
		this.forwardOccupied = false;
		this.backwardOccupied = false;
	}
	
	/*
	 * render this track
	 * @param shape renderer
	 */
	public void render(ShapeRenderer renderer){
		renderer.rectLine(startPos.x, startPos.y, endPos.x, endPos.y, LINE_WIDTH);
		renderer.setColor(new Color(245f/255f,245f/255f,245f/255f,0.5f).lerp(this.trackColour, 0.5f));
		renderer.rectLine(startPos.x, startPos.y, endPos.x, endPos.y, LINE_WIDTH/3);
		renderer.setColor(this.trackColour);
	}
	
	/*
	 * check if the train can enter this track
	 * @param the moving direction of the train
	 */
	@Override
	public boolean canEnter(boolean forward) {
		if(forward){
			return !this.forwardOccupied;
		} else {
			return !this.backwardOccupied;
		}
	}
	
	/*
	 * set the track state to occupied in some direction
	 * @param the direction
	 */
	@Override
	public void setOccupied(boolean direction){
		if(direction){
			this.forwardOccupied = true;
		} else {
			this.backwardOccupied = true;
		}
	}
	
	/*
	 * set the track state to available in some direction
	 * @param the direction
	 */
	@Override
	public void setAvailable(boolean direction){
		if(direction){
			this.forwardOccupied = false;
		} else {
			this.backwardOccupied = false;
		}
	}
}
