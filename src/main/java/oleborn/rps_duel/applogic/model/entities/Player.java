package oleborn.rps_duel.applogic.model.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "players")
@Builder
@ToString
public class Player {

    @Id
    private Long id; // Telegram user ID

    private String username;

    @Column(name = "created_at")
    private ZonedDateTime createdAt = ZonedDateTime.now();

    @Column(name = "last_active")
    private ZonedDateTime lastActive;

    @OneToOne(mappedBy = "player", cascade = CascadeType.ALL)
    private PlayerStats stats;

}
