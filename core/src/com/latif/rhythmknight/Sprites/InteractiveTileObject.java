package com.latif.rhythmknight.Sprites;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.latif.rhythmknight.RhythmKnight;
import com.latif.rhythmknight.Screens.PlayScreen;

import java.awt.RenderingHints;

public abstract class InteractiveTileObject {
  protected World world;
  protected TiledMap map;
  protected TiledMapTile tile;
  protected Rectangle bounds;
  protected Body body;

  protected Fixture fixture;

  public InteractiveTileObject(PlayScreen screen, MapObject object) {
    this.world = screen.getWorld();
    this.map = screen.getMap();
    this.bounds = ((RectangleMapObject) object).getRectangle();

    BodyDef bdef = new BodyDef();
    FixtureDef fdef = new FixtureDef();
    PolygonShape shape = new PolygonShape();

    // create body and fixtures - scale to PPM
    bdef.type = BodyDef.BodyType.StaticBody;
    bdef.position.set((bounds.getX() + bounds.getWidth() / 2) / RhythmKnight.PPM, (bounds.getY() + bounds.getHeight() / 2) / RhythmKnight.PPM);

    body = world.createBody(bdef);

    shape.setAsBox((bounds.getWidth() / 2) / RhythmKnight.PPM, (bounds.getHeight() / 2) / RhythmKnight.PPM);
    fdef.shape = shape;
    // capture fixture which surrounds tileObject
    fixture = body.createFixture(fdef);
  }

  public abstract void inSwordRange();
  public abstract void checkSwordHit();

  public TiledMapTileLayer.Cell getCell() {
    // box2d body is scaled down its 1/16th so we have to scale it back up (* by PPM) to get to 16
    TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(8);
    return layer.getCell((int)(body.getPosition().x * RhythmKnight.PPM / 16),
            (int) (body.getPosition().y * RhythmKnight.PPM / 16));
  }

  public void setCategoryFilter(short filterBit) {
    Filter filter = new Filter();
    filter.categoryBits = filterBit;
    fixture.setFilterData(filter);
  }


}
