package com.latif.rhythmknight.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.latif.rhythmknight.RhythmKnight;
import com.latif.rhythmknight.Screens.PlayScreen;
import com.latif.rhythmknight.Sprites.Gobling;

public class B2WorldCreator {

  private Array<Gobling> goblings;
  PlayScreen screen;
  World world;
  TiledMap map;

  private Array<Rectangle> goblingRects;

  public B2WorldCreator(PlayScreen screen) {
    this.screen = screen;
    world = screen.getWorld();
    map = screen.getMap();
    // Adding bodies and fixtures to the game world (This will need to be put in separate classes)
    // A definition of what the body, fixtures consists of
    BodyDef bdef = new BodyDef();
    PolygonShape shape = new PolygonShape();
    FixtureDef fdef = new FixtureDef();
    Body body;

    // create ground bodies/fixtures - scale to PPM
    for (MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {
      Rectangle rect = ((RectangleMapObject) object).getRectangle();

      bdef.type = BodyDef.BodyType.StaticBody;
      bdef.position.set((rect.getX() + rect.getWidth() / 2) / RhythmKnight.PPM, (rect.getY() + rect.getHeight() / 2) / RhythmKnight.PPM);
      body = world.createBody(bdef);

      shape.setAsBox((rect.getWidth() / 2) / RhythmKnight.PPM, (rect.getHeight() / 2) / RhythmKnight.PPM);
      fdef.shape = shape;
      fdef.filter.categoryBits = RhythmKnight.GROUND_BIT;
      body.createFixture(fdef);
    }

    // create stone bodies/ fixtures
//        for (MapObject object : map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)) {
//          Rectangle rect = ((RectangleMapObject) object).getRectangle();
//
//          new Stone(screen, object);
//        }

    // create gobling bodies/fixtures
    goblings = new Array<Gobling>();
    goblingRects = new Array<Rectangle>();

    for (MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)) {
      Rectangle rect = ((RectangleMapObject) object).getRectangle();
//      goblings.add(new Gobling(screen, rect.getX() / RhythmKnight.PPM, rect.getY() / RhythmKnight.PPM));
      goblingRects.add(rect);
    }
  }

  public void spawnGobling() {
    goblings.add(new Gobling(screen, goblingRects.get(0).getX() / RhythmKnight.PPM, goblingRects.get(0).getY() / RhythmKnight.PPM));
  }

  public Array<Gobling> getGoblings() {
    return goblings;
  }

  public Array<Rectangle> getGoblingRects() {
    return goblingRects;
  }

}
