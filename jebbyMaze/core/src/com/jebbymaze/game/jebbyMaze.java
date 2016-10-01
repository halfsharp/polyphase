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

public class jebbyMaze extends ApplicationAdapter {
	SpriteBatch batch;
	Sprite sprite;
	Sprite sprite2;
	Sprite sprite3;
	World world;
	Texture img;
	Texture img2;
	Texture img3;
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
  int mazeDifficulty=40;
  int mazeSize=(mazeDifficulty*2)+1;
	int[][] fieldMatrix = new int[mazeSize][mazeSize];
    
	@Override
	public void create () {
		batch = new SpriteBatch();
        img3 = new Texture("archie.jpg");
        img2 = new Texture("jeb_cropped.jpg");
        img = new Texture("wall1.jpg");
        sprite = new Sprite(img);
        sprite2 = new Sprite(img2);
        sprite3 = new Sprite(img3);
 		
        world = new World(new Vector2(0, 0), true);
        createMaze();
        System.out.println(fieldMatrix);
 
	}
	
	public void createMaze(){
    int mazeLenX;
    int mazeLenY;
    int matrixXPosition=1;
    int matrixYPosition=1;
    int historyStackIndex=0;
    int [] xHistory = new int [1000];
    int [] yHistory = new int [1000];
    int interations=0;
    long rand;
    int availableNeighbours=0;
    mazeLenY=fieldMatrix.length;
    mazeLenX=fieldMatrix[0].length;    
    System.out.println(mazeLenX);
    System.out.println(mazeLenY);
    createSpace();
    fieldMatrix[1][1]=2;
    while (isFieldComplete(mazeLenX,mazeLenY) && interations<3000) {
      System.out.println("====================");
      rand = Math.round((Math.random() * 3) + 1);
      System.out.println(rand + " rand");
      System.out.println(historyStackIndex + "  historyStackIndex");
      System.out.println(matrixXPosition + "  matrixXPosition");
      System.out.println(matrixYPosition + "  matrixYPosition");
      interations++;
      availableNeighbours=hasEmptyNeighbour(matrixYPosition,matrixXPosition,mazeLenX,mazeLenY);
      System.out.println(availableNeighbours + "  availableNeighbours");
      if (availableNeighbours != 0) {
        if ((rand==1) && ((availableNeighbours & 1)==1)){ //right
          if (matrixXPosition < (mazeLenX-1)){
            if (fieldMatrix[matrixYPosition][matrixXPosition+2]==0) {
              System.out.println("  Going right");
              fieldMatrix[matrixYPosition][matrixXPosition+2]=2;
              fieldMatrix[matrixYPosition][matrixXPosition+1]=2;
              xHistory[historyStackIndex]=matrixXPosition;
              yHistory[historyStackIndex]=matrixYPosition;
              matrixXPosition=matrixXPosition+2;
              historyStackIndex++;
            }
            else {
              historyStackIndex=historyStackIndex-1;
              matrixXPosition=xHistory[historyStackIndex-2];
              matrixYPosition=yHistory[historyStackIndex-2];
            }
          }
        }
        else if ((rand==2) && ((availableNeighbours & 2)==2)){ //left
          if (matrixXPosition > 2){
            if (fieldMatrix[matrixYPosition][matrixXPosition-2]==0) {
              System.out.println("  Going left");
              fieldMatrix[matrixYPosition][matrixXPosition-2]=2;
              fieldMatrix[matrixYPosition][matrixXPosition-1]=2;
              xHistory[historyStackIndex]=matrixXPosition;
              yHistory[historyStackIndex]=matrixYPosition;
              matrixXPosition=matrixXPosition-2;
              historyStackIndex++;
            }
            else {
              historyStackIndex=historyStackIndex-1;
              matrixXPosition=xHistory[historyStackIndex-2];
              matrixYPosition=yHistory[historyStackIndex-2];
            }
          }
        }
        else if ((rand==3) && ((availableNeighbours & 4)==4)){ //up
          if (matrixYPosition > 2){
            if (fieldMatrix[matrixYPosition-2][matrixXPosition]==0) {
              System.out.println("  Going up");
              fieldMatrix[matrixYPosition-2][matrixXPosition]=2;
              fieldMatrix[matrixYPosition-1][matrixXPosition]=2;
              xHistory[historyStackIndex]=matrixXPosition;
              yHistory[historyStackIndex]=matrixYPosition;
              matrixYPosition=matrixYPosition-2;
              historyStackIndex++;
            }
            else {
              historyStackIndex=historyStackIndex-1;
              matrixXPosition=xHistory[historyStackIndex-2];
              matrixYPosition=yHistory[historyStackIndex-2];
            }
          }
        }
        else if ((rand==4) && ((availableNeighbours & 8)==8)){ //down
          if (matrixXPosition < (mazeLenX-1)){
            if (fieldMatrix[matrixYPosition+2][matrixXPosition]==0) {
              System.out.println("  Going down");
              fieldMatrix[matrixYPosition+2][matrixXPosition]=2;
              fieldMatrix[matrixYPosition+1][matrixXPosition]=2;
              xHistory[historyStackIndex]=matrixXPosition;
              yHistory[historyStackIndex]=matrixYPosition;
              matrixYPosition=matrixYPosition+2;
              historyStackIndex++;
            }
            else {
              historyStackIndex=historyStackIndex-1;
              matrixXPosition=xHistory[historyStackIndex-2];
              matrixYPosition=yHistory[historyStackIndex-2];
              }
          }
        }
      }
      else{
        matrixXPosition=xHistory[historyStackIndex-1];
        matrixYPosition=yHistory[historyStackIndex-1];
        historyStackIndex = historyStackIndex - 1;
      }

    }   
    for (int y=0;y<mazeLenY-1;y++){
      System.out.println(" ");
      for (int x=0;x<mazeLenX-1;x++) {
        System.out.print(fieldMatrix[y][x]);
      }
      
    }
    fieldMatrix[mazeLenY-2][mazeLenX-2]=3;
    
	}
	
