package com.unimelb.swen30006.metromadness.passengers;

import java.util.ArrayList;
import java.util.Random;

import com.unimelb.swen30006.metromadness.stations.Station;
import com.unimelb.swen30006.metromadness.routers.*;

public class Passenger {

	final public int id;
    static final private int WITHOUT_CARGO = 0;
	public Station beginning;
	public Station destination;
	public float travelTime;
	public boolean reachedDestination;
	public Cargo cargo;
	public PassengerRouter router;
	
	public Passenger(int id, Random random, Station start, Station end, PassengerRouter router){
		this.id = id;
		this.beginning = start;
		this.destination = end;
		this.reachedDestination = false;
		this.travelTime = 0;
		this.cargo = generateCargo(random, start);
		this.router = router;
	}
	
	public void update(float time){
		if(!this.reachedDestination){
			this.travelTime += time;
		}
	}
	public Cargo getCargo(){
		return cargo;
	}
	public Cargo generateCargo(Random random, Station station){
		if(station.isCargoStation())
		    return new Cargo(random.nextInt(51));
		else
			return new Cargo(WITHOUT_CARGO);
	}
	
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

	public boolean shouldLeave(Station currentStation){
		return this.router.shouldLeave(currentStation, this);
	}
}
