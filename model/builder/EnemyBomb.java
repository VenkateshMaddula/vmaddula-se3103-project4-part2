package model.builder;

import model.Bomb;

public abstract class EnemyBomb {
	
	Bomb b;
	int x;
	int y;
	
	public Bomb getB() {
		return b;
	}
	
	public void createBomb() {
		b = new Bomb(x, y);
	}
	
	public abstract void buildShape();
    public abstract void buildColor();

}
