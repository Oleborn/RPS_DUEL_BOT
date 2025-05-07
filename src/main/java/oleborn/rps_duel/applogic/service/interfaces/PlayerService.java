package oleborn.rps_duel.applogic.service.interfaces;

import oleborn.rps_duel.applogic.model.entities.Player;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface PlayerService {

    Player saveNewPlayer(Update update);

    Player getPlayer(long id);

    void updatePlayer(Player player);

    boolean deletePlayer(long id);

    void createPlayers(int count);

}
