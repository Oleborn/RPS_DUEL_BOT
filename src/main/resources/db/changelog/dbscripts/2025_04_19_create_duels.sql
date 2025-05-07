CREATE TABLE duels (
                       id UUID PRIMARY KEY,
                       chat_id BIGSERIAL NOT NULL,
                       player1_id BIGINT NOT NULL,
                       player2_id BIGINT NOT NULL,
                       choice1 VARCHAR(20),
                       choice2 VARCHAR(20),
                       winner_id BIGINT,
                       started_at TIMESTAMP NOT NULL DEFAULT NOW(),
                       finished_at TIMESTAMP,
                       CONSTRAINT fk_duel_p1 FOREIGN KEY (player1_id) REFERENCES players(id),
                       CONSTRAINT fk_duel_p2 FOREIGN KEY (player2_id) REFERENCES players(id),
                       CONSTRAINT fk_duel_winner FOREIGN KEY (winner_id) REFERENCES players(id)
);

CREATE INDEX idx_duels_players ON duels(player1_id, player2_id);