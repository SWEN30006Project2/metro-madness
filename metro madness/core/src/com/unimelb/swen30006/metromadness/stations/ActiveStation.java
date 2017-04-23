package com.unimelb.swen30006.metromadness.stations;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.unimelb.swen30006.metromadness.passengers.Passenger;
import com.unimelb.swen30006.metromadness.passengers.PassengerGenerator;
import com.unimelb.swen30006.metromadness.routers.PassengerRouter;
import com.unimelb.swen30006.metromadness.tracks.Line;
import com.unimelb.swen30006.metromadness.trains.Train;

/*
 * This class is for active station, which is a subclass of the Station class.
 * Passengers generated at this station will have zero cargos 
 */
public class ActiveStation extends Station {
	// Logger
	private static Logger logger = LogManager.getLogger();
	//passenger generator
	public PassengerGenerator passengerGenerator;
	public ArrayList<Passenger> waiting;
	public float maxVolume;
	
	/*
	 * Constructor
	 * @param x value
	 * @param y value
	 * @param passenger router
	 * @param station name
	 * @param maximum volume
	 */
	public ActiveStation(float x, float y, PassengerRouter router,String name, float maxPax) {
		super(x, y, router, name);
		this.waiting = new ArrayList<Passenger>();
		this.passengerGenerator = new PassengerGenerator(this, this.lines, maxPax);
		this.maxVolume = maxPax;
	}
	
	/*
	 * add the train into the station
	 * @param the arrived train
	 */
	@Override
	public void arrivedTrain(Train train) throws Exception {
		if(trains.size() >= PLATFORMS){
			throw new Exception();
		} else {
			// Add the train
			this.trains.add(train);
			// Add the waiting passengers
			Iterator<Passenger> pIter = this.waiting.iterator();
			while(pIter.hasNext()){
				Passenger p = pIter.next();
				try {
					logger.info("Passenger "+p.id+" carrying "+p.getCargo().getWeight() +" kg cargo embarking at "+this.name+" heading to "+p.destination.name);
					train.embark(p);
					pIter.remove();
				} catch (Exception e){
					// Do nothing, already waiting
					break;
				}
			}
			
			//Do not add new passengers if there are too many already
			if(this.waiting.size() > maxVolume){
				return;
			}
			// Add the new passenger
			Passenger[] ps = this.passengerGenerator.generatePassengers();
			for(Passenger p: ps){
				try {
					logger.info("Passenger "+p.id+" carrying "+p.getCargo().getWeight() +" kg embarking at "+this.name+" heading to "+p.destination.name);
					train.embark(p);
				} catch(Exception e){
					this.waiting.add(p);
				}
			}
		}
	}
	
	/*
	 * render the station
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
		if(this.waiting.size() > 0){
			c = Color.RED;
		}
		
		renderer.setColor(c);
		renderer.circle(this.position.x, this.position.y, radius, NUM_CIRCLE_STATMENTS);		
	}
}
