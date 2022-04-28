package model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.net.NoRouteToHostException;
import java.util.ArrayList;
import java.util.Random;

import model.builder.BuilderDirector;
import model.builder.EnemyBomb;
import model.builder.GreenBomb;
import model.builder.RedBomb;
import model.strategy.ExplosionAnimation;
import model.strategy.NormalAnimation;
import view.GameBoard;

public class EnemyComposite extends GameElement {

    public static final int NROWS =2;
    public static final int NCOLS=10;
    public static final int ENEMY_SIZE=20;
    public static final int UNIT_MOVE=5;

    private ArrayList<ArrayList<GameElement>> rows;
    private ArrayList<GameElement> bombs;
    private boolean movingToRight = true;
    private Random random = new Random();
    
    private int collisionCount;
    
    //hit side wall boolean
    private boolean hitSideWall = false;

    public EnemyComposite() {
       rows = new ArrayList<>();
       bombs = new ArrayList<>();
       
       collisionCount = 0;
       
       GameElement enemy;

       for(int r=0;r <NROWS;r++){
           var oneRow = new ArrayList<GameElement>();
           rows.add(oneRow);
           for (int c=0;c<NCOLS;c++){
        	   enemy = new Enemy( c * ENEMY_SIZE *2 , r *ENEMY_SIZE *2, ENEMY_SIZE,Color.yellow,true );
        	   enemy.setAnimation(new NormalAnimation(enemy));
               oneRow.add(enemy);  
           }
       }
    }


    @Override
    public void render(Graphics2D g2) {
        for(var r:rows){
            for(var e:r){
                e.render(g2);
            }
        }

        for(var b :bombs){
            b.render(g2);
        }
        
    }

    @Override
    public void animate() {
        int dx = UNIT_MOVE;
        if(movingToRight){
            if(rightEnd() >= GameBoard.WIDTH) {
                dx = -dx;
                movingToRight = false;
                //Change in direction
                hitSideWall = true;
            }
        }else {
            dx=-dx;
            if(leftEnd() <= 0){
                dx = -dx;
                movingToRight = true;
              //Change in direction
                hitSideWall = true;
            }
        }

        for (var row:rows) {
            for( var e : row){
                e.x +=dx;
                if(hitSideWall)
                	e.y += ENEMY_SIZE;
                //animate
                e.animate();
                
            }
        }

        for(var b :bombs){
            b.animate();
        }
        
        //Reset Direction
        hitSideWall = false;
              
    }
    
    
    private int rightEnd(){
        int xEnd = -100;
        for(var row:rows){
            if(row.size() == 0) continue;
            int x= row.get(row.size() -1).x + ENEMY_SIZE;
            if(x > xEnd) xEnd = x;
        }
        return xEnd;
    }

    private int leftEnd(){
        int xEnd = 9000;
        for(var row:rows){
            if(row.size() == 0) continue;
            int x= row.get(0).x;
            if(x < xEnd) xEnd = x;
        }
        return xEnd;
    }

    public void dropBombs(){
    	BuilderDirector dir = new BuilderDirector();
    	
        for(var row: rows){
            for( var e:row){
                if(random.nextFloat() < 0.1F){
                	EnemyBomb b = new GreenBomb(e.x,e.y);
                	dir.setBuilder(b);
                	dir.createBomb();
                    bombs.add(dir.getBomb());
                }
                
                // Set unlikely frequency
                if(random.nextFloat() > 0.981F){
                	EnemyBomb b = new  RedBomb(e.x,e.y);
                	dir.setBuilder(b);
                	dir.createBomb();
                    bombs.add(dir.getBomb());
                }
            }
        }

    }

    public void removeBombsOutOfBound(){
        var remove = new ArrayList<GameElement>();
        for(var b : bombs){
            if(b.y >= GameBoard.HEIGHT){
                remove.add(b);
            }
            
        }
        bombs.removeAll(remove);
    }

    public void processCollision(Shooter shooter){
        var removeBullets = new ArrayList<GameElement>();

        for(var row:rows){
            var removeEnemies = new ArrayList<GameElement>();
            for(var enemy: row){
                for(var bullet:shooter.getWeapons()) {
                    if(enemy.collideWith(bullet)){
                    	++collisionCount;
                        removeBullets.add(bullet);
                        enemy.setAnimation(new ExplosionAnimation(enemy));
                        this.notifyListeners();
                    }
                }
                
                if(enemy.exploted) {
                	removeEnemies.add(enemy);
                }
            }
            row.removeAll(removeEnemies);

        }
        shooter.getWeapons().removeAll(removeBullets);


        var removeBombs = new ArrayList<GameElement>();
        removeBullets.clear();

        for(var b:bombs){
            for( var bullet:shooter.getWeapons()){
                if(b.collideWith(bullet)){
                    removeBombs.add(b);
                    removeBullets.add(bullet);
                }
            }
        }

        shooter.getWeapons().removeAll(removeBullets);
        bombs.removeAll(removeBombs);
    }
    
    //bombs getters
    public ArrayList<GameElement> getBombs() {
		return bombs;
	}
    
    //get enemies
    public ArrayList<ArrayList<GameElement>> getRows() {
		return rows;
	}
    
    //Get game scores
    public int getcollisionCount() {
		return collisionCount;
	}


	@Override
	public void notifyListeners() {
		for(var o : collisionObservers) {
			o.actionPerformed(ENEMY_COLLISION);
		}
		
	}
}
