package sand.lsartor.voltorb.factories;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;
import sand.lsartor.voltorb.dto.BoardDTO;
import sand.lsartor.voltorb.dto.Config;
import sand.lsartor.voltorb.model.Cell;
import sand.lsartor.voltorb.model.CellStatus;
import sand.lsartor.voltorb.model.CellType;

public class BoardFactory {
    public static BoardDTO createBoard(final Config config) {
        final List<Cell> cells = new ArrayList<>();
        IntStream.range(0, config.getColumns()).forEach(x ->
            IntStream.range(0, config.getRows()).forEach(y -> {
              cells.add(new Cell(x, y, CellStatus.CLEAR, CellType.FIVE));
        }));
        return new BoardDTO(cells, config.getMines());
    }

    public static BoardDTO emptyBoard(final Config config) {
        return new BoardDTO(Collections.emptyList(), config.getMines());
    }
}
