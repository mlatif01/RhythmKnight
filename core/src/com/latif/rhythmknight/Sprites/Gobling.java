package com.latif.rhythmknight.Sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.latif.rhythmknight.RhythmKnight;
import com.latif.rhythmknight.Scenes.Hud;
import com.latif.rhythmknight.Screens.PlayScreen;

public class Gobling extends Enemy {

  private float stateTime;
  public float born = 0f;
  private Array<TextureRegion> frames;

  private boolean setToDestroy;
  private boolean destroyed;

  // get individual texture for gobling idle
  private TextureRegion goblingIdleTextureRegion;
  private TextureRegion goblingDieTextureRegion;

  // animations
  private Animation<TextureRegion> goblingIdle;
  private Animation<TextureRegion> goblingMove;
  private Animation<TextureRegion> goblingAttack;
  private Animation<TextureRegion> goblingHurt;
  private Animation<TextureRegion> goblingDie;

  // track how many goblings are dead
  public static int death = 0;


  public Gobling(PlayScreen screen, float x, float y) {
    super(screen, x, y);
    // set up animations
    defineEnemy();
    defineAnimations();
    stateTime = 0;

    // create RKnight idle texture region
//    goblingIdleTextureRegion = new TextureRegion(screen.getAtlas_2().getTextures().first(), 443, 1, 25, 25);
//    goblingDieTextureRegion = new TextureRegion(screen.getAtlas_2().getTextures().first(), 171, 1, 25, 25);

    setBounds(getX(), getY(), 40 / RhythmKnight.PPM, 40 / RhythmKnight.PPM);

    // the actual texture region associated with the sprite
//    setRegion(goblingIdleTextureRegion);

    setToDestroy = false;
    destroyed = false;
  }


  public void update(float deltaTime) {
    born += deltaTime;
    stateTime += deltaTime;
    if (setToDestroy && !destroyed) {
      world.destroyBody(b2body);
      death += 1;
      destroyed = true;
      setRegion(goblingDie.getKeyFrame(stateTime, true));
      stateTime = 0;
    } else if (!destroyed) {
      b2body.setLinearVelocity(velocity);
      setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
      setRegion(goblingMove.getKeyFrame(stateTime, true));
    }
  }

  protected void defineAnimations() {
    // set up move frames
    frames = new Array<TextureRegion>();
    frames.add(new TextureRegion(screen.getAtlas_2().getTextures().first(), 579, 1, 25, 25));
    frames.add(new TextureRegion(screen.getAtlas_2().getTextures().first(), 613, 1, 25, 25));
    frames.add(new TextureRegion(screen.getAtlas_2().getTextures().first(), 647, 1, 25, 25));
    frames.add(new TextureRegion(screen.getAtlas_2().getTextures().first(), 681, 1, 25, 25));
    goblingMove = new Animation<TextureRegion>(0.4f, frames);
    frames.clear();

    // set up die frames
    frames.add(new TextureRegion(screen.getAtlas_2().getTextures().first(), 171, 1, 25, 25));
    frames.add(new TextureRegion(screen.getAtlas_2().getTextures().first(), 205, 1, 25, 25));
    frames.add(new TextureRegion(screen.getAtlas_2().getTextures().first(), 239, 1, 25, 25));
    frames.add(new TextureRegion(screen.getAtlas_2().getTextures().first(), 273, 1, 25, 25));
    goblingDie = new Animation<TextureRegion>(0.4f, frames);
    frames.clear();

  }

  @Override
  protected void defineEnemy() {

    // define body of sprite
    BodyDef bdef = new BodyDef();
    bdef.position.set(getX(), getY());
    bdef.type = BodyDef.BodyType.DynamicBody;
    b2body = world.createBody(bdef);

    // define fixture of sprite
    FixtureDef fdef = new FixtureDef();
    CircleShape shape = new CircleShape();
    shape.setRadius(12 / RhythmKnight.PPM);
    fdef.filter.categoryBits = RhythmKnight.GOBLING_BIT;
    // set what gobling can collide with
    fdef.filter.maskBits = RhythmKnight.GROUND_BIT | RhythmKnight.STONE_BIT |
            RhythmKnight.OBJECT_BIT | RhythmKnight.RKNIGHT_BIT | RhythmKnight.SWORD_BIT;
    // create fixture of sprite
    fdef.shape = shape;
    b2body.createFixture(fdef).setUserData(this);

    // create hit range here
    PolygonShape head = new PolygonShape();
    Vector2[] vertice = new Vector2[4];
    vertice[0] = new Vector2(-10, 20).scl(1 / RhythmKnight.PPM);
    vertice[1] = new Vector2(10, 20).scl(1 / RhythmKnight.PPM);
    vertice[2] = new Vector2(-10, 3).scl(1 / RhythmKnight.PPM);
    vertice[3] = new Vector2(10, 3).scl(1 / RhythmKnight.PPM);
    head.set(vertice);

    fdef.filter.categoryBits = RhythmKnight.GOBLING_HEAD_BIT;
    fdef.filter.maskBits = RhythmKnight.RKNIGHT_BIT | RhythmKnight.SWORD_BIT;
    fdef.shape = head;
    b2body.createFixture(fdef).setUserData(this);

  }

  public void draw(Batch batch) {
    if (!destroyed || stateTime < 0.5)
      super.draw(batch);
  }

  @Override
  public void hitOnHead() {
    if (RKnight.isAttacking) {
      setToDestroy = true;
      Hud.updateScore(100);
      screen.incrementEnemiesKilled();
      System.out.println("DEATH: " + born);
      RhythmKnight.manager.get("audio/sounds/goblingdie.wav", Sound.class).play(0.5f);
    }
  }

  @Override
  public void touchingRKnight() {
    RKnight.reduceHp();
    setToDestroy = true;
    screen.incrementEnemiesKilled();
    RhythmKnight.manager.get("audio/sounds/goblinghit.wav", Sound.class).play(0.8f);
  }

}
