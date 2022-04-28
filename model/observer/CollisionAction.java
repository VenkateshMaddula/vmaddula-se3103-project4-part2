package model.observer;

public interface CollisionAction {
	
	public static int ENEMY_COLLISION = 0;
	public static int SHOOTER_COLLISION = 1;
	public static int BOMB_COLLISION = 2;
	public static int BULLET_COLLISION = 3;
	
	void addCollisionListener(CollisionActionObserver o);
	void removeCollisionListener(CollisionActionObserver o);
	void notifyListeners();

}
