package sand.lsartor.voltorb.controllers;

import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sand.lsartor.voltorb.dto.Click;
import sand.lsartor.voltorb.dto.GameDTO;
import sand.lsartor.voltorb.services.GameService;
import sand.lsartor.voltorb.util.Position;

@RestController
public class GameController {

    private final GameService gameService;

    public GameController(final GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/users/{username}/games/new")
    public GameDTO newGame(@PathVariable final String username) {
        return gameService.getGame(username);
    }

    @PostMapping("/users/{username}/games/click")
    public Click clickCell(@PathVariable final String username, @RequestBody final Position position, final HttpServletResponse response) {
        return gameService.clickCell(username, position.x, position.y).orElseGet(() -> {
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            return new Click(null, null);
        });
    }
}
