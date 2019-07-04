package com.latif.rhythmknight.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.latif.rhythmknight.RhythmKnight;
import com.latif.rhythmknight.Screens.PlayScreen;


public class RKnight extends Sprite {

  public void message() {
    System.out.println("YES");
  }

  // ENUM for RKnight states
  public enum State {IDLE_SHEATHED, IDLE_UNSHEATHED, DRAWING_SWORD, RUNNING, JUMPING, FALLING, ATTACKING_1, ATTACKING_2};
  public enum AttackState {ATTACK1, ATTACK2}
  public State currentState;
  public State previousState;
  public AttackState nextAttackState;

  // state to check if RKnight is ready to battle
  public static boolean readyToBattle = false;


  // the world that RK lives in
  public World world;
  // Box2d Body
  public Body b2body;
  // get individual texture for RK idle
  private TextureRegion rKnightIdleTextureRegion;
  // padding for positioning of body sprite
  private float xPadding = 0.1f;
  // padding for attack animation
  private float xAniPadding = 0.5f;

  // animations
  private Animation<TextureRegion> rKnightIdleSheathed;
  private Animation<TextureRegion> rKnightDrawSword;
  private Animation<TextureRegion> rKnightIdleUnsheathed;
  private Animation<TextureRegion> rKnightRun;
  private Animation<TextureRegion> rKnightJump;
  private Animation<TextureRegion> rKnightAttack;
  private Animation<TextureRegion> rKnightAttack2;

  // state timer for animation frames
  private float stateTimer;
  // boolean representing if player is facing right
  private boolean runningRight;
  // boolean to check whether attack animation should be executed
  public static boolean isAttacking;
  // if attacking canMove should be false
  public static boolean canMove = true;
  // counter to help execute full slash animation
  private float attackCounter = 0.0f;
  // float used as offset for end of slash animation
  private float attackAniOffset = 1.5f;


  public RKnight(PlayScreen screen) {
    // get sprite actions for idle RKnight - initial frame
    super(screen.getAtlas().findRegion("adventurer-idle-00"));

    // initialise variables for animations
    currentState = State.IDLE_SHEATHED;
    previousState = State.IDLE_SHEATHED;
    nextAttackState = AttackState.ATTACK1;

    stateTimer = 0;
    runningRight = true;
    isAttacking = false;

    // method to set up array of animations
    defineAnimations();

    this.world = screen.getWorld();

    // defineRKnight attributes
    defineRKnight();

    // create RKnight idle texture region
    rKnightIdleTextureRegion = new TextureRegion(getTexture(), 419, 19, 37, 37);
    // how large we should render the sprite on our screen - scaled with PPM
    setBounds(0, 0, 64 / RhythmKnight.PPM, 64 / RhythmKnight.PPM);
    // the actual texture region associated with the sprite
    setRegion(rKnightIdleTextureRegion);

  }

  public void defineRKnight() {
    // define body of sprite
    BodyDef bdef = new BodyDef();
    bdef.position.set(128 / RhythmKnight.PPM, 128 / RhythmKnight.PPM);
    bdef.type = BodyDef.BodyType.DynamicBody;
    b2body = world.createBody(bdef);

    // define fixture of sprite
    FixtureDef fdef = new FixtureDef();
    PolygonShape shape = new PolygonShape();
    shape.setAsBox(6 / RhythmKnight.PPM,24 / RhythmKnight.PPM);
    fdef.filter.categoryBits = RhythmKnight.RKNIGHT_BIT;
    // set what RKnight can collide with
    fdef.filter.maskBits = RhythmKnight.GROUND_BIT | RhythmKnight.STONE_BIT | RhythmKnight.OBJECT_BIT |
            RhythmKnight.GOBLING_BIT | RhythmKnight.GOBLING_HEAD_BIT;
    // create fixture of sprite
    fdef.shape = shape;
    b2body.createFixture(fdef);

    // define and create fixture for sword
    EdgeShape sword = new EdgeShape();
    sword.set(new Vector2(5 / RhythmKnight.PPM, 1/ RhythmKnight.PPM), new Vector2(16 / RhythmKnight.PPM, 1/ RhythmKnight.PPM));
    fdef.filter.categoryBits = RhythmKnight.SWORD_BIT;
    // set what sword can collide with
    fdef.filter.maskBits = RhythmKnight.GROUND_BIT | RhythmKnight.STONE_BIT | RhythmKnight.OBJECT_BIT |
            RhythmKnight.GOBLING_BIT | RhythmKnight.GOBLING_HEAD_BIT;
    fdef.shape = sword;
    b2body.createFixture(fdef).setUserData("sword");
    fdef.isSensor = true;
  }

