package com.unimelb.swen30006.metromadness.passengers;

import java.util.ArrayList;
import java.util.Random;

import com.unimelb.swen30006.metromadness.stations.Station;
import com.unimelb.swen30006.metromadness.tracks.Line;

/*
 * This class is used to generate passengers that at Active stations
 */
public class PassengerGenerator extends PassengerGeneratorAdapter{
	// The station that passengers are getting on
	public Station currentStation;
	// The line they are travelling on
	public ArrayList<Line> lines;
	// The max volume
	public float maxVolume;
	
	/*
	 * Constructor
	 * @param current station, lines across this station, the maximum volume of the station
	 */
	public PassengerGenerator(Station station, ArrayList<Line> lines, float max){
		this.currentStation = station;
		this.lines = lines;
		this.maxVolume = max;
	}
	
	/*
	 * generate passengers
	 * @return a array of passengers
	 */
	public Passenger[] generatePassengers(){
		int count = random.nextInt(4)+1;
		Passenger[] passengers = new Passenger[count];
		for(int i=0; i<count; i++){
			passengers[i] = generatePassenger(random);
		}
		return passengers;
	}
	
	/*
	 * generate a single passenger
	 * @param random
	 * @return a single passenger
	 */
	public Passenger generatePassenger(Random random){
		// Pick a random station from the line
		Line l = this.lines.get(random.nextInt(this.lines.size()));
		int current_station = l.stations.indexOf(this.currentStation);
		boolean forward = random.nextBoolean();
		
		// If we are the end of the line then set our direction forward or backward
		if(current_station == 0){
			forward = true;
		} else if (current_station == l.stations.size()-1){
			forward = false;
		}
		
		// Find the station
		int index = 0;
		
		if (forward){
			index = random.nextInt(l.stations.size()-1-current_station) + current_station + 1;
		} else {
			index = current_station - 1 - random.nextInt(current_station);
		}
		Station targetStation = l.stations.get(index);
		
		return new Passenger(idGen++, random, currentStation, targetStation, currentStation.router);
	}
}
