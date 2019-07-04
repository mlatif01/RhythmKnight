package com.latif.rhythmknight.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.latif.rhythmknight.Screens.PlayScreen;

public abstract class Enemy extends Sprite {
  protected World world;
  protected PlayScreen screen;

  public Body b2body;
  public Vector2 velocity;

  public Enemy(PlayScreen screen, float x, float y) {
    // find texture region for gobling
//    super(screen.getAtlas_2().findRegion("slime-idle-0"));
    this.world = screen.getWorld();
    this.screen = screen;
    setPosition(x, y);
    defineEnemy();
    velocity = new Vector2(-0.5f, 0f);
    b2body.setActive(false);
  }

  protected abstract void defineEnemy();

  public abstract void hitOnHead();

  public abstract void touchingRKnight();

  public void reverseVelocity(boolean x, boolean y) {
    if (x) {
      velocity.x = -velocity.x;
    }
    if (y) {
      velocity.y = -velocity.y;
    }
  }
}
