package de.wizard;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.wizard.event.EventManager;
import de.wizard.model.Actor;
import de.wizard.model.Scene;

public class Main extends ApplicationAdapter {
        public static EventManager eventManager;
        public static Scene scene;

        private OrthographicCamera camera;
        private ShapeRenderer shapeRenderer;

        @Override
	public void create () {
                Gdx.app.setLogLevel(Application.LOG_DEBUG);

                eventManager = new EventManager();
                scene = new Scene();

                float w = Gdx.graphics.getWidth();
                float h = Gdx.graphics.getHeight();
                camera = new OrthographicCamera(scene.bounds.width, scene.bounds.height * (h / w));
                camera.position.set(scene.bounds.width / 2f, scene.bounds.height / 2f, 0);
                camera.update();

                shapeRenderer = new ShapeRenderer();
	}

	@Override
	public void render () {
                eventManager.processEvents();
                scene.update();

		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

                shapeRenderer.setProjectionMatrix(camera.combined);
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

                shapeRenderer.setColor(Color.GRAY);
                shapeRenderer.rect(0, 0, scene.bounds.width, scene.bounds.height);

                shapeRenderer.setColor(Color.YELLOW);
                for (Actor actor : scene.actors) {
                        shapeRenderer.circle(actor.x, actor.y, actor.radius, 16);
                }

                shapeRenderer.end();
	}

        @Override
        public void resize(int width, int height) {
                float widthAspect  = width > height ? (float) width / height : 1f;
                float heightAspect = height > width ? (float) height / width : 1f;
                camera.viewportWidth = scene.bounds.width * widthAspect;
                camera.viewportHeight = scene.bounds.height * heightAspect;
                camera.update();
        }

        @Override
        public void pause() {
        }

        @Override
        public void dispose() {
                shapeRenderer.dispose();
        }
}
