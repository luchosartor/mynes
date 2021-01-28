package sand.lsartor.voltorb.controller;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import sand.lsartor.voltorb.dto.Click;
import sand.lsartor.voltorb.dto.Config;
import sand.lsartor.voltorb.dto.GameDTO;
import sand.lsartor.voltorb.services.GameService;
import sand.lsartor.voltorb.util.Position;

import static sand.lsartor.voltorb.util.Constants.DEFAULT_CONFIG;
import static sand.lsartor.voltorb.util.Constants.MIN_COLUMNS;
import static sand.lsartor.voltorb.util.Constants.MIN_MINES;
import static sand.lsartor.voltorb.util.Constants.MIN_ROWS;

@Validated
@RestController
public class GameController {

    private final GameService gameService;

    public GameController(final GameService gameService) {
        this.gameService = gameService;
    }

    /**
     * Create, save and return a new Game for a given User. If no config values are specified, then <code>DEFAULT_CONFIG</code> values are used.
     * If a current game instance is running, then replaces it with the new one.
     *
     * @param username the username that is playing
     * @param config the gameplay's config (optional).
     * @return the initial Game information
     */
    @PostMapping("/users/{username}/games/new")
    public GameDTO newGame(@PathVariable final String username, @RequestBody(required = false) final Config config) {
        final int rows = config.getRows() < MIN_ROWS ? DEFAULT_CONFIG.getRows() : config.getRows();
        final int cols = config.getColumns() < MIN_COLUMNS ? DEFAULT_CONFIG.getColumns() : config.getColumns();
        final int size = rows * cols;
        final int mines = config.getMines() < MIN_MINES || config.getMines() >= size ? Math.min(DEFAULT_CONFIG.getMines(), size - 1) : config.getMines();
        return gameService.createGame(username, new Config(rows, cols, mines));
    }

    /**
     * Get a player's current game. If no game found, then creates a new one with default config.
     *
     * @param username the username trying to get the game.
     * @return the Game
     */
    @GetMapping("/users/{username}")
    public GameDTO getGame(@PathVariable final String username) {
        return gameService.getOrCreateGame(username);
    }

    /**
     * Register a click and return resulting board.
     *
     * @param username the username that is clicking
     * @param position the click's position
     * @return the affected cells and click's result.
     */
    @PostMapping("/users/{username}/games/click")
    public Click clickCell(@PathVariable final String username, @RequestBody final Position position) {
        try {
            return gameService.clickCell(username, position.x, position.y).orElseThrow(() -> new ResponseStatusException(HttpStatus.NO_CONTENT));
        } catch (final IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No current game to click on.");
        } catch (final IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Clicked out of board!");
        }
    }

    /**
     * Register a flag and return resulting board.
     *
     * @param username the username that is flagging
     * @param position the flag's position
     * @return the affected cells and flag's result.
     */
    @PostMapping("/users/{username}/games/flag")
    public Click flagCell(@PathVariable final String username, @RequestBody final Position position) {
        try {
            return gameService.flagCell(username, position.x, position.y).orElseThrow(() -> new ResponseStatusException(HttpStatus.NO_CONTENT));
        } catch (final IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No current game to flag on.");
        } catch (final IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Flagged out of board!");
        }
    }
}
