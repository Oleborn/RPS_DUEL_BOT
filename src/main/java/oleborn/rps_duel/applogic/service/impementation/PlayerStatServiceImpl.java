package oleborn.rps_duel.applogic.service.impementation;

import lombok.RequiredArgsConstructor;
import oleborn.rps_duel.applogic.model.PlayerStatsProjection;
import oleborn.rps_duel.applogic.model.entities.Player;
import oleborn.rps_duel.applogic.model.entities.PlayerStats;
import oleborn.rps_duel.applogic.repository.PlayerRepository;
import oleborn.rps_duel.applogic.repository.PlayerStatsRepository;
import oleborn.rps_duel.applogic.service.interfaces.PlayerStatService;
import oleborn.rps_duel.util.TableGenerate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PlayerStatServiceImpl implements PlayerStatService {

    private final int winPoints = 3;
    private final int lossPoints = 0;
    private final int drawPoints = 1;


    private final PlayerStatsRepository playerStatsRepository;

    private final PlayerRepository playerRepository;

    @Override
    public PlayerStats createNewStat(Player player) {
        return PlayerStats.builder()
                .player(player)
                .build();
    }

    @Override
    public void addFight(Player player) {
        Optional<PlayerStats> byId = playerStatsRepository.findById(player.getId());
        if (byId.isPresent()) {
            PlayerStats stats = byId.get();
            stats.setTotalGames(stats.getTotalGames() + 1);
            stats.setLastGame(ZonedDateTime.now());
            playerStatsRepository.save(stats);
        }
    }

    @Override
    public void addWin(Player player) {
        Optional<PlayerStats> byId = playerStatsRepository.findById(player.getId());
        if (byId.isPresent()) {
            PlayerStats stats = byId.get();
            stats.setWins(stats.getWins() + 1);
            stats.setPoints(stats.getPoints() + winPoints);
            playerStatsRepository.save(stats);
        }
    }

    @Override
    public void addLoss(Player player) {
        Optional<PlayerStats> byId = playerStatsRepository.findById(player.getId());
        if (byId.isPresent()) {
            PlayerStats stats = byId.get();
            stats.setLosses(stats.getLosses() + 1);
            stats.setPoints(stats.getPoints() + lossPoints);
            playerStatsRepository.save(stats);
        }
    }

    @Override
    public String getPlayerStatsProjection(Pageable pageable) {

        Page<PlayerStatsProjection> allWithRanking = playerStatsRepository.findAllWithRanking(pageable);

        return TableGenerate.generateTournamentTable(allWithRanking);
    }
}
