package de.wizard.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.Pools;
import de.wizard.Main;
import de.wizard.event.Event;
import de.wizard.event.VectorEvent;
import de.wizard.util.QuadTree;

public class Scene {
        public DelayedRemovalArray<Actor> actors;
        public QuadTree<Actor> tree;
        public PhysicalActor player;
        public Rectangle bounds;

        public final float GRAVITY = 9.81f;

        public float delta = 0f;

        public Scene() {
                actors = new DelayedRemovalArray<>();
                bounds = new Rectangle(0, 0, 100, 100);

                tree = new QuadTree<>(bounds);

                player = Pools.obtain(PhysicalActor.class);
                player.set(1, 1, 4, 50, 40);

                actors.add(player);
        }

        public void update() {
                handleInput();

                tree.clear();
                for (Actor actor : actors) {
                        tree.insert(actor);
                }

                delta = Gdx.graphics.getDeltaTime();

                actors.begin();
                for (Actor actor : actors) {
                        actor.update();
                }
                actors.end();
        }

        /*
         * Eventually return different friction coefficients depending on the surface.
         */
        public float getFriction(float x, float y, float v) {
                return v > 0.001f ? 0.8f : 1.0f;
        }

        public void handleInput() {
                float x = 0, y = 0;

                if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
                        x -= 1;
                if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
                        x += 1;
                if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
                        y -= 1;
                if (Gdx.input.isKeyPressed(Input.Keys.UP))
                        y += 1;

                VectorEvent e = Pools.obtain(VectorEvent.class);
                e.set(this, x, y);
                Main.eventManager.dispatchEvent(Event.MOVE_EVENT, e, player.getListenerId());
        }
}
