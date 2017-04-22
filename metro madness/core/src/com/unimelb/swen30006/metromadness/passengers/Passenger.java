package com.unimelb.swen30006.metromadness.passengers;

import java.util.Random;

import com.unimelb.swen30006.metromadness.stations.CargoStation;
import com.unimelb.swen30006.metromadness.stations.Station;
import com.unimelb.swen30006.metromadness.routers.*;

/*
 * This class is to simulate passengers
 */
public class Passenger {
	final public int id;
    static final private int WITHOUT_CARGO = 0;
	public Station beginning;
	public Station destination;
	public float travelTime;
	public boolean reachedDestination;
	public Cargo cargo;
	public PassengerRouter router;
	
	/*
	 * Constructor
	 * @param the passenger ID
	 * @param the current station
	 * @param the target station
	 * @param the passenger router
	 */
	public Passenger(int id, Random random, Station start, Station end, PassengerRouter router){
		this.id = id;
		this.beginning = start;
		this.destination = end;
		this.reachedDestination = false;
		this.travelTime = 0;
		this.cargo = generateCargo(random, start);
		this.router = router;
	}
	
	/*
	 * update the passenger's travel time
	 * @param the time cost between two stations
	 */
	public void update(float time){
		if(!this.reachedDestination){
			this.travelTime += time;
		}
	}
	
	/*
	 * get the passenger's cargo
	 * @return the passenger's cargo
	 */
	public Cargo getCargo(){
		return cargo;
	}
	
	/*
	 * random generate the cargo weight
	 * @param random
	 * @param current station
	 * @return cargo
	 */
	public Cargo generateCargo(Random random, Station station){
		if(station.getClass() == CargoStation.class)
		    return new Cargo(random.nextInt(51));
		else
			return new Cargo(WITHOUT_CARGO);
	}
	
	/*
	 * This class is for cargo
	 */
	public class Cargo{
		private int weight;
		
		public Cargo(int weight){
			this.setWeight(weight);
		}

		public int getWeight() {
			return weight;
		}

		public void setWeight(int weight) {
			this.weight = weight;
		}
	}

	/*
	 * check if passenger should leave
	 * @param current station
	 * @return if the passenger should disembark, return true; otherwise false
	 */
	public boolean shouldLeave(Station currentStation){
		return this.router.shouldLeave(currentStation, this);
	}
}