  // update state of RKnight sprite
  public void update(float deltaTime) {
    // if running left fix position
    if (!runningRight) {
      setPosition(b2body.getPosition().x - getWidth() / 2 + xPadding, b2body.getPosition().y - getHeight() / 2);
    }
    else {
      // offset position of sprite - using xPadding to center sprite within the fixture
      setPosition((b2body.getPosition().x  - getWidth() / 2) - xPadding, b2body.getPosition().y - getHeight() / 2);
    }
    // this method returns the appropriate frame which needs to be displayed as the sprites texture region
    setRegion(getFrame(deltaTime));
  }

  // handle slash input from user defined in PlayScreen class by setting is attacking to true
  public void handleSlash() {
    isAttacking = true;
    canMove = false;
    b2body.applyLinearImpulse(new Vector2(-0.1f, 0), b2body.getWorldCenter(), true);
    b2body.applyLinearImpulse(new Vector2(0.1f, 0), b2body.getWorldCenter(), true);
  }

  private TextureRegion getFrame(float deltaTime) {
    currentState = getState();
    TextureRegion region = null;

    switch(currentState) {
      case JUMPING:
        region = rKnightJump.getKeyFrame(stateTimer);
        break;
      case RUNNING:
        region = rKnightRun.getKeyFrame(stateTimer, true);
        break;
      case FALLING:
        region = rKnightJump.getKeyFrame(stateTimer);
        break;
      case IDLE_SHEATHED:
        region = rKnightIdleSheathed.getKeyFrame(stateTimer, true);
        break;
      case DRAWING_SWORD:
        region = rKnightIdleUnsheathed.getKeyFrame(stateTimer, true);
        break;
      // two different attack states
      case ATTACKING_1:
          region = rKnightAttack.getKeyFrame(stateTimer, true);
        break;
      case ATTACKING_2:
        region = rKnightAttack2.getKeyFrame(stateTimer, true);
        break;
      default:
        if (readyToBattle) {
          region = rKnightIdleUnsheathed.getKeyFrame(stateTimer, true);
        }
        else if (!readyToBattle) {
          region = rKnightIdleSheathed.getKeyFrame(stateTimer, true);
        }
        break;
    }

    // if not running right, flip x axis to look left
//    if ((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()) {
//      region.flip(true, false);
//      runningRight = false;
//    }

    // if running right, flip x axis to look right
    if ((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()) {
      region.flip(true, false);
      runningRight = true;
    }

    // if it doesn't equal previous state we must've reset to a new state so we reset the timer
    stateTimer = currentState == previousState ? stateTimer + deltaTime : 2;
    previousState = currentState;
    return region;

  }

  private State getState() {
    if (b2body.getLinearVelocity().y > 0 || (b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING)) {
      return State.JUMPING;
    }
    // logic for performing full set of attack frames
    else if (isAttacking) {
      attackCounter += 0.1;
      if (attackCounter > rKnightAttack.getAnimationDuration() + attackAniOffset) {
        attackCounter = 0.0f;
        isAttacking = false;
        canMove = true;
        // when animation is complete, change to next attack state
      }
      // flip attack state
      if (!isAttacking) {
        if (nextAttackState == AttackState.ATTACK1) {
          nextAttackState = AttackState.ATTACK2;
        }
        else {
          nextAttackState = AttackState.ATTACK1;
        }
      }
      if (nextAttackState == AttackState.ATTACK1) {
        return State.ATTACKING_1;
      }
      else {
        return State.ATTACKING_2;
      }
    }
    else if (b2body.getLinearVelocity().y < 0) {
      return State.FALLING;
    }
    else if (b2body.getLinearVelocity().x != 0) {
      return State.RUNNING;
    }
//    else if (Gdx.input.isKeyPressed(Input.Keys.Z)) {
//      return State.DRAWING_SWORD;
//    }
    else {
      return State.IDLE_UNSHEATHED;
    }
  }

  public void defineAnimations() {

    // FIX SPRITESHEET TO KEEP IT DRY
    // add animation frames
    Array<TextureRegion> frames = new Array<TextureRegion>();

    // set up idle sheathed animation frames
    frames.add(new TextureRegion(getTexture(), 417, 40, 37, 37));
    frames.add(new TextureRegion(getTexture(), 469, 79, 37, 37));
    frames.add(new TextureRegion(getTexture(), 469, 40, 37, 37));
    rKnightIdleSheathed = new Animation(0.5f, frames);
    frames.clear();

    // set up draw sword animation
    frames.add(new TextureRegion(getTexture(), 885, 79, 37, 37));
    frames.add(new TextureRegion(getTexture(), 885, 40, 37, 37));
    frames.add(new TextureRegion(getTexture(), 937, 79, 37, 37));
    frames.add(new TextureRegion(getTexture(), 937, 40, 37, 37));
    rKnightDrawSword = new Animation(0.1f, frames);
    frames.clear();

    // set up idle unsheathed animation frames
    frames.add(new TextureRegion(getTexture(), 521, 79, 37, 37));
    frames.add(new TextureRegion(getTexture(), 521, 40, 37, 37));
    frames.add(new TextureRegion(getTexture(), 573, 79, 37, 37));
    frames.add(new TextureRegion(getTexture(), 573, 40, 37, 37));
    rKnightIdleUnsheathed = new Animation(0.1f, frames);
    frames.clear();

    // set up running animation frames
    frames.add(new TextureRegion(getTexture(), 729, 79, 37, 37));
    frames.add(new TextureRegion(getTexture(), 729, 40, 37, 37));
    frames.add(new TextureRegion(getTexture(), 781, 79, 37, 37));
    frames.add(new TextureRegion(getTexture(), 781, 40, 37, 37));
    frames.add(new TextureRegion(getTexture(), 833, 79, 37, 37));
    frames.add(new TextureRegion(getTexture(), 833, 40, 37, 37));
    rKnightRun = new Animation(0.1f, frames);
    frames.clear();

    // set up jump animation frames
    frames.add(new TextureRegion(getTexture(), 625, 79, 37, 37));
    frames.add(new TextureRegion(getTexture(), 625, 40, 37, 37));
    frames.add(new TextureRegion(getTexture(), 677, 79, 37, 37));
    frames.add(new TextureRegion(getTexture(), 677, 40, 37, 37));
    rKnightJump = new Animation(0.1f, frames);
    frames.clear();

    // set up attack1 animation frames
    int xpad = 10;
    frames.add(new TextureRegion(getTexture(), 1+xpad, 61, 37, 37));
    frames.add(new TextureRegion(getTexture(), 1+xpad, 22, 37, 37));
    frames.add(new TextureRegion(getTexture(), 53+xpad, 61, 37, 37));
    frames.add(new TextureRegion(getTexture(), 53+xpad, 22, 37, 37));
    frames.add(new TextureRegion(getTexture(), 105+xpad, 61, 37, 37));
    rKnightAttack = new Animation(0.1f, frames);
    frames.clear();

    // set up attack2 animation frames
    // padding for slash animation
    frames.add(new TextureRegion(getTexture(), 105+xpad, 22, 37, 37));
    frames.add(new TextureRegion(getTexture(), 157+xpad, 61, 37, 37));
    frames.add(new TextureRegion(getTexture(), 157+xpad, 22, 37, 37));
    frames.add(new TextureRegion(getTexture(), 209+xpad, 61, 37, 37));
    frames.add(new TextureRegion(getTexture(), 209+xpad, 22, 37, 37));
    frames.add(new TextureRegion(getTexture(), 261+xpad, 79, 37, 37));
    rKnightAttack2 = new Animation(0.1f, frames);
    frames.clear();
  }

}
