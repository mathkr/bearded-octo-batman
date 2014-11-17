package de.wizard.model;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import de.wizard.util.QuadTree;

public class Scene {
        public DelayedRemovalArray<Actor> actors;
        public QuadTree<Actor> tree;

        public Rectangle bounds;

        public Scene() {
                actors = new DelayedRemovalArray<>();
                bounds = new Rectangle(0, 0, 300, 200);

                tree = new QuadTree<>(bounds);
        }

        public void update() {
                actors.begin();
                for (Actor actor : actors) {
                        actor.update();
                }
                actors.end();
        }
}
