package sand.lsartor.voltorb.repositories;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import sand.lsartor.voltorb.model.Game;

@Repository
public class GameRepository {
    private final Map<String, Game> games = new HashMap<>();

    public Optional<Game> getGame(final String username) {
        return Optional.ofNullable(games.get(username));
    }

    public void saveGame(final String username, final Game game) {
        games.put(username, game);
    }
}
