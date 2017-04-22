package com.unimelb.swen30006.metromadness.tracks;

import java.awt.geom.Point2D;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.unimelb.swen30006.metromadness.trains.Train;

/*
 * This class is the super class of the DualTrack class, any further added track classes
 * should be inherited from this class
 */ 
public class Track {
	public static final float DRAW_RADIUS=10f;
	public static final int LINE_WIDTH=6;
	public Point2D.Float startPos;
	public Point2D.Float endPos;
	public Color trackColour;
	public boolean occupied;
	
	/*
	 * Constructor
	 * @param the start point of the track
	 * @param the end point of the track
	 * @param the color of the track
	 */
	public Track(Point2D.Float start, Point2D.Float end, Color trackCol){
		this.startPos = start;
		this.endPos = end;
		this.trackColour = trackCol;
		this.occupied = false;
	}
	
	/*
	 * render the track
	 * @param shape renderer
	 */
	public void render(ShapeRenderer renderer){
		renderer.rectLine(startPos.x, startPos.y, endPos.x, endPos.y, LINE_WIDTH);
	}
	
	/*
	 * check if the track is occupied
	 * @param the direction
	 */
	public boolean canEnter(boolean forward){
		return !this.occupied;
	}
	
	@Override
	public String toString() {
		return "Track [startPos=" + startPos + ", endPos=" + endPos + ", trackColour=" + trackColour + ", occupied="
				+ occupied + "]";
	}
	
	/*
	 * set the track state to occupied
	 * @param the direction
	 */
	public void setOccupied(boolean direction){
		this.occupied = true;
	}
	
	/*
	 * set the track state to available
	 * @param the direction
	 */
	public void setAvailable(boolean direction){
		this.occupied = false;
	}
}
