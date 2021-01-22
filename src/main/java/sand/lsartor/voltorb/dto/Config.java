package sand.lsartor.voltorb.dto;

public class Config {
    private final int rows;
    private final int columns;
    private final int mines;

    public Config(final int rows, final int columns, final int mines) {
        this.rows = rows;
        this.columns = columns;
        this.mines = mines;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public int getMines() {
        return mines;
    }
}
