package sand.lsartor.voltorb.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import sand.lsartor.voltorb.dto.GameDTO;
import sand.lsartor.voltorb.services.GameService;

@RestController
public class GameController {

    private final GameService gameService;

    public GameController(final GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/users/{username}/games/new")
    public GameDTO newGame(@PathVariable String username) {
        return gameService.getGame(username);
    }
}
