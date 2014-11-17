package de.wizard.event;

public interface EventListener {
        public void deliver(int type, Event event);
        public int getListenerId();
        public int getEventMask();
}
