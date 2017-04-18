package com.unimelb.swen30006.metromadness.passengers;

import java.util.Random;

public interface PassengerGeneratorAdapter {
	static final Random random = new Random(30006);
	public Passenger[] generatePassengers();
	public Passenger generatePassenger(Random random);
}
