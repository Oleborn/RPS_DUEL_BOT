package oleborn.rps_duel.applogic.model.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "duels")
@Builder
public class Duel {

    @Id
    private UUID id;

    @Column(name = "chat_id")
    private Long chatId;

    @ManyToOne
    @JoinColumn(name = "player1_id")
    private Player player1;

    @ManyToOne
    @JoinColumn(name = "player2_id")
    private Player player2;

    private String choice1;
    private String choice2;

    @ManyToOne
    @JoinColumn(name = "winner_id")
    private Player winner;

    @Column(name = "started_at")
    private ZonedDateTime startedAt = ZonedDateTime.now();

    @Column(name = "finished_at")
    private ZonedDateTime finishedAt;
}
