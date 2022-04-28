package view;

import java.awt.Container;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import controller.KeyController;
import controller.TimerListener;
import model.EnemyComposite;
import model.GameElement;
import model.Shooter;
import model.ShooterElement;
import model.observer.CollisionActionObserver;
import model.observer.CollisionAction;

public class GameBoard implements CollisionActionObserver{

    public static final int WIDTH=600;
    public static final int HEIGHT=300;
    
    public static final int NO_SHOOTERS = 1;
    public static final int NO_ENEMIES = 0;
    public static final int ENEMY_ON_SURFACE = 2;
    public static final int SCORE = 10;

    public static final int FPS =20;
    public static final int DELAY = 1000/FPS; 

    private JFrame window;

    private MyCanvas canvas;
    private Shooter shooter;
    private EnemyComposite enemyComposite;
    private Timer timer;
    private TimerListener timerListener;
    
    private boolean gameOver;
    private ArrayList<GameElement> textMessages = new ArrayList<>();
    
    //Scores label
    private JLabel scoreLabel;
    
    int gamescores;

    public GameBoard(JFrame window){
            this.window=window;
    }
    
    public void init(){
    	
    	GameBoard self = this;
    	gamescores = 0;

        Container cp = window.getContentPane();
        canvas = new MyCanvas(this,WIDTH,HEIGHT);
        cp.add(BorderLayout.CENTER,canvas);
        canvas.addKeyListener(new KeyController(this));
        canvas.requestFocusInWindow();
        canvas.setFocusable(true);

        JButton startButton = new JButton("Start");
        JButton quitButton = new JButton("Quit");
        startButton.setFocusable(false);
        quitButton.setFocusable(false);
        
        scoreLabel = new JLabel("Score: 0");

        JPanel southPanel = new JPanel();
        southPanel.add(startButton);
        southPanel.add(quitButton);
        southPanel.add(scoreLabel);
        cp.add(BorderLayout.SOUTH,southPanel);       
 
        canvas.getGameElements().add(new TextDraw("click <start> to Play",100,100,Color.yellow,30));
        //shooter = new Shooter(GameBoard.WIDTH/2,GameBoard.HEIGHT-ShooterElement.SIZE);
        
        timerListener = new TimerListener(this);
        timer = new Timer(DELAY,timerListener);
        

        startButton.addActionListener(event-> {
        	gameOver = false;
        	textMessages.clear();
            shooter = new Shooter(GameBoard.WIDTH/2, GameBoard.HEIGHT - ShooterElement.SIZE);
            enemyComposite = new EnemyComposite();
            enemyComposite.addCollisionListener(self);
            canvas.getGameElements().clear();
            canvas.getGameElements().add(shooter);
            canvas.getGameElements().add(enemyComposite);   
            timer.start();
        });

        quitButton.addActionListener(event -> System.exit(0));
    
        
    }

    public MyCanvas getCanvas(){
        return canvas;
    }

    public Timer getTimer() {
        return timer;
    }

    public TimerListener getTimerListener() {
        return timerListener;
    }

    public Shooter getShooter(){
        return shooter;
    }

    public EnemyComposite getEnemyComposite(){
        return enemyComposite;
    }
    
    public void endGame(int scores, int reason) {
    	//Clear first text messages array
    	textMessages.clear();
    	
    	if(reason == NO_SHOOTERS) {
    		
    		textMessages.add(new TextDraw("Your shooters destroyed. You can't win",50,HEIGHT/2 - 30,Color.red,20));
    		textMessages.add(new TextDraw("You Lost! Score: " + scores,50,HEIGHT/2,Color.red,30));
    		
    	}else if (reason == NO_ENEMIES) {
    		
    		textMessages.add(new TextDraw("All enemies destroyed",50,HEIGHT/2-30,Color.blue,20));
    		textMessages.add(new TextDraw("You Won! Score: " + scores,50,HEIGHT/2,Color.blue,30));
    		
    	}else if(reason == ENEMY_ON_SURFACE) {
    		
    		textMessages.add(new TextDraw("Enemies at the Bottom ",50,HEIGHT/2-30,Color.red,20));
    		textMessages.add(new TextDraw("You Lost! Score: " + scores,50,HEIGHT/2,Color.red,30));
    		
    	}else 
    		textMessages.add(new TextDraw("Game error!. Score: " + scores,50,HEIGHT/2,Color.red,30));
    	
    	//Finally set game over to true
    	gameOver = true;
    }
    
    public void stopGame() {
    	canvas.getGameElements().clear();
    	canvas.getGameElements().addAll(textMessages);
    	timer.stop();
    }
    
    public boolean isGameOver() {
		return gameOver;
	}
    

	@Override
	public void actionPerformed(int action) {
		if(action == CollisionAction.SHOOTER_COLLISION) {
			gamescores -= 5;
			scoreLabel.setText("Score: " + gamescores);
		}
		
		if(action == CollisionAction.ENEMY_COLLISION) {
			gamescores += 10;
			scoreLabel.setText("Score: " + gamescores);
		}
		
	}
}
