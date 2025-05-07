package oleborn.rps_duel.applogic.model.entities;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import oleborn.rps_duel.applogic.repository.PlayerRepository;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
@RequiredArgsConstructor
public class VirtualPlayerInitializer {

    private final PlayerRepository playerRepository;

    public static final Long VIRTUAL_PLAYER_ID = -1L;

    @PostConstruct
    public void init() {
        if (playerRepository.existsById(VIRTUAL_PLAYER_ID)) {
            return;
        }

        Player virtualPlayer = Player.builder()
                .id(VIRTUAL_PLAYER_ID)
                .username("VirtualBot")
                .createdAt(ZonedDateTime.now())
                .lastActive(ZonedDateTime.now())
                .build();

        PlayerStats stats = PlayerStats.builder()
                .player(virtualPlayer)
                .wins(0)
                .losses(0)
                .points(0)
                .totalGames(0)
                .lastGame(null)
                .build();

        virtualPlayer.setStats(stats);

        playerRepository.save(virtualPlayer);
    }
}
