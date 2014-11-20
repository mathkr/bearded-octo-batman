package de.wizard.event;

import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.Pool.Poolable;

public class EventManager {
        private static int nextId = 0;

        private IntMap<EventListener> eventListener;
        private DelayedRemovalArray<EventEntry> pendingEventsQueue;

        public EventManager() {
                eventListener = new IntMap<>();
                pendingEventsQueue = new DelayedRemovalArray<>();
        }

        public static int getListenerId() {
                return nextId++;
        }

        public void addListener(EventListener listener) {
                eventListener.put(listener.getListenerId(), listener);
        }

        public void removeListener(EventListener listener) {
                eventListener.remove(listener.getListenerId());
        }

        public void processEvents() {
                pendingEventsQueue.begin();
                for (EventEntry entry : pendingEventsQueue) {
                        if (entry.waitTime == 0 || TimeUtils.timeSinceMillis(entry.originTime) >= entry.waitTime) {
                                EventListener receiver = eventListener.get(entry.receiverId);

                                if (receiver != null) {
                                        boolean wantsEvent = (entry.eventType & receiver.getEventMask()) != 0;

                                        if (wantsEvent) {
                                                receiver.fireEvent(entry.eventType, entry.event);
                                        }
                                }

                                pendingEventsQueue.removeValue(entry, true);
                                Pools.free(entry.event);
                                Pools.free(entry);
                        }
                }
                pendingEventsQueue.end();
        }

        public void dispatchEvent(int type, Event event, int receiver) {
                EventEntry entry = Pools.obtain(EventEntry.class);

                entry.event = event;
                entry.receiverId = receiver;
                entry.eventType = type;

                pendingEventsQueue.add(entry);
        }

        public void dispatchTimedEvent(int eventType, Event event, int receiver, int waitTime) {
                EventEntry entry = Pools.obtain(EventEntry.class);

                entry.originTime = TimeUtils.millis();
                entry.event = event;
                entry.receiverId = receiver;
                entry.waitTime = waitTime;
                entry.eventType = eventType;

                pendingEventsQueue.add(entry);
        }

        private static class EventEntry implements Poolable {
                public Event event;
                public int receiverId;
                public int eventType;
                public int waitTime;
                public long originTime;

                @Override
                public void reset() {
                        event = null;
                        originTime = eventType = waitTime = receiverId = 0;
                }
        }
}
