package com.jebbymaze.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.audio.Music;

import java.util.concurrent.TimeUnit;

public class jebbyMaze extends ApplicationAdapter {
  //while (!Gdx.input.isKeyPressed(Keys.RIGHT)) {}
	SpriteBatch batch;
	Sprite sprite;
	Sprite sprite2;
	Sprite sprite3;
	Sprite jukeboxSprite;
	Sprite amitySprite;
	Sprite dadSprite;
	Sprite laikaSprite;
	World world;
	Texture img;
	Texture img2;
	Texture img3;
	Texture jukeboxImg;
	Texture amityImg;
	Texture dadImg;
	Texture laikaImg;
	Body body;
	Body body2;
	long frameIdDelta = 0;
	int xPos = 1;
	int yPos = 1;
	long updateInterval = 10;
	int subStep;
	boolean drawReady;
	int xPosPrev;
	int yPosPrev;
  int direction=1;
  int mazeDifficulty=30; // maze size really
  int mazeSize=(mazeDifficulty*2)+1;
	int[][] fieldMatrix = new int[mazeSize][mazeSize];
    int[] xHistory = new int [1000];
    int[] yHistory = new int [1000];
    private int[] solutionTreeX = new int [1000];
    private int[] solutionTreeY = new int [1000];
  private Music rainMusic;
    boolean helper=false;
    
	@Override
	public void create () {
		batch = new SpriteBatch();
        img3 = new Texture("archie.jpg");
        img2 = new Texture("jeb_cropped.jpg");
        img = new Texture("wall1.jpg");
        jukeboxImg = new Texture("jukeboxR.jpg");
        amityImg = new Texture("amity.jpg");
        dadImg = new Texture("dad.jpg");
        laikaImg = new Texture("laika.jpg");
        sprite = new Sprite(img);
        sprite2 = new Sprite(img2);
        sprite3 = new Sprite(img3);
        jukeboxSprite = new Sprite(jukeboxImg);
        amitySprite = new Sprite(amityImg);
        dadSprite = new Sprite(dadImg);
        laikaSprite = new Sprite(laikaImg);
 		
        createMaze();
        world = new World(new Vector2(0, 0), true);
        //rainMusic = Gdx.audio.newMusic(Gdx.files.internal("09 Home.mp3"));
        //rainMusic.setLooping(true);
        //rainMusic.play();
	}
	
