package oleborn.rps_duel.applogic.service.interfaces;

import oleborn.rps_duel.applogic.model.PlayerStatsProjection;
import oleborn.rps_duel.applogic.model.entities.Player;
import oleborn.rps_duel.applogic.model.entities.PlayerStats;
import org.springframework.data.domain.Pageable;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public interface PlayerStatService {

    PlayerStats createNewStat(Player player);

    void addFight(Player player);

    void addWin(Player player);

    void addLoss(Player player);

    String getPlayerStatsProjection(Pageable pageable);

}
