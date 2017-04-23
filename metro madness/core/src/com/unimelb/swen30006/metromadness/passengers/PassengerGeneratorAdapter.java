package com.unimelb.swen30006.metromadness.passengers;

import java.util.Random;

/*
 * This interface is used to adapt different passenger generators
 */
public abstract class PassengerGeneratorAdapter {
	protected static int idGen = 1;
    protected static Random random = new Random(30006);
	public abstract Passenger[] generatePassengers();
	public abstract Passenger generatePassenger(Random random);
}
