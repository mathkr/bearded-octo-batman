package de.wizard.event;

import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.Pool.Poolable;

public class EventManager {
        public static final int ALWAYS_RECURRING = -1;

        private static int nextId = 0;

        private Array<EventListener> eventListener;
        private DelayedRemovalArray<EventEntry> pendingEventsQueue;

        public EventManager() {
                eventListener = new Array<>();
                pendingEventsQueue = new DelayedRemovalArray<>();
        }

        public static int getId() {
                return nextId++;
        }

        public void addListener(EventListener listener) {
                eventListener.add(listener);
        }

        public void removeListener(EventListener listener) {
                eventListener.removeValue(listener, true);
        }

        /*
         * UGLY!
         */
        public void processEvents() {
                pendingEventsQueue.begin();
                for (int i = 0; i < pendingEventsQueue.size; ++i) {
                        EventEntry eventEntry = pendingEventsQueue.get(i);
                        int eventType = eventEntry.type;
                        boolean process =
                                !eventEntry.timed || TimeUtils.timeSinceMillis(eventEntry.originTime) >= eventEntry.waitTime;

                        if (process) {
                                eventEntry.dispatchCounter = eventEntry.dispatchCounter == ALWAYS_RECURRING
                                        ? ALWAYS_RECURRING
                                        : eventEntry.dispatchCounter - 1;

                                for (EventListener listener : eventListener) {
                                        boolean isReceiver =
                                                eventEntry.global || listener.getListenerId() == eventEntry.receiverId;
                                        boolean wantsEvent =
                                                (eventType & listener.getEventMask()) != 0;

                                        if (isReceiver && wantsEvent) {
                                                listener.deliver(eventType, eventEntry.event);
                                        }
                                }

                                if (eventEntry.dispatchCounter == 0) {
                                        pendingEventsQueue.removeIndex(i);
                                        Pools.free(eventEntry.event);
                                        Pools.free(eventEntry);
                                } else if (eventEntry.timed) {
                                        eventEntry.originTime = TimeUtils.millis();
                                }
                        }
                }
                pendingEventsQueue.end();
        }

        public void dispatchEvent(int type, Event event, int receiver) {
                EventEntry entry = Pools.obtain(EventEntry.class);

                entry.event = event;
                entry.receiverId = receiver;
                entry.dispatchCounter = 1;
                entry.type = type;

                pendingEventsQueue.add(entry);
        }

        public void dispatchTimedEvent(int type, Event event, int receiver, int waitTime) {
                EventEntry entry = Pools.obtain(EventEntry.class);

                entry.originTime = TimeUtils.millis();
                entry.event = event;
                entry.receiverId = receiver;
                entry.timed = true;
                entry.waitTime = waitTime;
                entry.dispatchCounter = 1;
                entry.type = type;

                pendingEventsQueue.add(entry);
        }

        public void dispatchTimedRecurringEvent(int type, Event event, int receiver, int waitTime, int dispatchCounter) {
                EventEntry entry = Pools.obtain(EventEntry.class);

                entry.originTime = TimeUtils.millis();
                entry.event = event;
                entry.receiverId = receiver;
                entry.timed = true;
                entry.waitTime = waitTime;
                entry.dispatchCounter = dispatchCounter;
                entry.type = type;

                pendingEventsQueue.add(entry);
        }

        public void dispatchGlobalEvent(int type, Event event) {
                EventEntry entry = Pools.obtain(EventEntry.class);

                entry.event = event;
                entry.global = true;
                entry.dispatchCounter = 1;
                entry.type = type;

                pendingEventsQueue.add(entry);
        }

        private static class EventEntry implements Poolable {
                public Event event;
                public boolean global;
                public int receiverId;
                public int type;

                public boolean timed;
                public int waitTime;
                public int dispatchCounter;
                public long originTime;

                @Override
                public void reset() {
                        event = null;
                        global = timed = false;
                        originTime = type = waitTime = receiverId = dispatchCounter = 0;
                }
        }
}
