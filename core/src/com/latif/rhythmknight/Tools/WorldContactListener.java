package com.latif.rhythmknight.Tools;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.latif.rhythmknight.RhythmKnight;
import com.latif.rhythmknight.Sprites.Enemy;
import com.latif.rhythmknight.Sprites.RKnight;

public class WorldContactListener implements ContactListener {

  private boolean swordOnObject = false;

  // Called when two fixtures begin to collide
  @Override
  public void beginContact(Contact contact) {
    Fixture fixA = contact.getFixtureA();
    Fixture fixB = contact.getFixtureB();

    Fixture sword = fixA.getUserData() == "sword" ? fixA : fixB;
    Fixture object = sword == fixA ? fixB : fixA;

    if (fixA.getUserData() != null && fixA.getUserData().equals("sword")) {
      swordOnObject = true;
      // Check for sword touching Gobling
      // if user data is null, if not check if user data is an Enemy
      if (object.getUserData() != null && object.getUserData() instanceof Enemy) {
        if (swordOnObject) {
          ((Enemy) object.getUserData()).hitOnHead();
        }
      }
    }
  }

  // when two fixtures disconnect from each other
  @Override
  public void endContact(Contact contact) {
    Fixture fixA = contact.getFixtureA();
    Fixture fixB = contact.getFixtureB();

    Fixture sword = fixA.getUserData() == "sword" ? fixA : fixB;
    Fixture object = sword == fixA ? fixB : fixA;

    if (fixA.getUserData() != null && fixA.getUserData().equals("sword") && object.getUserData() instanceof Enemy) {
      swordOnObject = false;
    }
    if (fixB.getUserData() != null && fixB.getUserData().equals("sword") && object.getUserData() instanceof Enemy) {
      swordOnObject = false;
    }
  }

  public boolean isSwordOnObject() {
    return swordOnObject;
  }

  // when something has collided you can change the characteristics of that collision
  @Override
  public void preSolve(Contact contact, Manifold oldManifold) {
    // define the fixtures which are colliding
    Fixture fixA = contact.getFixtureA();
    Fixture fixB = contact.getFixtureB();
//
    int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

    // switch statement  to check for collision with gobling head
    switch (cDef) {
      case RhythmKnight
              .GOBLING_BIT | RhythmKnight.RKNIGHT_BIT:
        // when gobling touches player, player will not move
        if (fixB.getFilterData().categoryBits == RhythmKnight.GOBLING_BIT) {
          fixB.getBody().setLinearVelocity(0f, 0f);
        }
      case RhythmKnight
              .GOBLING_HEAD_BIT | RhythmKnight.RKNIGHT_BIT:
        // when gobling touches player, player will not move
        if (fixB.getFilterData().categoryBits == RhythmKnight.GOBLING_HEAD_BIT) {
          fixB.getBody().setLinearVelocity(0f, 0f);
        }

        break;
    }
  }

  // Provides results of what happened due to a collision
  @Override
  public void postSolve(Contact contact, ContactImpulse impulse) {

    // define the fixtures which are colliding
    Fixture fixA = contact.getFixtureA();
    Fixture fixB = contact.getFixtureB();
//
    int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

    // create two new fixtures to check which fixture is sword and which is enemy.
    Fixture sword = fixA.getUserData() == "sword" ? fixA : fixB;
    Fixture object = sword == fixA ? fixB : fixA;

    // switch statement to check for collision with gobling
    switch (cDef) {
      case RhythmKnight
              .GOBLING_BIT | RhythmKnight.RKNIGHT_BIT:
        if (fixA.getFilterData().categoryBits == RhythmKnight.GOBLING_BIT) {
          ((Enemy) fixA.getUserData()).touchingRKnight();
          // if RK dead play death sound
          if (RKnight.getHp() <= 0) {
            RhythmKnight.manager.get("audio/sounds/rkdeath.wav", Sound.class).play();
          }
        } else if (fixB.getFilterData().categoryBits == RhythmKnight.GOBLING_BIT) {
          ((Enemy) fixB.getUserData()).touchingRKnight();
          // if RK dead play death sound
          if (RKnight.getHp() <= 0) {
            RhythmKnight.manager.get("audio/sounds/rkdeath.wav", Sound.class).play();
          }
        }
        break;
    }
  }

}