package com.latif.rhythmknight.Tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.latif.rhythmknight.RhythmKnight;
import com.latif.rhythmknight.Sprites.Enemy;
import com.latif.rhythmknight.Sprites.InteractiveTileObject;
import com.latif.rhythmknight.Sprites.RKnight;

public class WorldContactListener implements ContactListener {

  // called when two fixtures begin to collide
  @Override
  public void beginContact(Contact contact) {

  }

  // when two fixtures disconnect from each other
  @Override
  public void endContact(Contact contact) {

  }

  // when something has collided you can change the characteristics of that collision
  @Override
  public void preSolve(Contact contact, Manifold oldManifold) {

  }

  // gives results of what happened due to that collision
  @Override
  public void postSolve(Contact contact, ContactImpulse impulse) {

    // define the fixtures which are colliding
    Fixture fixA = contact.getFixtureA();
    Fixture fixB = contact.getFixtureB();

    int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

    // check which fixture is sword and which is object
    // check if its colliding in range of sword and execute objects onSwordHit method
    if (fixA.getUserData() == "sword" || fixB.getUserData() == "sword") {
      // two new fixtures
      Fixture sword = fixA.getUserData() == "sword" ? fixA : fixB;
      Fixture object = sword == fixA ? fixB : fixA;

      // if user data is null, if not check if user data is an interactiveTileObject
      if (object.getUserData() != null && object.getUserData() instanceof InteractiveTileObject) {
        // if TileObject in range of sword and player is attacking call respective methods
        ((InteractiveTileObject) object.getUserData()).inSwordRange();
        ((InteractiveTileObject) object.getUserData()).checkSwordHit();

      }
    }

    // switch statement  to check for collision with gobling head
    switch (cDef) {
      case RhythmKnight
              .GOBLING_HEAD_BIT | RhythmKnight.SWORD_BIT:
        if (fixA.getFilterData().categoryBits == RhythmKnight.GOBLING_HEAD_BIT) {
          ((Enemy)fixA.getUserData()).hitOnHead();
        }
        else if (fixB.getFilterData().categoryBits == RhythmKnight.GOBLING_HEAD_BIT) {
          ((Enemy)fixB.getUserData()).hitOnHead();
        }
        break;
      case RhythmKnight
              .GOBLING_BIT | RhythmKnight.RKNIGHT_BIT:
        if (fixA.getFilterData().categoryBits == RhythmKnight.GOBLING_BIT) {
          ((Enemy)fixA.getUserData()).touchingRKnight();
        }
        else if (fixB.getFilterData().categoryBits == RhythmKnight.GOBLING_BIT) {
          ((Enemy)fixB.getUserData()).touchingRKnight();
        }
        break;
    }
  }
}
