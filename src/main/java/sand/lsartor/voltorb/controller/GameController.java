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

    @PostMapping("/users/{username}/games/new")
    public GameDTO newGame(@PathVariable final String username, @RequestBody(required = false) final Config config) {
        final int rows = config.getRows() < MIN_ROWS ? DEFAULT_CONFIG.getRows() : config.getRows();
        final int cols = config.getColumns() < MIN_COLUMNS ? DEFAULT_CONFIG.getColumns() : config.getColumns();
        final int size = rows * cols;
        final int mines = config.getMines() < MIN_MINES || config.getMines() >= size ? Math.min(DEFAULT_CONFIG.getMines(), size - 1) : config.getMines();
        return gameService.createGame(username, new Config(rows, cols, mines));
    }

    @GetMapping("/users/{username}")
    public GameDTO getGame(@PathVariable final String username) {
        return gameService.getOrCreateGame(username);
    }

    @PostMapping("/users/{username}/games/click")
    public Click clickCell(@PathVariable final String username, @RequestBody final Position position) {
        return gameService.clickCell(username, position.x, position.y).orElseThrow(() -> new ResponseStatusException(HttpStatus.NO_CONTENT));
    }

    @PostMapping("/users/{username}/games/flag")
    public Click flagCell(@PathVariable final String username, @RequestBody final Position position) {
        return gameService.flagCell(username, position.x, position.y).orElseThrow(() -> new ResponseStatusException(HttpStatus.NO_CONTENT));
    }
}