	public void createMaze(){
  	int noOnes=0;
  	int noTwos=0;
  	int noThrees=0;
  	int noFours=0;
    int mazeLenX;
    int mazeLenY;
    int matrixXPosition=1;
    int matrixYPosition=1;
    int historyStackIndex=0;
    int interations=0;
    long rand=0;
    int deadEnds=0;
    long randSame=0;
    long randPrev=0;
    long lastMove=0;
    long lastMoveSame=0;
    long forceNext=0;
    int solutionTreeLimit=0;
    int availableNeighbours=0;
    int historyStackIndexBest=0;
    int doRand=0;
    long jukeboxRandom=0;
    long objectRandom=0;
    int mazeComplete=0;
    int fakeIteration=0;
    int historyStackIndexDec=3; // this dictates how far back through the stack to go when reaching a dead end. The larger, the more tricky the maze
    mazeLenY=fieldMatrix.length;
    mazeLenX=fieldMatrix[0].length; 
    solutionTreeX[0]=2;
    solutionTreeY[0]=2;   
    System.out.println(mazeLenX);
    System.out.println(mazeLenY);
    createSpace();
    fieldMatrix[1][1]=2;
    while (isFieldComplete(mazeLenX,mazeLenY) && interations<4000) {
      //printMatrix(mazeLenX,mazeLenY);
      System.out.println("====================");
        rand = Math.round((Math.random() * 16) + 1);
        if (rand==1) {rand=1;}
        if (rand==2) {rand=1;}
        if (rand==3) {rand=1;}
        if (rand==4) {rand=1;}
        if (rand==5) {rand=1;}
        if (rand==6) {rand=2;}
        if (rand==7) {rand=2;}
        if (rand==8) {rand=2;}
        if (rand==9) {rand=2;}
        if (rand==10) {rand=2;}
        if (rand==11) {rand=3;}
        if (rand==12) {rand=3;}
        if (rand==13) {rand=3;}
        if (rand==14) {rand=4;}
        if (rand==15) {rand=4;}
        if (rand==16) {rand=4;}
        if (rand==17) {rand=4;}
        doRand=1;
        if ((matrixYPosition==1) && (rand==3) && mazeComplete==0) {rand=1;}
        if ((matrixXPosition==1) && (rand==4) && mazeComplete==0) {rand=2;}
        if ((matrixXPosition==(mazeLenX-2)) && (rand==4) && mazeComplete==0) {rand=2;}
        if ((matrixYPosition==(mazeLenY-2)) && (rand==3) && mazeComplete==0) {rand=1;}
        if (forceNext != 0) {
          rand=forceNext;
          forceNext=0;}
      System.out.println(rand + " rand");
      System.out.println(lastMoveSame + " lastMoveSame");
      System.out.println(lastMove + " lastMove");
      System.out.println(historyStackIndex + "  historyStackIndex");
      System.out.println(historyStackIndexBest + "  historyStackIndexBest");
      System.out.println(historyStackIndexDec + "  historyStackIndexDec");
      System.out.println(matrixXPosition + "  matrixXPosition");
      System.out.println(matrixYPosition + "  matrixYPosition");
      System.out.println(interations + "  interations");
      interations++;
      availableNeighbours=hasEmptyNeighbour(matrixYPosition,matrixXPosition,mazeLenX,mazeLenY);
      System.out.println(availableNeighbours + "  availableNeighbours");
      // Log the highest point on the stack
      if (historyStackIndex > historyStackIndexBest) {
        historyStackIndexBest = historyStackIndex;
      }
      if ((availableNeighbours != 0) && fakeIteration<10) {
        if ((rand==1) && ((availableNeighbours & 1)==1)){ //right
          if (matrixXPosition < (mazeLenX-1)){
            if ((fieldMatrix[matrixYPosition][matrixXPosition+2]==0) || (fieldMatrix[matrixYPosition][matrixXPosition+2]==2)) {
              System.out.println("  Going right");
              fieldMatrix[matrixYPosition][matrixXPosition+2]=2;
              fieldMatrix[matrixYPosition][matrixXPosition+1]=2;
              xHistory[historyStackIndex]=matrixXPosition;
              yHistory[historyStackIndex]=matrixYPosition;
              matrixXPosition=matrixXPosition+2;
              historyStackIndex++;
              if (mazeComplete==1){fakeIteration++;}
            }
          }
        }
        else if ((rand==3) && ((availableNeighbours & 2)==2)){ //left
          if (matrixXPosition > 2){
            if ((fieldMatrix[matrixYPosition][matrixXPosition-2]==0) || (fieldMatrix[matrixYPosition][matrixXPosition-2]==2)) {
              System.out.println("  Going left");
              fieldMatrix[matrixYPosition][matrixXPosition-2]=2;
              fieldMatrix[matrixYPosition][matrixXPosition-1]=2;
              xHistory[historyStackIndex]=matrixXPosition;
              yHistory[historyStackIndex]=matrixYPosition;
              matrixXPosition=matrixXPosition-2;
              historyStackIndex++;
              lastMove=rand;
              if (mazeComplete==1){fakeIteration++;}
            }
          }
        }
        else if ((rand==4) && ((availableNeighbours & 4)==4)){ //up
          if (matrixYPosition > 2){
            if ((fieldMatrix[matrixYPosition-2][matrixXPosition]==0) || (fieldMatrix[matrixYPosition-2][matrixXPosition]==2)) {
              System.out.println("  Going up");
              fieldMatrix[matrixYPosition-2][matrixXPosition]=2;
              fieldMatrix[matrixYPosition-1][matrixXPosition]=2;
              xHistory[historyStackIndex]=matrixXPosition;
              yHistory[historyStackIndex]=matrixYPosition;
              matrixYPosition=matrixYPosition-2;
              historyStackIndex++;
              lastMove=rand;
              if (mazeComplete==1){fakeIteration++;}
            }
          }
        }
        else if ((rand==2) && ((availableNeighbours & 8)==8)){ //down
          if (matrixXPosition < (mazeLenX-1)){
            if ((fieldMatrix[matrixYPosition+2][matrixXPosition]==0) || (fieldMatrix[matrixYPosition+2][matrixXPosition]==0)) {
              System.out.println("  Going down");
              fieldMatrix[matrixYPosition+2][matrixXPosition]=2;
              fieldMatrix[matrixYPosition+1][matrixXPosition]=2;
              xHistory[historyStackIndex]=matrixXPosition;
              yHistory[historyStackIndex]=matrixYPosition;
              matrixYPosition=matrixYPosition+2;
              historyStackIndex++;
              lastMove=rand;
              if (mazeComplete==1){fakeIteration++;}
            }
          }
        }
      }
      else { // else no available neighbours then move the stack pointer
        deadEnds++;
        fakeIteration=0;
        if (deadEnds < 40) {
          if (historyStackIndex>15){
            historyStackIndex = historyStackIndex*7/8;//historyStackIndexDec;
          }
          else if (historyStackIndex>2) {
            historyStackIndex = historyStackIndex/2;
          }
          else {
            historyStackIndex = historyStackIndex-1;
          }
        }
        else if (historyStackIndex>1) {
          historyStackIndex = historyStackIndex-1;
        }
        if (historyStackIndexDec > 1) { // reduce the step size to increase granularity to remove more walls
          historyStackIndexDec = historyStackIndexDec/2;
        }
        System.out.println(historyStackIndex + "  historyStackIndex New");
        System.out.println(deadEnds + "  deadEnds");
        if (deadEnds==1) { // if it's the first dead end the maze has failed (because deadEnds gets set to 1 on success). This code is hilarious
          System.out.println("  FAIL");
          matrixXPosition=1;
          matrixYPosition=1;
          historyStackIndex=0;
          interations=0;
          rand=0;
          deadEnds=0;
          solutionTreeLimit=0;
          availableNeighbours=1;
          createSpace();}
        else{
          matrixXPosition=xHistory[historyStackIndex-1];
          matrixYPosition=yHistory[historyStackIndex-1];
        }
        //break;

      }
      //for(int i=0;i<20;i++){
      //  System.out.println(xHistory[i]+"   "+yHistory[i]);
      //}
      
      if ((matrixXPosition==(mazeLenX-2)) && (matrixYPosition==(mazeLenY-2))) {
        //System.out.println("latching ");
        System.out.println("  SUCCESS");
        deadEnds=1;
        mazeComplete=1;
        for (int i=0;i<999;i++){
          solutionTreeX[i]=xHistory[i];
          solutionTreeY[i]=yHistory[i];
        }
        matrixXPosition=xHistory[historyStackIndex*7/8];
        matrixYPosition=yHistory[historyStackIndex*7/8];
        solutionTreeLimit=historyStackIndex;
        for(int i=0;i<solutionTreeLimit;i++){
          //System.out.println(solutionTreeX[i]+"   "+solutionTreeY[i]);
        }
        //break;
      }
          
    } // main while loop 
    
     
    for(int i=0;i<solutionTreeLimit;i++){
      //System.out.println(solutionTreeX[i]+"   "+solutionTreeY[i]);
    }
    for (int y=0;y<mazeLenY-1;y++){
      System.out.println(" ");
      for (int x=0;x<mazeLenX-1;x++) {
        for (int z=0;z<(solutionTreeLimit);z++) {
          if ((solutionTreeX[z]==x) && (solutionTreeY[z]==y)) {
            fieldMatrix[y][x]=4;
          }

            
        }
        if (fieldMatrix[y][x] != 1) {
          jukeboxRandom=(Math.round(Math.random() * mazeLenY * mazeLenX));
          if (jukeboxRandom < ((mazeLenX*mazeLenY)/90)) {
            fieldMatrix[y][x]=5;
          }
        }
        if ((fieldMatrix[y][x] != 1) && (fieldMatrix[y][x] != 5)) {
          objectRandom=(Math.round(Math.random() * 7));
          if (objectRandom==1){fieldMatrix[y][x]=6;}
          if (objectRandom==2){fieldMatrix[y][x]=7;}
          if (objectRandom==3){fieldMatrix[y][x]=8;}
        }
        System.out.print(fieldMatrix[y][x]);
      
      }
      
    }
    fieldMatrix[mazeLenY-2][mazeLenX-2]=3;
    
	}
	
