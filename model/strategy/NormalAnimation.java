package model.strategy;

import model.GameElement;

public class NormalAnimation implements BehaviorAnimation{
	
	private GameElement e;
	
	public NormalAnimation(GameElement e) {
		this.e = e;
	}

	@Override
	public void update() {
		e.exploted = false;
		
	}

	@Override
	public void monitor() {
		//Everything handled at each model.
		
	}

}
