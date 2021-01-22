package sand.lsartor.voltorb.model;

public class Cell {
    private final int x;
    private final int y;
    private final CellStatus status;
    private final CellType type;

    public Cell(final int x, final int y, final CellStatus status, final CellType type) {
        this.x = x;
        this.y = y;
        this.status = status;
        this.type = type;
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

    public CellType getType() {
        return type;
    }
}
