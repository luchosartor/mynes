package sand.lsartor.voltorb.dto;

public class GameDTO {
    private final Status status;
    private final BoardDTO board;
    private final Config config;
    private final long elapsedTime;

    public GameDTO(final Status status, final BoardDTO board, final Config config, final long elapsedTime) {
        this.status = status;
        this.board = board;
        this.config = config;
        this.elapsedTime = elapsedTime;
    }

    public Status getStatus() {
        return status;
    }

    public BoardDTO getBoard() {
        return board;
    }

    public Config getConfig() {
        return config;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }
}
