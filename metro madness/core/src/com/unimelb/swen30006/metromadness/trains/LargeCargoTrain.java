package com.unimelb.swen30006.metromadness.trains;

import java.awt.geom.Point2D;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.unimelb.swen30006.metromadness.passengers.Passenger;
import com.unimelb.swen30006.metromadness.stations.CargoStation;
import com.unimelb.swen30006.metromadness.stations.Station;
import com.unimelb.swen30006.metromadness.tracks.Line;
import com.unimelb.swen30006.metromadness.trains.Train.State;

/**
 *  LargeCargoTrain class extend from the train class, only start and stop and 
 *  the cargo station. Embarking passengers with cargo at cargo station. 
 * */

public class LargeCargoTrain extends Train {
	
	public static final int CARGO_CAPACITY = 1000;
	public int currentCargo;
	
	public LargeCargoTrain(Line trainLine, Station start, boolean foward, String name){
		super(trainLine, start, foward, name);
	}
	
	/**override the embark method from the train class. Embarking passenger should not exceed 
	 * both cargo and passenger capacity.
	 * */
	@Override
	public void embark(Passenger p) throws Exception{
		if(this.passengers.size()+1<=80 && this.currentCargo+p.getCargo().getWeight()<=CARGO_CAPACITY){
			this.passengers.add(p);
		}else{
			throw new Exception();
		}
	}
	
	/**
	 * override the update method from the train class
	 * */
	
	@Override
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
			if(this.pos.distance(this.station.position) < 10 && this.station.getClass() == CargoStation.class){
				this.state = State.WAITING_ENTRY;
			}else if(this.pos.distance(this.station.position) < 10 && this.station.getClass() != CargoStation.class){
				try {
					// Find the next
					Station next = this.trainLine.nextStation(this.station, this.forward);
					this.station = next;
					previousState = null;
				} catch (Exception e) {
//					e.printStackTrace();
				}
			}else {
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
	
	/**override the render method from the train class.*/
	@Override
	public void render(ShapeRenderer renderer){
		if(!this.inStation()){
			Color col = this.forward ? FORWARD_COLOUR : BACKWARD_COLOUR;
			float percentage = this.passengers.size()/10f;
			renderer.setColor(col.cpy().lerp(Color.MAROON, percentage));
			// We also get slightly bigger with passengers
			renderer.circle(this.pos.x, this.pos.y, TRAIN_WIDTH*(1+percentage));
		}
	}
	
	
}
