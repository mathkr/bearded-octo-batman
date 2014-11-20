package de.wizard.event;

public interface EventListener {
        public void fireEvent(int type, Event event);
        public int getListenerId();
        public int getEventMask();
}
