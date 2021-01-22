package sand.lsartor.voltorb.services;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import org.springframework.stereotype.Service;
import sand.lsartor.voltorb.dto.GameDTO;
import sand.lsartor.voltorb.factories.GameFactory;
import sand.lsartor.voltorb.model.Game;
import sand.lsartor.voltorb.repositories.GameRepository;

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
                final Game game = GameFactory.createGame();
                gameRepository.saveGame(username, game);
                return convertToDTO(game);
            });
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
