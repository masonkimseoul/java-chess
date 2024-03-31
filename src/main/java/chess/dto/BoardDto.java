package chess.dto;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import chess.model.Board;
import chess.model.piece.Color;
import java.util.ArrayList;
import java.util.List;

public class BoardDto {

    private static final int MAX_INDEX = 7;
    private static final String DELIMITER = "\n";

    private final List<RankDto> ranks;
    private final Color turn;

    public BoardDto(List<RankDto> rank, Color turn) {
        this.ranks = rank;
        this.turn = turn;
    }

    public static BoardDto from(Board board, Color turn) {
        List<RankDto> ranks = new ArrayList<>();
        for (int i = 0; i <= MAX_INDEX; i++) {
            ranks.add(RankDto.of(board, i));
        }
        return new BoardDto(ranks, turn);
    }

    public Color getTurn() {
        return turn;
    }

    @Override
    public String toString() {
        return ranks.stream()
                .map(RankDto::toString)
                .collect(collectingAndThen(toList(), list -> String.join(DELIMITER, list)));
    }
}
