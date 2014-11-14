package de.wizard;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class Main extends ApplicationAdapter {
        private static int pauseCount = 0;
        private static int disposeCount = 0;

	@Override
	public void create () {
                Gdx.app.setLogLevel(Application.LOG_DEBUG);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}

        @Override
        public void pause() {
                Gdx.app.debug(this.toString(), "calling pause(): " + (++pauseCount));
        }

        @Override
        public void dispose() {
                Gdx.app.debug(this.toString(), "calling dispose(): " + (++disposeCount));
        }
}
