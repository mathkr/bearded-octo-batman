package de.wizard.event;

public class VectorEvent extends Event {
        public float x, y;

        @Override
        public void reset() {
                y = x = 0;
        }

        public void set(Object source, float x, float y) {
                this.x = x;
                this.y = y;
                this.source = source;
        }
}
