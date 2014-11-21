package de.wizard.event;

import de.wizard.util.Action;

public class ActionEvent extends Event {
        public Action action;

        @Override
        public void reset() {
                action = null;
        }

        public void set(Object source, Action action) {
                this.source = source;
                this.action = action;
        }
}
