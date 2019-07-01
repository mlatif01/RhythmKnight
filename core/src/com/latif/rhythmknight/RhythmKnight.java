package com.latif.rhythmknight;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.latif.rhythmknight.Screens.PlayScreen;

public class RhythmKnight extends Game {

	// Virtual width and height for the game
	public static final int V_WIDTH = 400;
	public static final int V_HEIGHT = 208;

	//pixels per metre
	public static final float PPM = 100;

	//  The container which stores our images. Public so all screens can access
	public SpriteBatch batch;

	// Used to initialise game and load resources
	@Override
	public void create () {
		// initialise batch
		batch = new SpriteBatch();
		// pass in this so that the PlayScreen can set screens
		setScreen(new PlayScreen(this));
	}

	// used to update and render the game elements called 60 times per second
	@Override
	public void render () {
		// delegates the render method to the active screen
		super.render();
	}

	// used to free up resources used in the game
	@Override
	public void dispose () {
		// release all resources of the object
		batch.dispose();
	}
}
