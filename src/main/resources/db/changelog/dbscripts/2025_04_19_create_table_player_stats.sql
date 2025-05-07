CREATE TABLE player_stats (
                       player_id BIGINT PRIMARY KEY,
                       wins INT NOT NULL DEFAULT 0,
                       losses INT NOT NULL DEFAULT 0,
                       points INT NOT NULL DEFAULT 0,
                       total_games INT NOT NULL DEFAULT 0,
                       last_game TIMESTAMP,

                       CONSTRAINT fk_stats_player FOREIGN KEY (player_id) REFERENCES players(id)
);

CREATE INDEX idx_stats_wins ON player_stats(wins DESC);
CREATE INDEX idx_stats_total_games ON player_stats(total_games DESC);