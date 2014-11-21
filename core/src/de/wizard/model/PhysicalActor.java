package de.wizard.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import de.wizard.Main;
import de.wizard.event.Event;
import de.wizard.event.VectorEvent;

public class PhysicalActor extends Actor {
        public Vector2 velocity;
        public Vector2 force;
        public Vector2 acceleration;

        public float mass;
        public float friction;

        private Array<Modifier> modifiers;

        public PhysicalActor() {
                modifiers = new Array<>();
                velocity = new Vector2();
                force = new Vector2();
                acceleration = new Vector2();
        }

        @Override
        public void update() {
                updateModifiers();
                move();
        }

        private void updateModifiers() {
                modifiers.forEach(m -> m.update(Main.scene.delta));
        }

        public void addModifier(Modifier modifier) {
                modifiers.add(modifier);
        }

        public void removeModifier(Modifier modifier) {
                modifiers.removeValue(modifier, true);
        }

        protected void move() {
                float delta = Main.scene.delta;

                /* Apply accumulated forces */
                acceleration.x = force.x / mass;
                acceleration.y = force.y / mass;

                /* Reset force vector */
                force.setZero();

                /* Apply acceleration */
                velocity.x += acceleration.x * 0.5f * delta;
                velocity.y += acceleration.y * 0.5f * delta;

                /* Apply friction */
                float vel_len = velocity.len();
                float mu = friction * Main.scene.getFriction(x, y, vel_len);
                float friction_len = mu * Main.scene.GRAVITY * delta;

                if (friction_len > vel_len) {
                        velocity.setZero();
                } else {
                        velocity.x -= velocity.x / vel_len * friction_len;
                        velocity.y -= velocity.y / vel_len * friction_len;
                }

                /* Calculate new position */
                x += velocity.x * delta;
                y += velocity.y * delta;

                /* Scene bounds corrections */
                x = x > Main.scene.bounds.width ? Main.scene.bounds.width : x;
                x = x < 0 ? 0 : x;

                y = y > Main.scene.bounds.height ? Main.scene.bounds.height : y;
                y = y < 0 ? 0 : y;
        }

        @Override
        public void fireEvent(int type, Event event) {
                switch(type) {
                        case Event.MOVE_EVENT:
                                VectorEvent e = (VectorEvent)event;
                                // DEBUG:
                                velocity.x += e.x * 20;
                                velocity.y += e.y * 20;
                                velocity.limit(80);
                                break;
                        default:
                                break;
                }
        }

        @Override
        public int getEventMask() {
                return Event.MOVE_EVENT;
        }

        @Override
        public void reset() {
                super.reset();

                modifiers.clear();

                velocity.setZero();
                force.setZero();
                acceleration.setZero();

                mass = 0;
                friction = 0;
        }

        public void set(float x, float y, float radius, float mass, float friction) {
                super.set(x, y, radius);

                this.mass = mass;
                this.friction = friction;
        }
}
