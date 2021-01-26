package sand.lsartor.voltorb.model;

public abstract class Cell {
    private final int x;
    private final int y;

    private CellStatus status;

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

    public void setStatus(final CellStatus status) {
        this.status = status;
    }
}
