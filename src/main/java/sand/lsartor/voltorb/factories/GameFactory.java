package sand.lsartor.voltorb.factories;

import sand.lsartor.voltorb.dto.Config;
import sand.lsartor.voltorb.dto.Status;
import sand.lsartor.voltorb.model.Game;

public enum GameFactory {
    ;

    private static final Config DEFAULT_CONFIG = new Config(9, 9, 10);

    public static Game createGame() {
        return new Game(Status.INITIAL, DEFAULT_CONFIG, BoardFactory.emptyBoard(DEFAULT_CONFIG));
    }
}
