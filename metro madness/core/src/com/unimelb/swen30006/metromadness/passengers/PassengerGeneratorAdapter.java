package com.unimelb.swen30006.metromadness.passengers;

import java.util.Random;

/*
 * This interface is used to adapt different passenger generators
 */
public interface PassengerGeneratorAdapter {
    static final Random random = new Random(30006);
	public Passenger[] generatePassengers();
	public Passenger generatePassenger(Random random);
}
