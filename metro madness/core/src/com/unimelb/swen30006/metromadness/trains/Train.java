package com.unimelb.swen30006.metromadness.trains;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.unimelb.swen30006.metromadness.passengers.Passenger;
import com.unimelb.swen30006.metromadness.stations.CargoStation;
import com.unimelb.swen30006.metromadness.stations.Station;
import com.unimelb.swen30006.metromadness.tracks.Line;
import com.unimelb.swen30006.metromadness.tracks.Track;
import com.unimelb.swen30006.metromadness.trains.Train.State;

/**
 * this class represent is the  super class of the SmallCargoTrain, SmallPassengerTrain, BigPassengerTrain,
 * LargePassengerTrain. Any other train to be added in the later update should be inherited from this class 
 */


public class Train {
	// Logger
	protected static Logger logger = LogManager.getLogger();
	// The state that a train can be in 
	public enum State {
		IN_STATION, READY_DEPART, ON_ROUTE, WAITING_ENTRY, FROM_DEPOT
	}

	// Constants
	public static final int MAX_TRIPS=4;
	public static final Color FORWARD_COLOUR = Color.ORANGE;
	public static final Color BACKWARD_COLOUR = Color.VIOLET;
	public static final float TRAIN_WIDTH=4;
	public static final float TRAIN_LENGTH = 6;
	public static final float TRAIN_SPEED=50f;
	
	// The train's name
	public String name;

	// The line that this is traveling on
	public Line trainLine;

	// Passenger Information
	public ArrayList<Passenger> passengers;
	public float departureTimer;
	
	// Station and track and position information
	public Station station; 
	public Track track;
	public Point2D.Float pos;

	// Direction and direction
	public boolean forward;
	public State state;

	// State variables
	public int numTrips;
	public boolean disembarked;
	
	
	public State previousState = null;
	
