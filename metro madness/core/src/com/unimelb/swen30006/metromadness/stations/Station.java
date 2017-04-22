package com.unimelb.swen30006.metromadness.stations;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.unimelb.swen30006.metromadness.passengers.Passenger;
import com.unimelb.swen30006.metromadness.routers.PassengerRouter;
import com.unimelb.swen30006.metromadness.tracks.Line;
import com.unimelb.swen30006.metromadness.trains.Train;

/*
 * This class is the super class of the active station and the cargo station, any further added stations
 * class should be inherited from this class 
 */
public class Station {
	public static final int PLATFORMS=2;
	public Point2D.Float position;
	public static final float RADIUS=6;
	public static final int NUM_CIRCLE_STATMENTS=100;
	public static final int MAX_LINES=3;
	public String name;
	public ArrayList<Line> lines;
	public ArrayList<Train> trains;
	public static final float DEPARTURE_TIME = 2;
	public PassengerRouter router;

	/*
	 * Constructor
	 * @param x value
	 * @param y value 
	 * @param passenger router
	 * @param the station's name
	 */
	public Station(float x, float y, PassengerRouter router, String name){
		this.name = name;
		this.router = router;
		this.position = new Point2D.Float(x,y);
		this.lines = new ArrayList<Line>();
		this.trains = new ArrayList<Train>();
	}
	
	/*
	 * add lines that pass through this station
	 * @param the line that pass through this station
	 */
	public void registerLine(Line l){
		this.lines.add(l);
	}
	
	/*
	 * render this station
	 * @param shape renderer
	 */
	public void render(ShapeRenderer renderer){
		float radius = RADIUS;
		for(int i=0; (i<this.lines.size() && i<MAX_LINES); i++){
			Line l = this.lines.get(i);
			renderer.setColor(l.lineColour);
			renderer.circle(this.position.x, this.position.y, radius, NUM_CIRCLE_STATMENTS);
			radius = radius - 1;
		}
		
		// Calculate the percentage
		float t = this.trains.size()/(float)PLATFORMS;
		Color c = Color.WHITE.cpy().lerp(Color.DARK_GRAY, t);
		renderer.setColor(c);
		renderer.circle(this.position.x, this.position.y, radius, NUM_CIRCLE_STATMENTS);		
	}
	
	/*
	 * add the arrived train to this station
	 * @param arrived train
	 */
	public void arrivedTrain(Train t) throws Exception {
		if(trains.size() >= PLATFORMS){
			throw new Exception();
		} else {
			this.trains.add(t);
		}
    }
	
	/*
	 * delete the train that is departing this station
	 * @param the departing train
	 */
	public void departedTrain(Train t) throws Exception {
		if(this.trains.contains(t)){
			this.trains.remove(t);
		} else {
			throw new Exception();
		}
	}
	
	/*
	 * check if the train can enter the station through a particular line
	 * @param the line that an incoming train runs on
	 */
	public boolean canEnter(Line l) throws Exception {
		return trains.size() < PLATFORMS;
	}

	// Returns departure time in seconds
	public float getDepartureTime() {
		return DEPARTURE_TIME;
	}

	@Override
	public String toString() {
		return "Station [position=" + position + ", name=" + name + ", trains=" + trains.size()
				+ ", router=" + router + "]";
	}
}
