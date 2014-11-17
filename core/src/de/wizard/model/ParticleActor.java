package de.wizard.model;

import de.wizard.event.Event;

public class ParticleActor extends Actor {
        public void set(float x, float y, float radius) {
                this.x = x;
                this.y = y;
                this.radius = radius;
        }

        @Override
        public void update() {

        }

        @Override
        public void deliver(long type, Event event) {

        }

        @Override
        public long getEventMask() {
                return 0;
        }
}
