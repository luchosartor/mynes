package sand.lsartor.voltorb.dto;

import java.util.List;
import sand.lsartor.voltorb.model.Cell;

public class BoardDTO {
    private final List<Cell> cells;
    private int flagsLeft;

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

    public void setFlagsLeft(final int flagsLeft) {
        this.flagsLeft = flagsLeft;
    }
}