	public boolean isFieldComplete(int mazeLenX, int mazeLenY){
  	boolean emptyBlockFound = false;
    for (int x=1;x<((mazeLenX-1));x++){
      for (int y=1;y<((mazeLenY-1));y++){
        if (fieldMatrix[x][y]==0) {
          emptyBlockFound=true;
        }    
      } 
    }
    return emptyBlockFound;
  	
	}
	
	public int hasEmptyNeighbour(int matrixYPosition, int matrixXPosition, int mazeLenX, int mazeLenY){
  	int availableNeighbours = 0;
    if (matrixXPosition < (mazeLenX-2)){
      if (fieldMatrix[matrixYPosition][matrixXPosition+2]==0) {
        availableNeighbours = availableNeighbours + 1;//right
      }
    }
    if (matrixXPosition > 2){
      if (fieldMatrix[matrixYPosition][matrixXPosition-2]==0) {
        availableNeighbours = availableNeighbours + 2;//left
      }
    }
    if (matrixYPosition > 2){
      if (fieldMatrix[matrixYPosition-2][matrixXPosition]==0) {
        availableNeighbours = availableNeighbours + 4;//up
      }
    }
    if (matrixYPosition < (mazeLenY-2)){
      if (fieldMatrix[matrixYPosition+2][matrixXPosition]==0) {
        availableNeighbours = availableNeighbours + 8;//down
      }
    }
      
    return availableNeighbours;
  	
	}
	
