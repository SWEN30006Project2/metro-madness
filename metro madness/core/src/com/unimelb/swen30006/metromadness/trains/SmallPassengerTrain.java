package com.unimelb.swen30006.metromadness.trains;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.unimelb.swen30006.metromadness.passengers.Passenger;
import com.unimelb.swen30006.metromadness.stations.Station;
import com.unimelb.swen30006.metromadness.tracks.Line;

/**
 * this class extends from the train class, have 10 passengers capacity
 * */

public class SmallPassengerTrain extends Train {
	
	public static final int PASSENGER_CAPACITY =  10;

	public SmallPassengerTrain(Line trainLine, Station start, boolean forward, String name) {
		super(trainLine, start, forward, name);
	}

	/**override the embark method from the train class*/
	@Override
	public void embark(Passenger p) throws Exception {
		if(this.passengers.size() > PASSENGER_CAPACITY ){
			throw new Exception();
		}
		this.passengers.add(p);
	}
	
	/**override the render method from the train class*/
	@Override
	public void render(ShapeRenderer renderer){
		if(!this.inStation()){
			Color col = this.forward ? FORWARD_COLOUR : BACKWARD_COLOUR;
			float percentage = this.passengers.size()/10f;
			renderer.setColor(col.cpy().lerp(Color.MAROON, percentage));
			// We also get slightly bigger with passengers
			renderer.circle(this.pos.x, this.pos.y, TRAIN_WIDTH*(1+percentage));
		}
	}

}
