package oleborn.rps_duel.bot.updatehandler;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface Handler {
    void handleUpdate(Update update);

}
