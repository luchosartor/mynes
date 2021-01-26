package sand.lsartor.voltorb.factories;

import sand.lsartor.voltorb.dto.BoardDTO;
import sand.lsartor.voltorb.dto.Config;
import sand.lsartor.voltorb.dto.Status;
import sand.lsartor.voltorb.model.Game;

import static sand.lsartor.voltorb.util.Constants.DEFAULT_CONFIG;

public enum GameFactory {
    ;
    public static Game createEmptyGame(final Config gameConfig) {
        return new Game(Status.INITIAL, gameConfig, BoardFactory.emptyBoard(gameConfig));
    }

    public static Game initGame(final int x, final int y, final Game game) {
        final BoardDTO board = BoardFactory.createBoard(game.getConfig(), x, y);
        return new Game(Status.RUNNING, game.getStart(), game.getConfig(), board);
    }
}
