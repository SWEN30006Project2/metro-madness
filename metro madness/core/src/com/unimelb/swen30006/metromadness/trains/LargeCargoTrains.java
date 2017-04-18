package com.unimelb.swen30006.metromadness.trains;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.unimelb.swen30006.metromadness.passengers.Passenger;
import com.unimelb.swen30006.metromadness.stations.Station;
import com.unimelb.swen30006.metromadness.tracks.Line;

public class LargeCargoTrains extends Train {
	
	public static final int CARGO_CAPACITY = 1000;
	public int currentCargo;
	
	public LargeCargoTrains(Line trainLine, Station start, boolean foward, String name){
		super(trainLine, start, foward, name);
	}
	
	@Override
	public void embark(Passenger p) throws Exception{
		if(this.passengers.size()+1<=80 && this.currentCargo+p.getCargo().getWeight()<=CARGO_CAPACITY){
			this.passengers.add(p);
		}else{
			throw new Exception();
		}
	}
	
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
