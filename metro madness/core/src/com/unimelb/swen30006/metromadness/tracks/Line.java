package com.unimelb.swen30006.metromadness.tracks;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.unimelb.swen30006.metromadness.stations.CargoStation;
import com.unimelb.swen30006.metromadness.stations.Station;

public class Line {
	
	// The colour of this line
	public Color lineColour;
	public Color trackColour;
	
	// The name of this line
	public String name;
	// The stations on this line
	public ArrayList<Station> stations;
	// The tracks on this line between stations
	public ArrayList<Track> tracks;
		
	/*
	 * Constructor
	 * @param the color of the stations on this line
	 * @param the color of the line
	 * @param the name of the line
	 */
	public Line(Color stationColour, Color lineColour, String name){
		// Set the line colour
		this.lineColour = stationColour;
		this.trackColour = lineColour;
		this.name = name;
		
		// Create the data structures
		this.stations = new ArrayList<Station>();
		this.tracks = new ArrayList<Track>();
	}
	
	/*
	 * add a station to this line, and generate the track that connects this new station
	 * @param the station that should be added to this line
	 * @param double track is true, normal track is false
	 */
	public void addStation(Station s, Boolean two_way){
		// We need to build the track if this is adding to existing stations
		if(this.stations.size() > 0){
			// Get the last station
			Station last = this.stations.get(this.stations.size()-1);
			
			// Generate a new track
			Track t;
			if(two_way){
				t = new DualTrack(last.position, s.position, this.trackColour);
			} else {
				t = new Track(last.position, s.position, this.trackColour);
			}
			this.tracks.add(t);
		}
		
		// Add the station
		s.registerLine(this);
		this.stations.add(s);
	}
	
	@Override
	public String toString() {
		return "Line [lineColour=" + lineColour + ", trackColour=" + trackColour + ", name=" + name + "]";
	}

	/*
	 * check if a station is at the end of the line
	 * @param the station
	 */
	public boolean endOfLine(Station s) throws Exception{
		if(this.stations.contains(s)){
			int index = this.stations.indexOf(s);
			return (index==0 || index==this.stations.size()-1);
		} else {
			throw new Exception();
		}
	}

	/*
	 * Given the station and the direction, retrieve the next track
	 * @param the current station
	 * @param the direction
	 */
	public Track nextTrack(Station currentStation, boolean forward) throws Exception {
		if(this.stations.contains(currentStation)){
			// Determine the track index
			int curIndex = this.stations.lastIndexOf(currentStation);
			// Increment to retrieve
			if(!forward){ curIndex -=1;}
			
			// Check index is within range
			if((curIndex < 0) || (curIndex > this.tracks.size()-1)){
				throw new Exception();
			} else {
				return this.tracks.get(curIndex);
			}
			
		} else {
			throw new Exception();
		}
	}
	
	/*
	 * Given the station and the direction, retrieve the next station
	 * @param the current station
	 * @param the direction
	 */
	public Station nextStation(Station s, boolean forward) throws Exception{
		if(this.stations.contains(s)){
			int curIndex = this.stations.lastIndexOf(s);
			if(forward){ curIndex+=1;}else{ curIndex -=1;}
			
			// Check index is within range
			if((curIndex < 0) || (curIndex > this.stations.size()-1)){
				throw new Exception();
			} else {
				return this.stations.get(curIndex);
			}
		} else {
			throw new Exception();
		}
	}
	
	/*
	 * render the line
	 * @param shape renderer
	 */
	public void render(ShapeRenderer renderer){
		// Set the color to our line
		renderer.setColor(trackColour);
	
		// Draw all the track sections
		for(Track t: this.tracks){
			t.render(renderer);
		}	
	}
	
	/*
	 * check if this line contains two more cargo stations
	 * @return if this line contains two more cargo stations, return true; else return false
	 */
	public boolean twoMoreCargoStation(){
		int count = 0;
		for(Station station : stations){
			if(station.getClass() == CargoStation.class)
				count++;
			if(count == 2)
				return true;
		}
		return false;
	}
	
}