	public void createSpace(){
    for (int x=0;x<=(mazeSize-1);x++){
      System.out.println();
      for (int y=0;y<=(mazeSize-1);y++){
        if ((y&1)==0){
          fieldMatrix[y][x]=1;
        }
        else if ((x&1)==0) {
          fieldMatrix[y][x]=1;
        }
        else {
          fieldMatrix[y][x]=0;
        }
      System.out.print(fieldMatrix[y][x]);
      } 
    }
	}
	
	public void printMatrix(int mazeLenX, int mazeLenY) {
    for (int y=0;y<mazeLenY-1;y++){
      System.out.println(" ");
      for (int x=0;x<mazeLenX-1;x++) {
      System.out.print(fieldMatrix[y][x]);
      }
      
    }
	}
	

	@Override
	public void render () {
        world.step(Gdx.graphics.getDeltaTime(), 6, 2);
 
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        //font.draw(game.batch, "Welcome to Drop!!! ", 100, 150);
        //batch.draw(sprite2, sprite2.getX(), sprite2.getY());
        //System.out.println(sprite.getHeight());
    	int xAxisSize = Math.round(Gdx.graphics.getWidth()/sprite.getWidth());
    	int yAxisSize = Math.round(Gdx.graphics.getHeight()/sprite.getHeight());
        //System.out.println(yAxisSize);
        //System.out.println(xAxisSize);

        drawReady=drawField(fieldMatrix,xPos,yPos,xAxisSize,yAxisSize,helper);
        //System.out.println(Gdx.graphics.getFrameId()); 
        //System.out.println(xPos + "  : xPos");  
        if((Gdx.input.isKeyPressed(Keys.LEFT)) && drawReady) {
            if ((Gdx.graphics.getFrameId() - frameIdDelta) > updateInterval){
                frameIdDelta = Gdx.graphics.getFrameId();
                if ((xPos > 0) && (fieldMatrix[yPos][xPos-1] != 1)) {xPos=xPos-1;}
                //xPos=xPos-1;
            }
        }
        if((Gdx.input.isKeyPressed(Keys.RIGHT)) && drawReady) {
            if ((Gdx.graphics.getFrameId() - frameIdDelta) > updateInterval){
                frameIdDelta = Gdx.graphics.getFrameId();
                if ((xPos < fieldMatrix[0].length-1) && (fieldMatrix[yPos][xPos+1] != 1)) {xPos=xPos+1;}
                //xPos=xPos+1;
            }
        }
        if((Gdx.input.isKeyPressed(Keys.UP)) && drawReady) {
            if ((Gdx.graphics.getFrameId() - frameIdDelta) > updateInterval){
                frameIdDelta = Gdx.graphics.getFrameId();
                if ((yPos > 0) && (fieldMatrix[yPos-1][xPos] != 1)) {yPos=yPos-1;}
                //yPos=yPos-1;
            }
        }
        if((Gdx.input.isKeyPressed(Keys.DOWN)) && drawReady) {
            if ((Gdx.graphics.getFrameId() - frameIdDelta) > updateInterval){
                frameIdDelta = Gdx.graphics.getFrameId();
                if ((yPos < fieldMatrix.length-1) && (fieldMatrix[yPos+1][xPos] != 1)) {yPos=yPos+1;}
                //yPos=yPos+1;
            }
        }
        if((Gdx.input.isKeyPressed(Keys.SPACE)) && drawReady) {
            if ((Gdx.graphics.getFrameId() - frameIdDelta) > updateInterval){
                frameIdDelta = Gdx.graphics.getFrameId();
                helper=!helper;
                //yPos=yPos+1;
            }
        }
        if (drawReady) {
          if (fieldMatrix[yPos][xPos]==5) {
            System.out.println("jukebox");
            fieldMatrix[yPos][xPos]=2;
          }
          if (fieldMatrix[yPos][xPos]==3) {
            System.out.println("jukebox");
            fieldMatrix[yPos][xPos]=2;
          }
          if (fieldMatrix[yPos][xPos]==6) {
            System.out.println("jukebox");
            fieldMatrix[yPos][xPos]=2;
          }
          if (fieldMatrix[yPos][xPos]==7) {
            System.out.println("jukebox");
            fieldMatrix[yPos][xPos]=2;
          }
          if (fieldMatrix[yPos][xPos]==8) {
            System.out.println("jukebox");
            fieldMatrix[yPos][xPos]=2;
          }
        }
        batch.end();
	}
	
