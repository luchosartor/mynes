package sand.lsartor.voltorb.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import sand.lsartor.voltorb.dto.BoardDTO;
import sand.lsartor.voltorb.dto.Click;
import sand.lsartor.voltorb.dto.Config;
import sand.lsartor.voltorb.dto.GameDTO;
import sand.lsartor.voltorb.dto.Result;
import sand.lsartor.voltorb.dto.Status;
import sand.lsartor.voltorb.factories.GameFactory;
import sand.lsartor.voltorb.model.Cell;
import sand.lsartor.voltorb.model.CellStatus;
import sand.lsartor.voltorb.model.Game;
import sand.lsartor.voltorb.model.ValueCell;
import sand.lsartor.voltorb.repositories.InMemRepository;
import sand.lsartor.voltorb.util.CellUtil;
import sand.lsartor.voltorb.util.Constants;
import sand.lsartor.voltorb.util.GameUtil;

@Service
public class GameService {

    private final InMemRepository gameRepository;

    public GameService(final InMemRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    /**
     * Find a game for a given user or create a new one with default config.
     *
     * @param username the username requesting the game
     * @return the game retrieved from repository if found, the new game in initial state otherwise.
     */
    public GameDTO getOrCreateGame(final String username) {
        return gameRepository.findById(username)
            .map(GameUtil::gameAsDTO)
            .orElseGet(() -> createGame(username, Constants.DEFAULT_CONFIG));
    }

    /**
     * Register a click in a given cell position and returns the cell affectation on the game's board.
     *
     * @param username the username clicking the cell.
     * @param x the click x position
     * @param y the click y position
     * @return the click result if any made, empty otherwise.
     */
    public Optional<Click> clickCell(final String username, final int x, final int y) {
        final Game game = gameRepository.findById(username).orElseThrow(() -> new IllegalStateException("Cannot click in null game"));
        if (game.getStatus() == Status.OVER) {
            return Optional.of(new Click(game.getBoard(), Result.GAME_OVER));
        } else if (game.getStatus() == Status.INITIAL) {
            GameFactory.initGame(x, y, game);
        }
        return clickCell(x, y, game, username);
    }

    private Optional<Click> clickCell(final int x, final int y, final Game game, final String username) {
        final List<Cell> cells = game.getBoard().getCells();
        final Cell cell = CellUtil.findCell(cells, x, y).orElseThrow(IllegalArgumentException::new);
        if (cell.getStatus() != CellStatus.CLEAR) {
            return Optional.empty();
        }
        if (cell.isMine()) {
            final List<Cell> mines = cells.stream().filter(Cell::isMine).collect(Collectors.toList());
            gameRepository.save(new Game(username, Status.OVER, game.getStart(), game.getConfig(), game.getBoard()));
            return Optional.of(new Click(new BoardDTO(mines, game.getBoard().getFlagsLeft()), Result.GAME_OVER));
        }
        final List<Cell> revealed = new ArrayList<>();
        revealCells(cell, cells, revealed);
        final long remainingCells = game.getBoard().getCells().stream().filter(c -> !c.isMine()).filter(c -> c.getStatus() != CellStatus.REVEALED).count();
        if (remainingCells == 0) {
            gameRepository.save(new Game(username, Status.VICTORY, game.getStart(), game.getConfig(), game.getBoard()));
            return Optional.of(new Click(new BoardDTO(revealed, game.getBoard().getFlagsLeft()), Result.VICTORY));
        }
        gameRepository.save(new Game(username, Status.RUNNING, game.getStart(), game.getConfig(), game.getBoard()));
        return Optional.of(new Click(new BoardDTO(revealed, game.getBoard().getFlagsLeft()), Result.SUCCESS));
    }

    private void revealCells(final Cell cell, final List<Cell> cells, final List<Cell> revealed) {
        cell.setStatus(CellStatus.REVEALED);
        final ValueCell valueCell = (ValueCell) cell;
        revealed.add(cell);
        if (valueCell.getValue() == 0) {
            cells.stream().filter(c -> Math.abs(c.getX() - cell.getX()) <= 1 && Math.abs(c.getY() - cell.getY()) <= 1)
                .filter(c -> c.getStatus() != CellStatus.REVEALED)
                .forEach(c -> revealCells(c, cells, revealed));
        }
    }

    /**
     * Create a new game for a given user, for the specified config. If previous game found for username, then replaces it with the new one.
     *
     * @param username the username attempting to create the game.
     * @param config the gameplay's config.
     * @return the created Game information.
     */
    public GameDTO createGame(final String username, final Config config) {
        final Config gameConfig = Optional.ofNullable(config).orElse(Constants.DEFAULT_CONFIG);
        final Game game = GameFactory.createEmptyGame(gameConfig, username);
        gameRepository.save(game);
        return GameUtil.gameAsDTO(game);
    }

    /**
     * Register a flag in a given cell position and returns the cell affectation on the game's board.
     *
     * @param username the username flagging the cell.
     * @param x the flag x position
     * @param y the flag y position
     * @return the flag result if any made, empty otherwise.
     */
    public Optional<Click> flagCell(final String username, final int x, final int y) {
        Game game = gameRepository.findById(username).orElseThrow(() -> new IllegalStateException("Cannot click in null game"));
        final BoardDTO board = game.getBoard();
        if (game.getStatus() == Status.OVER) {
            return Optional.of(new Click(board, Result.GAME_OVER));
        } else if (game.getStatus() == Status.INITIAL) {
            GameFactory.initGame(x, y, game);
        }
        final List<Cell> cells = board.getCells();
        final Cell cell = CellUtil.findCell(cells, x, y).orElseThrow(IllegalArgumentException::new);
        if (cell.getStatus() == CellStatus.REVEALED) {
            return Optional.empty();
        } else if (cell.getStatus() == CellStatus.FLAG) {
            cell.setStatus(CellStatus.CLEAR);
            board.setFlagsLeft(board.getFlagsLeft() + 1);
        } else {
            if (board.getFlagsLeft() == 0) {
                return Optional.empty();
            }
            cell.setStatus(CellStatus.FLAG);
            board.setFlagsLeft(board.getFlagsLeft() - 1);
        }
        gameRepository.save(new Game(username, Status.RUNNING, game.getStart(), game.getConfig(), board));
        return Optional.of(new Click(new BoardDTO(Collections.singletonList(cell), board.getFlagsLeft()), Result.SUCCESS));
    }
}
