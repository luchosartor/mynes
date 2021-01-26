package sand.lsartor.voltorb.factories;

import sand.lsartor.voltorb.dto.BoardDTO;
import sand.lsartor.voltorb.dto.Config;
import sand.lsartor.voltorb.dto.Status;
import sand.lsartor.voltorb.model.Game;

public enum GameFactory {
    ;

    private static final Config DEFAULT_CONFIG = new Config(3, 3, 1);

    public static Game createEmptyGame() {
        return new Game(Status.INITIAL, DEFAULT_CONFIG, BoardFactory.emptyBoard(DEFAULT_CONFIG));
    }

    public static Game initGame(final int x, final int y, final Game game) {
        final BoardDTO board = BoardFactory.createBoard(game.getConfig(), x, y);
        return new Game(Status.RUNNING, game.getStart(), game.getConfig(), board);
    }
}
