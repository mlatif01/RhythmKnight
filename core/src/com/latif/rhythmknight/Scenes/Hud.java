package com.latif.rhythmknight.Scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.latif.rhythmknight.RhythmKnight;
import com.latif.rhythmknight.Sprites.RKnight;

public class Hud implements Disposable {

  boolean readyToUpdate = false;
  float timer = 0;


  int world;
  int stage;

  // A 2D scene graph containing hierarchies of actors. Stage handles the viewport and distributes
  // input events.
  public Stage hud_stage;

  // We use a new camera and viewport specifically for the HUD. So that it is locked and only
  // renders that part of the screen.
  private Viewport viewport;

  // player score
  private static Integer score;

  // player HP (This should go in another class!)
  private static Integer hp;

  // (Widgets from library Scene2d) A text label with optional word wrapping
  private Label rhythmKnightLabel;
  private static Label scoreLabel;
  private Label hpLabel;
  private Label rhythmKnightHpLabel;
  private Label stageLabel;
  private Label levelLabel;

  public Hud(SpriteBatch sb, int world, int stage) {
    score = 0;
    hp = RKnight.getHp();
    viewport = new FitViewport(RhythmKnight.V_WIDTH, RhythmKnight.V_HEIGHT, new OrthographicCamera());
    hud_stage = new Stage(viewport, sb);


    // In order to provide organisation, we create a table in our stage and then we can lay this out
    // to organise them in a certain position in the stage
    Table table = new Table();
    // put labels to the top of the stage
    table.top();
    // table is now the size of our stage
    table.setFillParent(true);

    rhythmKnightLabel = new Label("RK", new Label.LabelStyle(new BitmapFont(),
            Color.WHITE));
    scoreLabel = new Label(String.format("%06d", score), new Label.LabelStyle(new BitmapFont(),
            Color.WHITE));
    hpLabel = new Label("HP", new Label.LabelStyle(new BitmapFont(),
            Color.WHITE));
    rhythmKnightHpLabel = new Label(String.format("%02d", RKnight.getHp()), new Label.LabelStyle(new BitmapFont(),
            Color.WHITE));
    stageLabel = new Label("STAGE", new Label.LabelStyle(new BitmapFont(),
            Color.WHITE));
    // Change level depending on which stage is selected (NEEDS TO BE ADDED)
    levelLabel = new Label(world + "-" + stage, new Label.LabelStyle(new BitmapFont(),
            Color.WHITE));

    // add labels to the table
    table.add(rhythmKnightLabel).expandX().padTop(10);
    table.add(hpLabel).expandX().padTop(10);
    table.add(stageLabel).expandX().padTop(10);
    table.row();
    table.add(scoreLabel).expandX();
    table.add(rhythmKnightHpLabel).expandX();
    table.add(levelLabel).expandX();

    // adds the table to the stage
    hud_stage.addActor(table);
  }

  public static void updateScore(int value) {
    score += value;
  }

  public void update(float deltaTime) {
    timer += deltaTime;
    // if rk alive then set hp to current hp value
    if (RKnight.getHp() > 0) {
      rhythmKnightHpLabel.setText((String.format("%02d", RKnight.getHp())));
    }
    // TODO
    // if rk dead set hp to 0
    else if (RKnight.getHp() <= 0) {
      rhythmKnightHpLabel.setText((String.format("%02d", 0)));
    }
    if (timer > 0.001) {
      scoreLabel.setText(String.format("%06d", score));
      timer = 0;
    }
  }

  @Override
  public void dispose() {
    hud_stage.dispose();
  }

  public static int getScore() {
    return score;
  }
}
