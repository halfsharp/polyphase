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
	World world;
	Texture img;
	Texture img2;
	Body body;
	Body body2;
	long frameIdDelta = 0;
	int xPos = 0;
	int yPos = 0;
	long updateInterval = 10;
	int subStep;
	boolean drawReady;
	int xPosPrev;
	int yPosPrev;
    int direction=1;
	int[][] fieldMatrix = new int[][]{
		{0,0,1,0,1,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
		{0,0,0,0,0,0,0,0,0,1,0,0,1,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,1,0,0,1,0,0,0,0,0,0,0,0,0,1,1,1,1,0,0,0,0,0},
		{0,1,1,1,1,1,1,0,0,1,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0},
		{0,1,0,0,0,0,0,0,1,0,0,0,1,0,0,0,1,1,1,1,1,1,1,1,0,1,0,0,0,0,0},
		{1,1,0,1,1,1,1,0,1,1,0,0,1,0,0,0,1,0,0,0,0,0,0,0,0,1,0,0,0,0,0},
		{1,1,0,1,0,0,0,0,1,1,0,0,1,0,0,0,1,0,1,1,1,1,1,1,0,1,0,0,0,0,0},
		{1,1,0,1,0,0,0,0,1,1,0,0,1,0,0,0,1,0,1,0,1,0,0,0,0,1,0,0,0,0,0},
		{0,1,0,1,0,0,0,0,1,1,0,0,1,0,0,0,1,0,1,0,1,0,0,0,0,1,0,0,0,0,0},
		{0,1,0,1,0,0,0,0,1,1,0,0,1,0,0,0,1,0,1,0,1,0,0,0,0,1,0,0,0,0,0},
		{0,1,0,1,0,0,0,0,1,1,0,0,1,0,0,0,1,0,1,0,1,1,1,1,0,1,0,0,0,0,0},
		{0,1,0,1,1,1,0,1,1,1,0,0,1,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,1,1,0,0,1,1,1,1,1,1,1,0,1,1,1,1,0,1,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,1,0,0,1,0,0,0,0},
		{0,0,0,0,0,0,0,0,1,1,0,0,1,0,1,1,1,1,1,0,1,0,0,1,0,0,1,0,0,0,0},
		{0,0,0,0,0,0,0,0,1,1,0,0,1,0,1,0,0,0,0,0,1,0,0,1,0,0,1,0,0,0,0},
		{0,0,0,0,0,0,0,0,1,1,0,0,1,0,1,0,1,1,1,1,1,0,0,1,0,0,1,0,0,0,0},
		{0,0,0,0,0,0,0,0,1,1,0,0,1,0,1,0,1,0,0,0,0,0,0,0,0,0,1,0,0,0,0},
		{0,0,0,0,0,0,0,0,1,1,0,0,1,0,1,0,1,0,1,1,1,1,1,1,1,0,1,0,0,0,0},
		{0,0,0,0,0,0,0,0,1,1,0,0,1,0,1,0,1,0,0,0,0,0,0,0,1,0,1,0,0,0,0},
		{0,0,0,0,0,0,0,0,1,1,0,0,1,0,1,0,1,1,1,1,0,0,0,0,1,0,1,0,0,0,0},
		{0,0,0,0,0,0,0,0,1,1,0,0,1,0,0,0,0,0,0,0,0,0,0,0,1,0,1,0,0,0,0},
		{0,0,0,0,0,0,0,0,1,1,0,0,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,1,1,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}
    };
    
	@Override
	public void create () {
		batch = new SpriteBatch();
        img = new Texture("wall1.jpg");
        img2 = new Texture("jeb_cropped.jpg");
        sprite = new Sprite(img);
        sprite2 = new Sprite(img2);
 		
        world = new World(new Vector2(0, 0), true);
 
 
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
            }
        }
        if((Gdx.input.isKeyPressed(Keys.RIGHT)) && drawReady) {
            if ((Gdx.graphics.getFrameId() - frameIdDelta) > updateInterval){
                frameIdDelta = Gdx.graphics.getFrameId();
                if ((xPos < fieldMatrix[0].length-1) && (fieldMatrix[yPos][xPos+1] != 1)) {xPos=xPos+1;}
            }
        }
        if((Gdx.input.isKeyPressed(Keys.UP)) && drawReady) {
            if ((Gdx.graphics.getFrameId() - frameIdDelta) > updateInterval){
                frameIdDelta = Gdx.graphics.getFrameId();
                if ((yPos > 0) && (fieldMatrix[yPos-1][xPos] != 1)) {yPos=yPos-1;}
            }
        }
        if((Gdx.input.isKeyPressed(Keys.DOWN)) && drawReady) {
            if ((Gdx.graphics.getFrameId() - frameIdDelta) > updateInterval){
                frameIdDelta = Gdx.graphics.getFrameId();
                if ((yPos < fieldMatrix.length-1) && (fieldMatrix[yPos+1][xPos] != 1)) {yPos=yPos+1;}
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