	public boolean drawField(int fieldMatrix[][], int xPos, int yPos, int xAxisSize, int yAxisSize, boolean helper) {
    	int xAxisMin=xPos-(xAxisSize/2);
    	int yAxisMin=yPos-(yAxisSize/2);
    	if (xPosPrev != xPos) {
        	subStep=(int) updateInterval;
        	if (xPosPrev > xPos) {direction=-1;}
        	else {direction=1;}
        	xPosPrev=xPos;
        }
        else if (yPosPrev != yPos) {
        	subStep=(int) updateInterval;
        	if (yPosPrev > yPos) {direction=-2;}
        	else {direction=2;}
        	yPosPrev=yPos;
        }
        //System.out.println(direction+ "   Direction");
    	//System.out.println(xAxisSize + "   xAxisSize");
    	//System.out.println(yAxisSize + "   yAxisSize");
    	//System.out.println(xPos + "   xPos");
    	//System.out.println(yPos + "   yPos");
    	//System.out.println(xAxisMin + "   xAxisMin");
    	//System.out.println(yAxisMin + "   yAxisMin");
    	//System.out.println("----------FRAME---------");
    	//System.out.println(fieldMatrix.length + "   fieldMatrix.length"); // this is Y
    	//System.out.println(fieldMatrix[0].length + "   fieldMatrix[0].length"); //this is X
		for(int x=-1;x<=xAxisSize;x++){
			for(int y=-1;y<=yAxisSize;y++){
    			int xIndex=xAxisMin+x;
    			int yIndex=yAxisMin+y;
    			//System.out.println(x + "   x");
    			//System.out.println(y + "   y");
    			//System.out.println(xIndex + "   xIndex");
    			//System.out.println(yIndex + "   yIndex");
    			if ((xIndex<0) || (xIndex >= fieldMatrix[0].length) || (yIndex<0) || (yIndex>=fieldMatrix.length)) {
        			//x,y
    			    //System.out.println("out of range");
    			    if (direction==1){
        			    batch.draw(sprite, ((x)*sprite.getWidth())+(subStep*(sprite.getWidth()/(updateInterval+1))), Gdx.graphics.getHeight()-((y+1)*sprite.getHeight()));
    			        }
    			    else if (direction==2){
        			    batch.draw(sprite, ((x)*sprite.getWidth()), Gdx.graphics.getHeight()-((y+1)*sprite.getHeight())-(subStep*sprite.getHeight()/(updateInterval+1)));
    			        }
    			    else if (direction==-2){
        			    batch.draw(sprite, ((x)*sprite.getWidth()), Gdx.graphics.getHeight()-((y+1)*sprite.getHeight())+(subStep*sprite.getHeight()/(updateInterval+1)));
    			        }
    			    else {
        			    batch.draw(sprite, ((x)*sprite.getWidth())-(subStep*(sprite.getWidth()/(updateInterval+1))), Gdx.graphics.getHeight()-((y+1)*sprite.getHeight()));
    			        }
    			    }
    			else if (fieldMatrix[yIndex][xIndex]==1){
    			    if (direction==1){
		                batch.draw(sprite, ((x)*sprite.getWidth())+(subStep*(sprite.getWidth()/(updateInterval+1))), Gdx.graphics.getHeight()-((y+1)*sprite.getHeight()));
	                }
    			    else if (direction==2){
        			    batch.draw(sprite, ((x)*sprite.getWidth()), Gdx.graphics.getHeight()-((y+1)*sprite.getHeight())-(subStep*sprite.getHeight()/(updateInterval+1)));
	                }
    			    else if (direction==-2){
		                batch.draw(sprite, ((x)*sprite.getWidth()), Gdx.graphics.getHeight()-((y+1)*sprite.getHeight())+(subStep*sprite.getHeight()/(updateInterval+1)));
	                }
	                else {
		                batch.draw(sprite, ((x)*sprite.getWidth())-(subStep*(sprite.getWidth()/(updateInterval+1))), Gdx.graphics.getHeight()-((y+1)*sprite.getHeight()));
                    }    	                
                }
    			else if (fieldMatrix[yIndex][xIndex]==3){
    			    if (direction==1){
		                batch.draw(sprite3, ((x)*sprite3.getWidth())+(subStep*(sprite3.getWidth()/(updateInterval+1))), Gdx.graphics.getHeight()-((y+1)*sprite3.getHeight()));
	                }
    			    else if (direction==2){
        			    batch.draw(sprite3, ((x)*sprite3.getWidth()), Gdx.graphics.getHeight()-((y+1)*sprite3.getHeight())-(subStep*sprite3.getHeight()/(updateInterval+1)));
	                }
    			    else if (direction==-2){
		                batch.draw(sprite3, ((x)*sprite3.getWidth()), Gdx.graphics.getHeight()-((y+1)*sprite3.getHeight())+(subStep*sprite3.getHeight()/(updateInterval+1)));
	                }
	                else {
		                batch.draw(sprite3, ((x)*sprite3.getWidth())-(subStep*(sprite3.getWidth()/(updateInterval+1))), Gdx.graphics.getHeight()-((y+1)*sprite3.getHeight()));
                    }    	                
                }
    			else if ((fieldMatrix[yIndex][xIndex]==4) && helper) {
    			    if (direction==1){
		                batch.draw(sprite2, ((x)*sprite3.getWidth())+(subStep*(sprite3.getWidth()/(updateInterval+1))), Gdx.graphics.getHeight()-((y+1)*sprite3.getHeight()));
	                }
    			    else if (direction==2){
        			    batch.draw(sprite2, ((x)*sprite3.getWidth()), Gdx.graphics.getHeight()-((y+1)*sprite3.getHeight())-(subStep*sprite3.getHeight()/(updateInterval+1)));
	                }
    			    else if (direction==-2){
		                batch.draw(sprite2, ((x)*sprite3.getWidth()), Gdx.graphics.getHeight()-((y+1)*sprite3.getHeight())+(subStep*sprite3.getHeight()/(updateInterval+1)));
	                }
	                else {
		                batch.draw(sprite2, ((x)*sprite3.getWidth())-(subStep*(sprite3.getWidth()/(updateInterval+1))), Gdx.graphics.getHeight()-((y+1)*sprite3.getHeight()));
                    }    	                
                }
    			else if ((fieldMatrix[yIndex][xIndex]==5)) {
    			    if (direction==1){
		                batch.draw(jukeboxSprite, ((x)*sprite3.getWidth())+(subStep*(sprite3.getWidth()/(updateInterval+1))), Gdx.graphics.getHeight()-((y+1)*sprite3.getHeight()));
	                }
    			    else if (direction==2){
        			    batch.draw(jukeboxSprite, ((x)*sprite3.getWidth()), Gdx.graphics.getHeight()-((y+1)*sprite3.getHeight())-(subStep*sprite3.getHeight()/(updateInterval+1)));
	                }
    			    else if (direction==-2){
		                batch.draw(jukeboxSprite, ((x)*sprite3.getWidth()), Gdx.graphics.getHeight()-((y+1)*sprite3.getHeight())+(subStep*sprite3.getHeight()/(updateInterval+1)));
	                }
	                else {
		                batch.draw(jukeboxSprite, ((x)*sprite3.getWidth())-(subStep*(sprite3.getWidth()/(updateInterval+1))), Gdx.graphics.getHeight()-((y+1)*sprite3.getHeight()));
                    }    	                
                }
    			else if ((fieldMatrix[yIndex][xIndex]==6)) {
    			    if (direction==1){
		                batch.draw(dadSprite, ((x)*sprite3.getWidth())+(subStep*(sprite3.getWidth()/(updateInterval+1))), Gdx.graphics.getHeight()-((y+1)*sprite3.getHeight()));
	                }
    			    else if (direction==2){
        			    batch.draw(dadSprite, ((x)*sprite3.getWidth()), Gdx.graphics.getHeight()-((y+1)*sprite3.getHeight())-(subStep*sprite3.getHeight()/(updateInterval+1)));
	                }
    			    else if (direction==-2){
		                batch.draw(dadSprite, ((x)*sprite3.getWidth()), Gdx.graphics.getHeight()-((y+1)*sprite3.getHeight())+(subStep*sprite3.getHeight()/(updateInterval+1)));
	                }
	                else {
		                batch.draw(dadSprite, ((x)*sprite3.getWidth())-(subStep*(sprite3.getWidth()/(updateInterval+1))), Gdx.graphics.getHeight()-((y+1)*sprite3.getHeight()));
                    }    	                
                }
    			else if ((fieldMatrix[yIndex][xIndex]==7)) {
    			    if (direction==1){
		                batch.draw(amitySprite, ((x)*sprite3.getWidth())+(subStep*(sprite3.getWidth()/(updateInterval+1))), Gdx.graphics.getHeight()-((y+1)*sprite3.getHeight()));
	                }
    			    else if (direction==2){
        			    batch.draw(amitySprite, ((x)*sprite3.getWidth()), Gdx.graphics.getHeight()-((y+1)*sprite3.getHeight())-(subStep*sprite3.getHeight()/(updateInterval+1)));
	                }
    			    else if (direction==-2){
		                batch.draw(amitySprite, ((x)*sprite3.getWidth()), Gdx.graphics.getHeight()-((y+1)*sprite3.getHeight())+(subStep*sprite3.getHeight()/(updateInterval+1)));
	                }
	                else {
		                batch.draw(amitySprite, ((x)*sprite3.getWidth())-(subStep*(sprite3.getWidth()/(updateInterval+1))), Gdx.graphics.getHeight()-((y+1)*sprite3.getHeight()));
                    }    	                
                }
    			else if ((fieldMatrix[yIndex][xIndex]==8)) {
    			    if (direction==1){
		                batch.draw(laikaSprite, ((x)*sprite3.getWidth())+(subStep*(sprite3.getWidth()/(updateInterval+1))), Gdx.graphics.getHeight()-((y+1)*sprite3.getHeight()));
	                }
    			    else if (direction==2){
        			    batch.draw(laikaSprite, ((x)*sprite3.getWidth()), Gdx.graphics.getHeight()-((y+1)*sprite3.getHeight())-(subStep*sprite3.getHeight()/(updateInterval+1)));
	                }
    			    else if (direction==-2){
		                batch.draw(laikaSprite, ((x)*sprite3.getWidth()), Gdx.graphics.getHeight()-((y+1)*sprite3.getHeight())+(subStep*sprite3.getHeight()/(updateInterval+1)));
	                }
	                else {
		                batch.draw(laikaSprite, ((x)*sprite3.getWidth())-(subStep*(sprite3.getWidth()/(updateInterval+1))), Gdx.graphics.getHeight()-((y+1)*sprite3.getHeight()));
                    }    	                
                }
			}
	    }
	    batch.draw(sprite2, (xAxisSize*sprite.getWidth()/2), (Gdx.graphics.getHeight()-((((yAxisSize)/2)+1)*sprite.getHeight())));

	    //System.out.println(subStep);
	    if (subStep==0) {
	        return true;
        }
	    else {
	        subStep=subStep-1;
    	    return false;
	    }
	             
    }
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
		img2.dispose();
	}
}
