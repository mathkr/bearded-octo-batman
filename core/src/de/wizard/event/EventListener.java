package de.wizard.event;

public interface EventListener {
        public void deliver(long type, Event event);
        public int getListenerId();
        public long getEventMask();
}
