package de.wizard;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import de.wizard.model.Scene;

public class Main extends ApplicationAdapter {
        public static Scene scene;

        @Override
	public void create () {
                Gdx.app.setLogLevel(Application.LOG_DEBUG);

                scene = new Scene();
	}

	@Override
	public void render () {
                scene.update();

		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}

        @Override
        public void pause() {
        }

        @Override
        public void dispose() {
        }
}
