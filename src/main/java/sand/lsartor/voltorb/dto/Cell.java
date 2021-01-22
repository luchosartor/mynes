package sand.lsartor.voltorb.dto;

public class Cell {
    private final int x;
    private final int y;
    private final CellStatus status;

    public Cell(final int x, final int y, final CellStatus status) {
        this.x = x;
        this.y = y;
        this.status = status;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public CellStatus getStatus() {
        return status;
    }
}
