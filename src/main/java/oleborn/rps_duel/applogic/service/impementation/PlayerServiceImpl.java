package oleborn.rps_duel.applogic.service.impementation;

import lombok.RequiredArgsConstructor;
import oleborn.rps_duel.applogic.model.entities.Player;
import oleborn.rps_duel.applogic.model.entities.PlayerStats;
import oleborn.rps_duel.applogic.repository.PlayerRepository;
import oleborn.rps_duel.applogic.repository.PlayerStatsRepository;
import oleborn.rps_duel.applogic.service.interfaces.PlayerService;
import oleborn.rps_duel.applogic.service.interfaces.PlayerStatService;
import oleborn.rps_duel.util.RandomStringGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;
    private final PlayerStatsRepository playerStatsRepository;
    private final PlayerStatService playerStatService;

    @Override
    public Player saveNewPlayer(Update update) {

        Optional<Player> byId = playerRepository.findById(update.getMessage().getFrom().getId());
        if (byId.isPresent()) {
            return byId.get();
        }

        Player buildPlayer = Player.builder()
                .id(update.getMessage().getFrom().getId())
                .username(update.getMessage().getFrom().getUserName())
                .createdAt(ZonedDateTime.now())
                .lastActive(ZonedDateTime.now())
                .build();

        PlayerStats newStat = playerStatService.createNewStat(buildPlayer);

        buildPlayer.setStats(newStat);

        return playerRepository.save(buildPlayer);
    }

    @Override
    public Player getPlayer(long id) {
        return null;
    }

    @Override
    public void updatePlayer(Player player) {

    }

    @Override
    public boolean deletePlayer(long id) {
        return false;
    }

    @Override
    @Transactional
    public void createPlayers(int count) {

        for (int i = 0; i < count; i++) {

            PlayerStats newStat = PlayerStats.builder()
                    .wins(new Random().nextInt(count))
                    .totalGames(new Random().nextInt(count))
                    .losses(new Random().nextInt(count))
                    .points(new Random().nextInt(count))
                    .build();


            Player player = Player.builder()
                    .id((long) i)
                    .stats(newStat)
                    .username(RandomStringGenerator.generateRandomName(15))
                    .lastActive(ZonedDateTime.now())
                    .createdAt(ZonedDateTime.now())
                    .build();

            newStat.setPlayer(player);

            playerRepository.save(player);
        }
    }
}
