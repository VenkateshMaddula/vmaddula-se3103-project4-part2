package controller;

import java.awt.event.ActionListener;
import java.util.LinkedList;

import model.Bullet;
import model.EnemyComposite;
import model.Shooter;
import view.GameBoard;

import java.awt.event.ActionEvent;

public class TimerListener implements ActionListener {

    public enum EventType {
        KEY_RIGHT,KEY_LEFT,KEY_SPACE
    }

    private LinkedList<EventType> eventQueue;

    private GameBoard gameBoard;
    private final int BOMB_DROP_FREQ = 20;
    private int frameCounter = 0;
    
    //Scores
    private int gameScores;

    public TimerListener(GameBoard gameBoard){
        this.gameBoard=gameBoard;
        eventQueue=new LinkedList<>();
        gameScores = 0;
    }

    @Override
    public void actionPerformed(ActionEvent e){
        ++frameCounter;
        //Before performing any action, confirm if game over then stop it.
        if(gameBoard.isGameOver()) gameBoard.stopGame();
        
        update();
        processEventQueue();
        processCollision();
        gameBoard.getCanvas().repaint();
        
    }   

    private void processEventQueue(){
              while(!eventQueue.isEmpty()){
                  var e=eventQueue.getFirst();
                  eventQueue.removeFirst();
                  Shooter shooter = gameBoard.getShooter();
                  if(shooter == null) return;

                  switch(e) {
                    case KEY_LEFT:
                      shooter.moveLeft();
                      break;
                    case KEY_RIGHT:
                      shooter.moveRight();
                      break;
                    case KEY_SPACE:
                      if (shooter.canFireMoreBullet())
                        shooter.getWeapons().add(new Bullet(shooter.x, shooter.y));
                      break;
                  }
              }

              if(frameCounter == BOMB_DROP_FREQ) {
                  gameBoard.getEnemyComposite().dropBombs();
                  frameCounter = 0;
              }
    }

    private void processCollision(){
        var shooter = gameBoard.getShooter();
        var enemyComposite = gameBoard.getEnemyComposite();
        
        //Before processing collision: ensure that no 
        //Available elements (Shooter or Enemy)
        if(shooter.getComponents().isEmpty()) {
        	gameBoard.endGame(gameScores, GameBoard.NO_SHOOTERS);
        }else if(enemyComposite.getRows().get(0).isEmpty() && enemyComposite.getRows().get(1).isEmpty())
        	gameBoard.endGame(gameScores, GameBoard.NO_ENEMIES);
        
        else {
        	shooter.removeBulletsOutOfBound();
            enemyComposite.removeBombsOutOfBound();
            enemyComposite.processCollision(shooter);
            shooter.processCollision(enemyComposite.getBombs());
        }
        
        //process lowest enemy position
        for(var row: enemyComposite.getRows()) {
        	for (var e: row) {
        		if(e.y >= (GameBoard.HEIGHT - EnemyComposite.ENEMY_SIZE))
        			//end game
        			gameBoard.endGame(gameScores, GameBoard.ENEMY_ON_SURFACE);
        	}
        }
        
        gameScores = enemyComposite.getcollisionCount() * GameBoard.SCORE;
        
    }
    
    public int getGameScores() {
		return gameScores;
	}
    
    private void update(){
        for(var e: gameBoard.getCanvas().getGameElements()){
             e.animate();
        }
        
    }

    public LinkedList<EventType> getEventQueue(){
        return eventQueue;
    }
}
