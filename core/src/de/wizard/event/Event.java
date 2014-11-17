package de.wizard.event;

public abstract class Event {
        public static final long ALL_EVENTS = Long.MAX_VALUE;

        public Object source;
        public Object object;
        public String message;

        public abstract long getType();
}
