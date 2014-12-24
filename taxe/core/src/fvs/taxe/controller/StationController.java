package fvs.taxe.controller;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import fvs.taxe.StationClickListener;
import fvs.taxe.actor.StationActor;
import gameLogic.map.Connection;
import gameLogic.map.IPositionable;
import gameLogic.map.Station;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class StationController {
    private final int CONNECTION_LINE_WIDTH = 5;
    /*
    have to use CopyOnWriteArrayList because when we iterate through our listeners and execute
    their handler's method, one case unsubscribes from the event removing itself from this list
    and this list implementation supports removing elements whilst iterating through it
    */
    private List<StationClickListener> stationClickListeners = new CopyOnWriteArrayList<StationClickListener>();

    public void subscribeStationClick(StationClickListener listener) {
        stationClickListeners.add(listener);
    }

    public void unsubscribeStationClick(StationClickListener listener) {
        stationClickListeners.remove(listener);
    }

    private void stationClicked(Station station) {
        for (StationClickListener listener : stationClickListeners) {
            listener.clicked(station);
        }
    }

    private void renderStation(final Station station) {
        final StationActor stationActor = new StationActor(station.getLocation());

        stationActor.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                stationClicked(station);
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                tooltip.setPosition(stationActor.getX() + 20, stationActor.getY() + 20);
                tooltip.show(station.getName());
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                tooltip.hide();
            }
        });

        stage.addActor(stationActor);
    }

    public void renderStations() {
        for (Station station : map.getStations()) {
            renderStation(station);
        }
    }

    public void renderConnections(List<Connection> connections, Color color) {
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        game.shapeRenderer.setColor(color);
        //game.shapeRenderer.setProjectionMatrix(camera.combined);

        for (Connection connection : connections) {
            IPositionable start = connection.getStation1().getLocation();
            IPositionable end = connection.getStation2().getLocation();
            game.shapeRenderer.rectLine(start.getX(), start.getY(), end.getX(), end.getY(), CONNECTION_LINE_WIDTH);
        }
        game.shapeRenderer.end();
    }
}
