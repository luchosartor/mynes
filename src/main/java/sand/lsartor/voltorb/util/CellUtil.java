package sand.lsartor.voltorb.util;

import java.util.List;
import java.util.Optional;
import sand.lsartor.voltorb.model.Cell;

public enum CellUtil {
    ;
    public static Optional<Cell> findCell(final List<Cell> cells, final int xIndex, final int yIndex) {
        return cells.stream().filter(c -> c.getX() == xIndex && c.getY() == yIndex).findFirst();
    }
}