	public boolean isFieldComplete(int mazeLenX, int mazeLenY){
  	boolean emptyBlockFound = false;
    for (int x=1;x<((mazeLenX-1)/2);x++){
      for (int y=1;y<((mazeLenY-1)/2);y++){
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
        availableNeighbours = availableNeighbours + 1;
      }
    }
    if (matrixXPosition > 2){
      if (fieldMatrix[matrixYPosition][matrixXPosition-2]==0) {
        availableNeighbours = availableNeighbours + 2;
      }
    }
    if (matrixYPosition > 2){
      if (fieldMatrix[matrixYPosition-2][matrixXPosition]==0) {
        availableNeighbours = availableNeighbours + 4;
      }
    }
    if (matrixYPosition < (mazeLenY-2)){
      if (fieldMatrix[matrixYPosition+2][matrixXPosition]==0) {
        availableNeighbours = availableNeighbours + 8;
      }
    }
      
    return availableNeighbours;
  	
	}
	
	public void createSpace(){
    for (int x=0;x<(mazeSize-1);x++){
      for (int y=0;y<(mazeSize-1);y++){
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

	@Override
	public void render () {
        world.step(Gdx.graphics.getDeltaTime(), 6, 2);
 
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        //batch.draw(sprite2, sprite2.getX(), sprite2.getY());
        //System.out.println(sprite.getHeight());
    	int xAxisSize = Math.round(Gdx.graphics.getWidth()/sprite.getWidth());
    	int yAxisSize = Math.round(Gdx.graphics.getHeight()/sprite.getHeight());
        //System.out.println(yAxisSize);
        //System.out.println(xAxisSize);

        drawReady=drawField(fieldMatrix,xPos,yPos,xAxisSize,yAxisSize);
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
        batch.end();
	}
	
	public boolean drawField(int fieldMatrix[][], int xPos, int yPos, int xAxisSize, int yAxisSize) {
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
