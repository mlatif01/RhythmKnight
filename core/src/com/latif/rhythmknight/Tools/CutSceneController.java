package com.latif.rhythmknight.Tools;

import com.badlogic.gdx.math.Vector2;
import com.latif.rhythmknight.Screens.PlayScreen;

public class CutSceneController {

  PlayScreen screen;

  // Cutscene variables
  // boolean representing if cutscene has been executed for stage 1
  private boolean cameraPositioned = false;
  private final float cameraStop = 5.0f;
  private final float cameraSpeed = 0.3f;

  public CutSceneController(PlayScreen screen) {
    this.screen = screen;
  }

  public void animateStartCutscene(float deltaTime) {
    // Move screen to correct position at start of game
    // **Improve the implementation of this**
    if (screen.getGameCam().position.x < cameraStop) {
      screen.getGameCam().position.x += cameraSpeed * deltaTime;
    }
    // stop moving camera when in correct position
    if (screen.getGameCam().position.x > cameraStop) {
      cameraPositioned = true;
    }
    // move Player to correct position
    if (screen.getGameCam().position.x - 1.5f > screen.getPlayer().b2body.getPosition().x && screen.getGameCam().position.x < cameraStop) {
      screen.getPlayer().b2body.applyLinearImpulse(new Vector2(0.5f, 0f), screen.getPlayer().b2body.getWorldCenter(), true);
    }
  }

  public boolean isCameraPositioned() {
    return cameraPositioned;
  }

  public void setCameraPositioned(boolean b) {
    cameraPositioned = b;
  }


  public void animateEndCutscene(float xspeed, float yspeed, float zspeed) {
    screen.getPlayer().b2body.applyLinearImpulse(new Vector2(0.05f, 0f), screen.getPlayer().b2body.getWorldCenter(), true);
    screen.getGameCam().zoom += zspeed;
    screen.getGameCam().position.y += yspeed;
    screen.getGameCam().position.x += xspeed;
  }


}


