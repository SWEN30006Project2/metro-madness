package com.unimelb.swen30006.metromadness;

import java.util.Collection;

import com.unimelb.swen30006.metromadness.stations.Station;
import com.unimelb.swen30006.metromadness.tracks.Line;
import com.unimelb.swen30006.metromadness.trains.Train;

public interface StandardMapReader {
	public Collection<Train> getTrains();
	
	public Collection<Line> getLines();
	
	public Collection<Station> getStations();
}
