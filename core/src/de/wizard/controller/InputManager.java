package de.wizard.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.utils.Pools;
import de.wizard.Main;
import de.wizard.event.Event;
import de.wizard.event.VectorEvent;

public class InputManager extends InputAdapter {
        public void update() {
                handleInputState();
        }

        private void handleInputState() {
                float x = 0, y = 0;

                if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
                        x -= 1;
                if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
                        x += 1;
                if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
                        y -= 1;
                if (Gdx.input.isKeyPressed(Input.Keys.UP))
                        y += 1;

                VectorEvent e = Pools.obtain(VectorEvent.class);
                e.set(this, x, y);
                Main.eventManager.dispatchEvent(Event.MOVE_EVENT, e, Main.scene.player.getListenerId());
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                return true;
        }
}
