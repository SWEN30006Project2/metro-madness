package com.unimelb.swen30006.metromadness.routers;

import com.unimelb.swen30006.metromadness.passengers.Passenger;
import com.unimelb.swen30006.metromadness.stations.Station;

/*
 * The interface for passenger router, which is used to control the move of passengers
 */
public interface PassengerRouter {

	public boolean shouldLeave(Station current, Passenger p);
	
}
