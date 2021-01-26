package sand.lsartor.voltorb.services;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import sand.lsartor.voltorb.dto.BoardDTO;
import sand.lsartor.voltorb.dto.Click;
import sand.lsartor.voltorb.dto.GameDTO;
import sand.lsartor.voltorb.dto.Result;
import sand.lsartor.voltorb.dto.Status;
import sand.lsartor.voltorb.factories.GameFactory;
import sand.lsartor.voltorb.model.Cell;
import sand.lsartor.voltorb.model.CellStatus;
import sand.lsartor.voltorb.model.Game;
import sand.lsartor.voltorb.model.Mine;
import sand.lsartor.voltorb.model.ValueCell;
import sand.lsartor.voltorb.repositories.GameRepository;
import sand.lsartor.voltorb.util.CellUtil;

@Service
public class GameService {

    private final GameRepository gameRepository;

    public GameService(final GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public GameDTO getGame(final String username) {
        return gameRepository.getGame(username)
            .map(this::convertToDTO)
            .orElseGet(() -> {
                final Game game = GameFactory.createEmptyGame();
                gameRepository.saveGame(username, game);
                return convertToDTO(game);
            });
    }

    public Optional<Click> clickCell(final String username, final int x, final int y) {
        Game game = gameRepository.getGame(username).orElseThrow(() -> new IllegalStateException("Cannot click in null game"));
        if (game.getStatus() == Status.OVER) {
            return Optional.of(new Click(game.getBoard(), Result.GAME_OVER));
        } else if (game.getStatus() == Status.INITIAL) {
            game = GameFactory.initGame(x, y, game);
        }
        return clickCell(x, y, game, username);
    }

    private Optional<Click> clickCell(final int x, final int y, final Game game, final String username) {
        final List<Cell> cells = game.getBoard().getCells();
        final Cell cell = CellUtil.findCell(cells, x, y).orElseThrow(IllegalStateException::new);
        if (cell.getStatus() != CellStatus.CLEAR) {
            return Optional.empty();
        }
        if (cell instanceof Mine) {
            final List<Cell> mines = cells.stream().filter(c -> c instanceof Mine).collect(Collectors.toList());
            gameRepository.saveGame(username, new Game(Status.OVER, game.getStart(), game.getConfig(), game.getBoard()));
            return Optional.of(new Click(new BoardDTO(mines, game.getBoard().getFlagsLeft()), Result.GAME_OVER));
        }
        final List<Cell> revealed = new ArrayList<>();
        revealCells(cell, cells, revealed);
        gameRepository.saveGame(username, new Game(Status.RUNNING, game.getStart(), game.getConfig(), game.getBoard()));
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

    private long getElapsedTime(final OffsetDateTime start) {
        if (start != null) {
            return ChronoUnit.SECONDS.between(start, OffsetDateTime.now());
        }
        return 0;
    }

    private GameDTO convertToDTO(final Game game) {
        return new GameDTO(game.getStatus(), game.getBoard(), game.getConfig(), getElapsedTime(game.getStart()));
    }
}
