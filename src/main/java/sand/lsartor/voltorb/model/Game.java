package sand.lsartor.voltorb.model;

import java.time.OffsetDateTime;
import sand.lsartor.voltorb.dto.BoardDTO;
import sand.lsartor.voltorb.dto.Config;
import sand.lsartor.voltorb.dto.Status;

public class Game {
    //    @Id annotation for Mongo implementation.
    private String username;
    private Status status;
    private OffsetDateTime start;
    private Config config;
    private BoardDTO board;

    public Game() {
    }

    public Game(final String username, final Status status, final OffsetDateTime start, final Config config, final BoardDTO board) {
        this.username = username;
        this.status = status;
        this.start = start;
        this.config = config;
        this.board = board;
    }

    public Game(final String username, final Status status, final Config config, final BoardDTO board) {
        this.username = username;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public void setStatus(final Status status) {
        this.status = status;
    }

    public void setStart(final OffsetDateTime start) {
        this.start = start;
    }

    public void setConfig(final Config config) {
        this.config = config;
    }

    public void setBoard(final BoardDTO board) {
        this.board = board;
    }
}
