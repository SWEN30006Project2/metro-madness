package com.unimelb.swen30006.metromadness.passengers;

import java.util.ArrayList;
import java.util.Random;

import com.unimelb.swen30006.metromadness.stations.CargoStation;
import com.unimelb.swen30006.metromadness.stations.Station;
import com.unimelb.swen30006.metromadness.tracks.Line;

public class CargoPassengerGenerator implements PassengerGeneratorAdapter {
	// Passenger id generator
		static protected int idGen = 1;
		
		
		// The station that passengers are getting on
		public Station currentStation;
		// The line they are travelling on
		public ArrayList<Line> lines;
		
		// The max volume
		public float maxVolume;
	public CargoPassengerGenerator(Station station, ArrayList<Line> lines, float max){
		this.currentStation = station;
		this.lines = lines;
		this.maxVolume = max;
	}
	
	public Passenger[] generatePassengers(){
		int count = random.nextInt(4)+1;
		Passenger[] passengers = new Passenger[count];
		for(int i=0; i<count; i++){
			passengers[i] = generatePassenger(random);
		}
		return passengers;
	}


	public Passenger generatePassenger(Random random){
		// Pick a random station from the line
		ArrayList<Line> twoMoreCargoStationsLines = new ArrayList<Line>();
		for(Line line : lines){
			if(line.twoMoreCargoStation())
				twoMoreCargoStationsLines.add(line);
		}
		Line line = twoMoreCargoStationsLines.get(random.nextInt(this.lines.size()));
		
		int current_station = line.stations.indexOf(this.currentStation);
		boolean forward = random.nextBoolean();
		
		// If we are the end of the line then set our direction forward or backward
		if(current_station == 0){
			forward = true;
		}else if (current_station == line.stations.size()-1){
			forward = false;
		}
		
		ArrayList<Station> cargoStations = new ArrayList<Station>(); 
		
		if(forward == true){
			for(int i =current_station+1; i<line.stations.size(); i++){
				if(line.stations.get(i).getClass() == CargoStation.class)
					cargoStations.add(line.stations.get(i));
			}
			if(cargoStations.isEmpty())
				forward = false;
		}
		
        if(forward == false){
			for(int i =current_station-1; i>=0; i--){
				if(line.stations.get(i).getClass() == CargoStation.class)
					cargoStations.add(line.stations.get(i));
			}
			if(cargoStations.isEmpty())
				forward = true;
		}
		// Find the station
		int index = random.nextInt(cargoStations.size());

		Station targetStation = cargoStations.get(index);
		
		return new Passenger(idGen++, random, currentStation, targetStation, currentStation.router);
	}
}