	/**
	 * Constructor
	 * @param train line
	 * @param strat station
	 * @param train's heading direction
	 * @param the train's name
	 */
	public Train(Line trainLine, Station start, boolean forward, String name){
		this.trainLine = trainLine;
		this.station = start;
		this.state = State.FROM_DEPOT;
		this.forward = forward;
		this.passengers = new ArrayList<Passenger>();
		this.name = name;
	}
	
	
	/**
	 * This method determines the transition between the
	 * different states of the train
	 * @param delta value
	 */
	public void update(float delta){
		// Update all passengers
		for(Passenger p: this.passengers){
			p.update(delta);
		}
		boolean hasChanged = false;
		if(previousState == null || previousState != this.state){
			previousState = this.state;
			hasChanged = true;
		}
		
		// Update the state
		switch(this.state) {
		case FROM_DEPOT:
			if(hasChanged){
				logger.info(this.name+ " is travelling from the depot: "+this.station.name+" Station...");
			}
			
			// We have our station initialized we just need to retrieve the next track, enter the
			// current station officially and mark as in station
			try {
				if(this.station.canEnter(this.trainLine)){	
					enterStation();
					this.pos = (Point2D.Float) this.station.position.clone();
					this.state = State.IN_STATION;
					this.disembarked = false;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		case IN_STATION:
			if(hasChanged){
				logger.info(this.name+" is in "+this.station.name+" Station.");
			}
			
			// When in station we want to disembark passengers 
			// and wait 10 seconds for incoming passengers
			if(!this.disembarked){
				this.disembark();
				this.departureTimer = this.station.getDepartureTime();
				this.disembarked = true;
			} else {
				// Count down if departure timer. 
				if(this.departureTimer>0){
					this.departureTimer -= delta;
				} else {
					// We are ready to depart, find the next track and wait until we can enter 
					try {
						boolean endOfLine = this.trainLine.endOfLine(this.station);
						if(endOfLine){
							this.forward = !this.forward;
						}
						this.track = this.trainLine.nextTrack(this.station, this.forward);
						this.state = State.READY_DEPART;
						break;
					} catch (Exception e){
						// Massive error.
						return;
					}
				}
			}
			break;
		case READY_DEPART:
			// When ready to depart, check that the track is clear and if
			// so, then occupy it if possible.
			if(this.track.canEnter(this.forward)){
				try {
					// Find the next
					Station next = this.trainLine.nextStation(this.station, this.forward);
					// Depart our current station
					leaveStation();
					this.station = next;

				} catch (Exception e) {
//					e.printStackTrace();
				}
				enterTrack();
				this.state = State.ON_ROUTE;
			}
			
			if(hasChanged){
				logger.info(this.name+ " is ready to depart for "+this.station.name+" Station!");
			}
			break;
		case ON_ROUTE:
			if(hasChanged){
				logger.info(this.name+ " enroute to "+this.station.name+" Station!");
			}
			
			// Checkout if we have reached the new station
			if(this.pos.distance(this.station.position) < 10 ){
				this.state = State.WAITING_ENTRY;
			} else {
				move(delta);
			}
			break;
		case WAITING_ENTRY:
			if(hasChanged){
				logger.info(this.name+ " is awaiting entry "+this.station.name+" Station..!");
			}
			
			// Waiting to enter, we need to check the station has room and if so
			// then we need to enter, otherwise we just wait
			try {
				if(this.station.canEnter(this.trainLine)){
					leaveTrack();
					this.pos = (Point2D.Float) this.station.position.clone();
					enterStation();
					this.state = State.IN_STATION;
					this.disembarked = false;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		}

	}
	
	/**
	 * render the train moving status on the simulation
	 * @param delta value
	 */
	public void move(float delta){
		// Work out where we're going
		float angle = angleAlongLine(this.pos.x,this.pos.y,this.station.position.x,this.station.position.y);
		float newX = this.pos.x + (float)( Math.cos(angle) * delta * TRAIN_SPEED);
		float newY = this.pos.y + (float)( Math.sin(angle) * delta * TRAIN_SPEED);
		this.pos.setLocation(newX, newY);
	}

	/**
	 * passenger embark into the train
	 * @param passenger
	 * */
	public void embark(Passenger p) throws Exception {
		throw new Exception();
	}
	
	/**
	 * disembark passengers when they reached their destination
	 * */

	public ArrayList<Passenger> disembark(){
		ArrayList<Passenger> disembarking = new ArrayList<Passenger>();
		Iterator<Passenger> iterator = this.passengers.iterator();
		while(iterator.hasNext()){
			Passenger p = iterator.next();
			if(p.shouldLeave(this.station)){
				logger.info("Passenger "+p.id+" is disembarking at "+this.station.name);
				disembarking.add(p);
				iterator.remove();
			}
		}
		return disembarking;
	}
	
	/**
	 * Override the toString() method
	 */
	@Override
	public String toString() {
		return "Train [line=" + this.trainLine.name +", departureTimer=" + departureTimer + ", pos=" + pos + ", forward=" + forward + ", state=" + state
				+ ", numTrips=" + numTrips + ", disembarked=" + disembarked + "]";
	}
	
	/**
	 * decide whether the train is in station
	 */
	public boolean inStation(){
		return (this.state == State.IN_STATION || this.state == State.READY_DEPART);
	}
	
	/**
	 * calculate the angle along the line
	 * @param x1 value
	 * @param y1 value
	 * @param x2 value
	 * @param y2 value
	 * */
	public float angleAlongLine(float x1, float y1, float x2, float y2){	
		return (float) Math.atan2((y2-y1),(x2-x1));
	}
	
	/**
	 * render this train
	 * @param shape renderer
	 * */

	public void render(ShapeRenderer renderer){
		if(!this.inStation()){
			Color col = this.forward ? FORWARD_COLOUR : BACKWARD_COLOUR;
			renderer.setColor(col);
			renderer.circle(this.pos.x, this.pos.y, TRAIN_WIDTH);
		}
	}
	
	/**
	 * train enter the track, set track as occupied
	 */
	public void enterTrack(){
		this.track.setOccupied(this.forward);
	}
	
	/**
	 * train leave the track,set track as avaliable
	 */
	public void leaveTrack(){
		this.track.setAvailable(this.forward);
	}
	
	/**
	 * train enter the station
	 */
	public void enterStation(){
		try{
		    this.station.arrivedTrain(this);
		}catch(Exception e){
		}
	}
	
	/**
	 * train leave the station
	 */
	public void leaveStation(){
		try{
		    this.station.departedTrain(this);
		}catch(Exception e){
		}
	}
}
