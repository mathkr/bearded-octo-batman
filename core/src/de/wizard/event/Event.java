package de.wizard.event;

import com.badlogic.gdx.utils.Pool.Poolable;

public abstract class Event implements Poolable {
        public static final int NO_EVENTS   = 0;
        public static final int MOVE_EVENT = 1 << 0;
        public static final int ALL_EVENTS  = ~0;

        public Object source;
}
