package sand.lsartor.voltorb.factories;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.IntStream;
import sand.lsartor.voltorb.dto.BoardDTO;
import sand.lsartor.voltorb.dto.Config;
import sand.lsartor.voltorb.model.Cell;
import sand.lsartor.voltorb.model.CellStatus;
import sand.lsartor.voltorb.model.Mine;
import sand.lsartor.voltorb.model.ValueCell;

import static sand.lsartor.voltorb.util.CellUtil.findCell;

public class BoardFactory {

    private static final Random RANDOM = new Random();
    public static BoardDTO createBoard(final Config config, final int firstX, final int firstY) {
        final int capacity = config.getColumns() * config.getRows();
        final List<Cell> cells = new ArrayList<>(capacity);
        RANDOM.ints(config.getMines() + 1, 0, capacity).forEach(mine -> {
            final int y = (mine / config.getColumns());
            final int x = mine - (config.getColumns() * y);
            if (x == firstX && y == firstY || cells.size() == config.getMines()) {
                return;
            }
            cells.add(new Mine(x, y, CellStatus.CLEAR));
        });
        IntStream.range(0, config.getRows()).forEach(yIndex -> {
            IntStream.range(0, config.getColumns()).forEach(xIndex -> {
                final Optional<Cell> cell = findCell(cells, xIndex, yIndex);
                if (cell.isEmpty()) {
                    final long adjacentMines = cells.stream().filter(Cell::isMine)
                        .filter(c -> Math.abs(c.getX() - xIndex) <= 1 && Math.abs(c.getY() - yIndex) <= 1).count();
                    cells.add(new ValueCell(xIndex, yIndex, CellStatus.CLEAR, adjacentMines));
                }
            });
        });
        return new BoardDTO(cells, config.getMines());
    }

    public static BoardDTO emptyBoard(final Config config) {
        return new BoardDTO(Collections.emptyList(), config.getMines());
    }
}
