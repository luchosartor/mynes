package sand.lsartor.voltorb.util;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import sand.lsartor.voltorb.dto.GameDTO;
import sand.lsartor.voltorb.model.Game;

public enum GameUtil {
    ;
    public static long getElapsedTime(final OffsetDateTime start) {
        if (start != null) {
            return Math.min(ChronoUnit.SECONDS.between(start, OffsetDateTime.now()), Constants.MAX_ELAPSED_TIME);
        }
        return 0;
    }

    public static GameDTO gameAsDTO(final Game game) {
        return new GameDTO(game.getStatus(), game.getBoard(), game.getConfig(), getElapsedTime(game.getStart()));
    }
}
