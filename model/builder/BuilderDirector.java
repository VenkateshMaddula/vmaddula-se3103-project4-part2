package model.builder;

import model.Bomb;

public class BuilderDirector {
	
	private EnemyBomb builder;
	
	public void setBuilder(EnemyBomb builder) {
		this.builder = builder;
	}
	
	public Bomb getBomb() {
		return builder.getB();
	}
	
    public void createBomb() {
        builder.createBomb();
        builder.buildShape();
        builder.buildColor();
    }

}
