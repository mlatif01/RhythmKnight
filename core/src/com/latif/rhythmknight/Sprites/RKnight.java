package com.latif.rhythmknight.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.latif.rhythmknight.RhythmKnight;
import com.latif.rhythmknight.Screens.PlayScreen;

public class RKnight extends Sprite {
  // the world that RK lives in
  public World world;
  // Box2d Body
  public Body b2body;
  // get individual texture for RK idle
  private TextureRegion RKnightIdle;
  // padding for positioning of body sprite
  private float xPadding = 0.1f;

  public RKnight(World world, PlayScreen screen) {
    // get sprite actions for idle RKnight - initial frame
    super(screen.getAtlas().findRegion("adventurer-idle-2-00"));

    this.world = world;
    defineRKnight();
    // create RKnight idle texture
    RKnightIdle = new TextureRegion(getTexture(), 417, 40, 37, 37);
    // how large we should render the sprite on our screen - scaled with PPM
    setBounds(0, 0, 64 / RhythmKnight.PPM, 64 / RhythmKnight.PPM);
    // the actual texture region associated with the sprite
    setRegion(RKnightIdle);
  }

  // update state of RKnight sprite
  public void update(float deltaTime) {
    // offset position of sprite - using xPadding to center sprite within the fixture
    setPosition((b2body.getPosition().x  - getWidth() / 2) - xPadding, b2body.getPosition().y - getHeight() / 2);
  }

  public void defineRKnight() {
    // define body of sprite
    BodyDef bdef = new BodyDef();
    bdef.position.set(32 / RhythmKnight.PPM, 32 / RhythmKnight.PPM);
    bdef.type = BodyDef.BodyType.DynamicBody;
    b2body = world.createBody(bdef);

    // define fixture of sprite
    FixtureDef fdef = new FixtureDef();
    CircleShape shape = new CircleShape();
    shape.setRadius(24 / RhythmKnight.PPM);
    fdef.shape = shape;
    b2body.createFixture(fdef);

  }

}
