package de.wizard.model;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.utils.Pool.Poolable;
import de.wizard.event.EventListener;
import de.wizard.event.EventManager;

public abstract class Actor extends Circle implements EventListener, Poolable {
        private int listenerId = EventManager.getId();

        public abstract void update();

        @Override
        public int getListenerId() {
                return listenerId;
        }

        @Override
        public void reset() {
                x = y = radius = 0;
        }
}
