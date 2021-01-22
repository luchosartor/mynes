package sand.lsartor.voltorb.dto;

import java.util.List;

public class BoardDTO {
    private final List<Cell> cells;
    private final int flagsLeft;

    public BoardDTO(final List<Cell> cells, final int flagsLeft) {
        this.cells = cells;
        this.flagsLeft = flagsLeft;
    }

    public List<Cell> getCells() {
        return cells;
    }

    public int getFlagsLeft() {
        return flagsLeft;
    }
}
