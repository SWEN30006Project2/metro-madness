package com.unimelb.swen30006.metromadness;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

// Imports for parsing XML files
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;


// The things we are generating
import com.unimelb.swen30006.metromadness.routers.PassengerRouter;
import com.unimelb.swen30006.metromadness.routers.SimpleRouter;
import com.unimelb.swen30006.metromadness.stations.ActiveStation;
import com.unimelb.swen30006.metromadness.stations.CargoStation;
import com.unimelb.swen30006.metromadness.stations.Station;
import com.unimelb.swen30006.metromadness.tracks.Line;
import com.unimelb.swen30006.metromadness.trains.BigPassengerTrain;
import com.unimelb.swen30006.metromadness.trains.LargeCargoTrain;
import com.unimelb.swen30006.metromadness.trains.SmallCargoTrain;
import com.unimelb.swen30006.metromadness.trains.SmallPassengerTrain;
import com.unimelb.swen30006.metromadness.trains.Train;

/*
 * This class is used to read the information from the XML file and 
 * generate the instances of Train, Station and Line
 */
public class MapReader implements StandardMapReader{
	
	private ArrayList<Train> trains;
	private HashMap<String, Station> stations;
	private HashMap<String, Line> lines;

	private boolean processed;
	private String filename;

	/*
	 * Constructor
	 * @param file name
	 */
	public MapReader(String filename){
		this.trains = new ArrayList<Train>();
		this.stations = new HashMap<String, Station>();
		this.lines = new HashMap<String, Line>();
		this.filename = filename;
		this.processed = false;
	}

	/*
	 * Generate the instances of Train, Station and Line
	 */
	public void process(){
		try {
			// Build the doc factory
			FileHandle file = Gdx.files.internal("../core/assets/maps/melbourne.xml");			
//			FileHandle file = Gdx.files.internal("../core/assets/maps/world.xml");
			XmlReader reader = new XmlReader();
			Element root = reader.parse(file);
			
			// Process stations
			Element stations = root.getChildByName("stations");
			Array<Element> stationList = stations.getChildrenByName("station");
			for(Element e : stationList){
				Station s = processStation(e);
				this.stations.put(s.name, s);
			}
			
			// Process Lines
			Element lines = root.getChildByName("lines");
			Array<Element> lineList = lines.getChildrenByName("line");
			for(Element e : lineList){
				Line l = processLine(e);
				this.lines.put(l.name, l);
			}

			// Process Trains
			Element trains = root.getChildByName("trains");
			Array<Element> trainList = trains.getChildrenByName("train");
			for(Element e : trainList){
				Train t = processTrain(e);
				this.trains.add(t);
			}
			
			this.processed = true;
			
		} catch (Exception e){
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	/*
	 * get access to the instances of the Train
	 */
	public Collection<Train> getTrains(){
		if(!this.processed) { this.process(); }
		return this.trains;
	}
	
	/*
	 * get access to the instances of the Line 
	 */
	public Collection<Line> getLines(){
		if(!this.processed) { this.process(); }
		return this.lines.values();
	}
	
	/*
	 * get access to the instances of the Station 
	 */
	public Collection<Station> getStations(){
		if(!this.processed) { this.process(); }
		return this.stations.values();
	}

	/*
	 * generate the instance of train
	 * @param the element got from the XML file
	 */
	private Train processTrain(Element e){
		// Retrieve the values
		String type = e.get("type");
		String line = e.get("line");
		String start = e.get("start");
		String name = e.get("name");
		boolean dir = e.getBoolean("direction");

		// Retrieve the lines and stations
		Line l = this.lines.get(line);
		Station s = this.stations.get(start);
		
		// Make the train
		if(type.equals("BigPassenger")){
			return new BigPassengerTrain(l,s,dir,name);
		} else if (type.equals("SmallPassenger")){
			return new SmallPassengerTrain(l,s,dir,name);
		} else if (type.equals("SmallCargo")) {
			return new SmallCargoTrain(l, s, dir, name);
		}else if (type.equals("BigCargo")) {
			return new LargeCargoTrain(l, s, dir, name);
		}else {
			return new Train(l, s, dir,name);
		}
	}

	/*
	 * generate the instance of the Station class
	 * @param the element got from the XML file
	 */
	private Station processStation(Element e){
		String type = e.get("type");
		String name = e.get("name");
		int x_loc = e.getInt("x_loc")/8;
		int y_loc = e.getInt("y_loc")/8;
		String router = e.get("router");
		PassengerRouter r = createRouter(router);
		if(type.equals("Active")){
			int maxPax = e.getInt("max_passengers");
			return new ActiveStation(x_loc, y_loc, r, name, maxPax);
		}else if (type.equals("Cargo")) {
			int maxPax = e.getInt("max_passengers");
			return new CargoStation(x_loc, y_loc, r, name, maxPax);
		} else if (type.equals("Passive")){
			return new Station(x_loc, y_loc, r, name);
		} else{
			return new Station(x_loc,y_loc,r,name);
		}
	}

	
	/*
	 * generate the instance of the Line class
	 * @param the element got from the XML file
	 */
	private Line processLine(Element e){
		Color stationCol = extractColour(e.getChildByName("station_colour"));
		Color lineCol = extractColour(e.getChildByName("line_colour"));
		String name = e.get("name");
		Line l = new Line(stationCol, lineCol, name);
		
		Array<Element> stations = e.getChildrenByNameRecursively("station");
		for(Element s: stations){
			Station station = this.stations.get(s.get("name"));
			boolean twoWay = s.getBoolean("double");
			l.addStation(station, twoWay);
		}
		return l;
	}
	
	/*
	 * generate the passenger router
	 * @param the type of the router
	 */
	private PassengerRouter createRouter(String type){
		if(type.equals("simple")){
			return new SimpleRouter();
		}
		return null;
	}
	
	/*
	 * generate the color
	 * @param the element got from the XML file
	 */
	private Color extractColour(Element e){
		float red = e.getFloat("red")/255f;
		float green = e.getFloat("green")/255f;
		float blue = e.getFloat("blue")/255f;
		return new Color(red, green, blue, 1f);
	}

}
