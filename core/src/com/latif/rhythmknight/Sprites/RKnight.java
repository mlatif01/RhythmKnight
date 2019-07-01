package com.latif.rhythmknight.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.latif.rhythmknight.RhythmKnight;

public class RKnight extends Sprite {

  // the world that RK lives in
  public World world;
  // Box2d Body
  public Body b2body;

  public RKnight(World world) {
    this. world = world;
    defineRKnight();
  }

  public void defineRKnight() {
    // define body of sprite
    BodyDef bdef = new BodyDef();
    bdef.position.set(32/ RhythmKnight.PPM, 32 / RhythmKnight.PPM);
    bdef.type = BodyDef.BodyType.DynamicBody;
    b2body = world.createBody(bdef);

    // define fixture of sprite
    FixtureDef fdef = new FixtureDef();
    CircleShape shape =  new CircleShape();
    shape.setRadius(5 / RhythmKnight.PPM);
    fdef.shape = shape;
    b2body.createFixture(fdef);
  }

}
