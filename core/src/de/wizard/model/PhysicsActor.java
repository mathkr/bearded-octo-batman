package de.wizard.model;

import com.badlogic.gdx.math.Vector2;
import de.wizard.Main;
import de.wizard.event.Event;
import de.wizard.event.VectorEvent;

public class PhysicsActor extends Actor {
        private Vector2 velocity;
        private Vector2 force;
        private Vector2 acceleration;

        private float mass;
        private float friction;

        public PhysicsActor() {
                velocity = new Vector2();
                force = new Vector2();
                acceleration = new Vector2();
        }

        @Override
        public void update() {
                move();
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
