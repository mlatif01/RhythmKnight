package com.latif.rhythmknight.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.latif.rhythmknight.RhythmKnight;

public class DesktopLauncher {
	public static void main (String[] arg) {
		// An open GL surface (fullscreen or in a lightweight window)
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		// initialise with the rhythm knight class and it's configuration
		// This is an application that libgdx uses as a back end for Desktop apps
		// This requires ApplicationListener which defines the lifecycle of a every libgdx app
		new LwjglApplication(new RhythmKnight(), config);
	}
}
