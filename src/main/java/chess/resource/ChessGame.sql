DROP TABLE IF EXISTS piece;
DROP TABLE IF EXISTS chessgame;
USE chess;

CREATE TABLE chessgame (
    id BIGINT NOT NULL AUTO_INCREMENT,
    game_turn VARCHAR(5),
    PRIMARY KEY (id)
);

CREATE TABLE piece (
    id BIGINT NOT NULL AUTO_INCREMENT,
    game_id BIGINT NOT NULL,
    piece_type VARCHAR(6),
    piece_color VARCHAR(5),
    position_row VARCHAR(1),
    position_column VARCHAR(1),
    PRIMARY KEY (id),
    FOREIGN KEY (game_id) REFERENCES chessgame(id) ON DELETE CASCADE
);
