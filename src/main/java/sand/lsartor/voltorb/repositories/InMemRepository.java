package sand.lsartor.voltorb.repositories;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import sand.lsartor.voltorb.model.Game;

@Repository
public class InMemRepository {
    private final Map<String, Game> games = new HashMap<>();
    public Optional<Game> findById(final String username) {
        return Optional.ofNullable(games.get(username));
    }

    public Game save(final Game game) {
        games.put(game.getUsername(), game);
        return game;
    }
}
