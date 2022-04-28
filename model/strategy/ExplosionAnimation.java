package model.strategy;

import java.awt.Color;

import model.GameElement;

public class ExplosionAnimation implements BehaviorAnimation{
	
	
	private int effectTimer;
	private GameElement e;
	
	public ExplosionAnimation(GameElement e) {
		effectTimer = 0;
		this.e = e;
	}

	@Override
	public void update() {
		e.color = Color.red;
		
	}

	@Override
	public void monitor() {
		if(effectTimer >= BehaviorAnimation.MAX_EFFECT_TIME)
			e.exploted = true;
		effectTimer++;
		
	}
	

}
