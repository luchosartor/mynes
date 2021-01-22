package sand.lsartor.voltorb.model;

import java.time.OffsetDateTime;
import sand.lsartor.voltorb.dto.BoardDTO;
import sand.lsartor.voltorb.dto.Config;
import sand.lsartor.voltorb.dto.Status;

public class Game {
    private final Status status;
    private OffsetDateTime start;
    private final Config config;
    private final BoardDTO board;

    public Game(final Status status, final OffsetDateTime start, final Config config, final BoardDTO board) {
        this.status = status;
        this.start = start;
        this.config = config;
        this.board = board;
    }

    public Game(final Status status, final Config config, final BoardDTO board) {
        this.status = status;
        this.config = config;
        this.board = board;
    }

    public OffsetDateTime getStart() {
        return start;
    }

    public Config getConfig() {
        return config;
    }

    public BoardDTO getBoard() {
        return board;
    }

    public Status getStatus() {
        return status;
    }
}
