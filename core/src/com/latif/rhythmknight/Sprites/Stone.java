package com.latif.rhythmknight.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.latif.rhythmknight.RhythmKnight;
import com.latif.rhythmknight.Screens.PlayScreen;

public class Stone extends InteractiveTileObject {

  PlayScreen screen;

  public Stone(PlayScreen screen, MapObject object) {
    super(screen, object);
    // set user data to the object itself
    fixture.setUserData(this);
    // set category data to stone bit
    setCategoryFilter(RhythmKnight.STONE_BIT);
  }


  @Override
  public void inSwordRange() {
    Gdx.app.log("Stone", "In range");
  }

  @Override
  public void checkSwordHit() {
    if (screen.getPlayer().checkIsAttacking()) {
      Gdx.app.log("Stone", "Sword Hit");
      getCell().setTile(null);
      setCategoryFilter(RhythmKnight.DESTROYED_BIT);
      RhythmKnight.manager.get("audio/sounds/breakblock.wav", Sound.class).play();
    }
  }

}
