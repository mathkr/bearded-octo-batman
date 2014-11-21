package de.wizard.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.Pools;
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

                PhysicalActor a = Pools.obtain(PhysicalActor.class);
                a.set(bounds.width / 2, bounds.height / 2, 2, 0.2f, 5);

                a.addModifier(new Modifier(a) {
                        @Override
                        public void modify(PhysicalActor actor) {
                                actor.radius = 2 + Math.abs(MathUtils.sin(2 * time) * 10);
                        }
                });

                a.addModifier(new Modifier(a) {
                        @Override
                        public void modify(PhysicalActor actor) {
                                Vector2 vel = new Vector2(100, 100);
                                vel.rotateRad(10 * time);
                                actor.velocity.set(vel);
                        }
                });

                a.addModifier(new Modifier(a) {
                        @Override
                        public void modify(PhysicalActor actor) {
                                float sign = Math.signum(MathUtils.sin(time / 4));
                                actor.velocity.add(0, 10 * sign);
                        }
                });

                actors.add(a);
        }

        public void update() {
                /* Rebuild the quadtree */
                tree.clear();
                for (Actor actor : actors) {
                        tree.insert(actor);
                }

                /* Set delta to be used by actors to allow for easy manipulation */
                delta = Gdx.graphics.getDeltaTime();

                /* Update all actors */
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
}
