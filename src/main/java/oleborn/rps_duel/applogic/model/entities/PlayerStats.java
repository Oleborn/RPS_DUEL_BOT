package oleborn.rps_duel.applogic.model.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "player_stats")
@Builder
public class PlayerStats {

    @Id
    @Column(name = "player_id")
    private Long playerId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "player_id")
    private Player player;

    private int wins;
    private int losses;

    private int points;

    @Column(name = "total_games")
    private int totalGames;

    @Column(name = "last_game")
    private ZonedDateTime lastGame;
}
