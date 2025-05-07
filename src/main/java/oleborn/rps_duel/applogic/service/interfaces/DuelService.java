package oleborn.rps_duel.applogic.service.interfaces;


import oleborn.rps_duel.applogic.model.entities.Duel;
import oleborn.rps_duel.applogic.model.entities.Player;

public interface DuelService {

    Duel createDuel(Player player1, Player player2, long chatId);

    Duel save(Duel duel);
}
