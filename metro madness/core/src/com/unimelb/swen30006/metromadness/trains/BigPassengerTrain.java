package com.unimelb.swen30006.metromadness.trains;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.unimelb.swen30006.metromadness.passengers.Passenger;
import com.unimelb.swen30006.metromadness.stations.Station;
import com.unimelb.swen30006.metromadness.tracks.Line;


/**
 * BigPassengerTrain class extends from the train class, have larger passenger capacity.
 * */

public class BigPassengerTrain extends Train {

	public static final int PASSENGER_CAPACITY =  80;
	
	public BigPassengerTrain(Line trainLine, Station start, boolean forward, String name) {
		super(trainLine, start, forward, name);
	}
	
	/**override the embark method of the train class*/
	@Override
	public void embark(Passenger p) throws Exception {
		if(this.passengers.size() > PASSENGER_CAPACITY){
			throw new Exception();
		}
		this.passengers.add(p);
	}
	
	
	/**override the render method of the train calss*/
	@Override
	public void render(ShapeRenderer renderer){
		if(!this.inStation()){
			Color col = this.forward ? FORWARD_COLOUR : BACKWARD_COLOUR;
			float percentage = this.passengers.size()/20f;
			renderer.setColor(col.cpy().lerp(Color.LIGHT_GRAY, percentage));
			renderer.circle(this.pos.x, this.pos.y, TRAIN_WIDTH*(1+percentage));
		}
	}

}
