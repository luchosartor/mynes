package sand.lsartor.voltorb.services;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import sand.lsartor.voltorb.dto.BoardDTO;
import sand.lsartor.voltorb.dto.Click;
import sand.lsartor.voltorb.dto.Config;
import sand.lsartor.voltorb.dto.GameDTO;
import sand.lsartor.voltorb.dto.Result;
import sand.lsartor.voltorb.dto.Status;
import sand.lsartor.voltorb.factories.GameFactory;
import sand.lsartor.voltorb.model.Cell;
import sand.lsartor.voltorb.model.Game;
import sand.lsartor.voltorb.model.Mine;
import sand.lsartor.voltorb.model.ValueCell;
import sand.lsartor.voltorb.repositories.InMemRepository;
import sand.lsartor.voltorb.util.CellUtil;
import sand.lsartor.voltorb.util.Constants;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static sand.lsartor.voltorb.model.CellStatus.CLEAR;

@SpringBootTest
class GameServiceTest {

    public static final String USER = "user";
    private GameService service;
    private InMemRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemRepository();
        service = new GameService(repository);
    }

    @Test
    void whenGetOrCreateGameAndGameAlreadyExistsThenReturnsCurrentGame() {
        final Game emptyGame = GameFactory.createEmptyGame(new Config(20, 20, 20), USER);
        repository.save(emptyGame);

        final GameDTO game = service.getOrCreateGame(USER);
        assertEquals(emptyGame.getConfig().getMines(), game.getConfig().getMines());
    }

    @Test
    void whenGetOrCreateGameAndNoPreviousGameThenCreatesAndSaveGameWithDefaultConfig() {
        final GameDTO game = service.getOrCreateGame(USER);
        assertEquals(Constants.DEFAULT_CONFIG.getMines(), game.getConfig().getMines());
        assertTrue(repository.findById(USER).isPresent());
    }

    @Test
    void whenClickCellButGameDoesNotExistThenThrowsException() {
        try {
            service.clickCell(USER, 0, 0);
        } catch (final IllegalStateException e) {
            assertEquals("Cannot click in null game", e.getMessage());
        }
    }

    @Test
    void whenClickCellAndGameIsAlreadyOverThenReturnsGameBoardAndResultOver() {
        final Game game = GameFactory.createEmptyGame(new Config(20, 20, 20), USER);
        game.setStatus(Status.OVER);
        repository.save(game);

        final Optional<Click> click = service.clickCell(USER, 0, 0);
        assertTrue(click.isPresent());
        assertEquals(click.get().getResult(), Result.GAME_OVER);
    }

    @Test
    void whenFirstClickCellThenSetStatusRunningAndStartTimeTracking() {
        repository.save(GameFactory.createEmptyGame(new Config(20, 20, 20), USER));

        service.clickCell(USER, 0, 0);

        final Optional<Game> game = repository.findById(USER);
        assertTrue(game.isPresent());
        assertEquals(game.get().getStatus(), Status.RUNNING);
        assertNotNull(game.get().getStart());
    }

    @Test
    void whenClickMineThenGameOver() {
        repository.save(getMiniGame());

        final Optional<Click> click = service.clickCell(USER, 0, 0);
        assertTrue(click.isPresent());
        assertEquals(click.get().getResult(), Result.GAME_OVER);
    }

    @Test
    void whenClickZeroValueThenRevealsAdjacentCells() {
        repository.save(getMiniGame());

        final Optional<Click> click = service.clickCell(USER, 2, 0);
        assertTrue(click.isPresent());
        final List<Cell> revealedCells = click.get().getBoard().getCells();
        assertEquals(revealedCells.size(), 2);
        assertTrue(CellUtil.findCell(revealedCells, 1, 0).isPresent());
        assertTrue(CellUtil.findCell(revealedCells, 2, 0).isPresent());
    }

    @Test
    void whenAllNonMinesCellClickedThenReturnsVictory() {
        repository.save(getMiniGame());

        final Optional<Click> click = service.clickCell(USER, 2, 0);
        assertTrue(click.isPresent());
        assertEquals(click.get().getResult(), Result.VICTORY);
    }

    @Test
    void whenFlagCellAlreadyFlaggedThenRemoveFlagAndIncreaseFlagsLeft() {
        repository.save(getMiniGame());
        service.flagCell(USER, 0, 0);

        final Optional<Click> click = service.flagCell(USER, 0, 0);
        assertTrue(click.isPresent());
        assertEquals(click.get().getResult(), Result.SUCCESS);
        final List<Cell> unflagged = click.get().getBoard().getCells();
        assertEquals(unflagged.size(), 1);
        assertTrue(CellUtil.findCell(unflagged, 0, 0).isPresent());
        assertEquals(click.get().getBoard().getFlagsLeft(), 1);
    }

    @Test
    void whenFlagCellThenAddFlagAndDecreaseFlagsLeft() {
        repository.save(getMiniGame());

        final Optional<Click> click = service.flagCell(USER, 0, 0);
        assertTrue(click.isPresent());
        assertEquals(click.get().getResult(), Result.SUCCESS);
        final List<Cell> flagged = click.get().getBoard().getCells();
        assertEquals(flagged.size(), 1);
        assertTrue(CellUtil.findCell(flagged, 0, 0).isPresent());
        assertEquals(click.get().getBoard().getFlagsLeft(), 0);
    }

    @Test
    void whenNoMoreFlagsLeftThenFlaggingCellReturnsEmpty() {
        repository.save(getMiniGame());
        service.flagCell(USER, 0, 0);

        final Optional<Click> click = service.flagCell(USER,1, 0);
        assertFalse(click.isPresent());
    }

    private Game getMiniGame() {
        return new Game(USER, Status.RUNNING, OffsetDateTime.now(), new Config(1, 3, 1), new BoardDTO(
            Arrays.asList(new Mine(0, 0, CLEAR), new ValueCell(1, 0, CLEAR, 1), new ValueCell(2, 0, CLEAR, 0)), 1
        ));
    }
}