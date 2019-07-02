package com.latif.rhythmknight.Sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.latif.rhythmknight.RhythmKnight;
import com.latif.rhythmknight.Screens.PlayScreen;

public class InteractiveTileObject {
  protected World world;
  protected TiledMap map;
  protected TiledMapTile tile;
  protected Rectangle bounds;
  protected Body body;

  public InteractiveTileObject(PlayScreen screen, Rectangle bounds) {
    this.world = screen.getWorld();
    this.map = screen.getMap();
    this.bounds = bounds;

    BodyDef bdef = new BodyDef();
    FixtureDef fdef = new FixtureDef();
    PolygonShape shape = new PolygonShape();

    // create body and fixtures - scale to PPM
    bdef.type = BodyDef.BodyType.StaticBody;
    bdef.position.set((bounds.getX() + bounds.getWidth() / 2) / RhythmKnight.PPM, (bounds.getY() + bounds.getHeight() / 2) / RhythmKnight.PPM);

    body = world.createBody(bdef);

    shape.setAsBox((bounds.getWidth() / 2) / RhythmKnight.PPM, (bounds.getHeight() / 2) / RhythmKnight.PPM);
    fdef.shape = shape;
    body.createFixture(fdef);
  }

}
