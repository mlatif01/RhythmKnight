package com.latif.rhythmknight;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.latif.rhythmknight.Screens.PlayScreen;

public class RhythmKnight extends Game {
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
