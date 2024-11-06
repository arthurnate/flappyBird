import java.awt.*;
import java.awt.event.*;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;


public class FlappyBird extends JPanel implements ActionListener, KeyListener {
      int boardWidth = 360;
      int boardHeight = 640;

      Image background;
      Image birdImage;
      Image topPipeImg;
      Image bottomPipeImg;

      int xOfBird = boardWidth/8;
      int yOfBird = boardHeight/2;
      int birdW = 34;
      int birdH = 24;

      class Bird{
        int x = xOfBird;
        int y = yOfBird;
        int width = birdW;
        int height = birdH;
        Image img;

        Bird(Image img){
          this.img = img;
        }
      }

      int pipeX = boardWidth;
      int pipeY = 0;
      int pipeW = 64;
      int pipeH = 512;

      class Pipe{
        int x = pipeX;
        int y = pipeY;
        int width = pipeW;
        int height = pipeH;
        Image img;
        boolean past = false;

        Pipe(Image img){
          this.img = img;
        }
      }

      Bird bird;
      int velocityX = -4;
      int velocityY = 0;
      int gravity = 1;

      ArrayList<Pipe> pipes;
      Random random = new Random();

      Timer gameLoop;
      Timer placePipesTimer;

      boolean gameOver = false;

      double score = 0;

      FlappyBird(){
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        // setBackground(Color.blue);
        setFocusable(true);
        addKeyListener(this);

        background = new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
        birdImage = new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
        topPipeImg = new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
        bottomPipeImg = new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();

        bird = new Bird(birdImage);
        pipes = new ArrayList<Pipe>();

        placePipesTimer = new Timer(1500, new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e){
            placePipes();
          }
        });

        placePipesTimer.start();

        gameLoop = new Timer(1000/60, this);
        gameLoop.start();
      }

      public void placePipes(){
        int randomPipeY = (int)(pipeY - pipeH/4 - Math.random()*(pipeH/2));
        int openingSpace = boardHeight/4;
        
        
        Pipe topPipe = new Pipe(topPipeImg);
        topPipe.y = randomPipeY;
        pipes.add(topPipe);

        Pipe bottomPipe = new Pipe(bottomPipeImg);
        bottomPipe.y = topPipe.y + pipeH + openingSpace;
        pipes.add(bottomPipe);
      }

      public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
      }
      public void draw(Graphics g){
        g.drawImage(background,0, 0, boardWidth , boardHeight,null);
        
        g.drawImage(birdImage, bird.x, bird.y, bird.width, bird.height, null);

        for(int i=0; i<pipes.size(); i++){
          Pipe pipe = pipes.get(i);
          g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
        }

        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 32));
        if(gameOver){
          g.drawString("Game Over: " + String.valueOf((int)score), 10, 35);
        }
        else{
          g.drawString(String.valueOf((int)score), 10, 35);
        }
      }

      public void move(){
        velocityY += gravity;
        bird.y += velocityY;
        bird.y = Math.max(bird.y, 0);

        for(int i=0; i<pipes.size(); i++){
          Pipe pipe = pipes.get(i);
          pipe.x += velocityX;

          if(!pipe.past && bird.x > pipe.x + pipe.width){
            pipe.past = true;
            score += 0.5;
          }
          

          if (collision(bird, pipe)){
            gameOver = true;
          }
        }

        if(bird.y > boardHeight){
          gameOver = true;
        }
      }

      public boolean collision(Bird a, Pipe b){
        return a.x < b.x + b.width && a.x + a.width > b.x && a.y < b.y + b.height && a.y + a.height > b.y;
      }
      
      @Override
      public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if(gameOver){
          placePipesTimer.stop();
          gameLoop.stop();
        }
      }

      @Override
      public void keyPressed(KeyEvent e) {
        // TODO Auto-generated method stub
        if(e.getKeyCode() == KeyEvent.VK_SPACE){
          velocityY += -9;
          if(gameOver){
            bird.y = yOfBird;
            velocityY = 0;
            pipes.clear();
            score = 0;
            gameOver = false;
            gameLoop.start();
            placePipesTimer.start();
          }
        }
      }
      @Override
      public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
      }
      @Override
      public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
      }

      
}
