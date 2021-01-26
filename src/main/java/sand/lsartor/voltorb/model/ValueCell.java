package sand.lsartor.voltorb.model;

public class ValueCell extends Cell {
    private final long value;

    public ValueCell(final int x, final int y, final CellStatus status, final long value) {
        super(x, y, status);
        this.value = value;
    }

    public long getValue() {
        return value;
    }
}
