package de.wizard.model;

import com.badlogic.gdx.utils.Pools;
import de.wizard.event.ActionEvent;

public abstract class Modifier {
        protected PhysicalActor actor;
        protected float lifeTime;
        protected float time;
        protected float deltaTime;

        public Modifier(PhysicalActor actor) {
                this(actor, 0);
        }

        public Modifier(PhysicalActor actor, float lifeTime) {
                this.actor = actor;
                this.lifeTime = lifeTime;
        }

        public void update(float delta) {
                deltaTime = delta;
                time += deltaTime;

                if (lifeTime == 0 || time >= lifeTime) {
                        modify(actor);
                } else {
                        /* Tell actor to remove modifier */
                        ActionEvent event = Pools.obtain(ActionEvent.class);
                        event.set(this, () -> actor.removeModifier(this));
                }
        }

        public abstract void modify(PhysicalActor actor);
}
