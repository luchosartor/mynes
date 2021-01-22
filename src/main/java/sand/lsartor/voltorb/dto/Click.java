package sand.lsartor.voltorb.dto;

public class Click {
    private final BoardDTO board;
    private final Result result;

    public Click(final BoardDTO board, final Result result) {
        this.board = board;
        this.result = result;
    }

    public BoardDTO getBoard() {
        return board;
    }

    public Result getResult() {
        return result;
    }
}
