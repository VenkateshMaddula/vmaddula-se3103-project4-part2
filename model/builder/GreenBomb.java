package model.builder;

import model.Bomb;
import java.awt.Color;

public class GreenBomb extends EnemyBomb{
	
	public GreenBomb(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public void buildShape() {
		b.height = Bomb.SIZE + 2;
		b.width = Bomb.SIZE;
		
	}

	@Override
	public void buildColor() {
		b.color = Color.green;
		
	}

}
