package sand.lsartor.voltorb.factories;

import sand.lsartor.voltorb.dto.BoardDTO;
import sand.lsartor.voltorb.dto.Config;
import sand.lsartor.voltorb.dto.Status;
import sand.lsartor.voltorb.model.Game;

public enum GameFactory {
    ;
    public static Game createEmptyGame(final Config gameConfig, final String username) {
        return new Game(username, Status.INITIAL, gameConfig, BoardFactory.emptyBoard(gameConfig));
    }

    public static Game initGame(final int x, final int y, final Game game) {
        game.setStatus(Status.RUNNING);
        final BoardDTO board = BoardFactory.createBoard(game.getConfig(), x, y);
        game.setBoard(board);
        return game;
    }
}
