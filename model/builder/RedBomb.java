package model.builder;

import model.Bomb;
import java.awt.Color;

public class RedBomb extends EnemyBomb{
	
	public RedBomb(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public void buildShape() {
		b.height = Bomb.SIZE*2;
		b.width = Bomb.SIZE*2;
		
	}

	@Override
	public void buildColor() {
		b.color = Color.red;
		
	}

}
