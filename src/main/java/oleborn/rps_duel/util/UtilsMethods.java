package oleborn.rps_duel.util;

import oleborn.rps_duel.applogic.model.dto.DuelPlayerIds;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.UUID;


public class UtilsMethods {

    public static long searchId(Update update) {
        if (update.hasMessage()) {
            return update.getMessage().getFrom().getId();
        } else if (update.hasCallbackQuery()) {
            return update.getCallbackQuery().getFrom().getId();
        }
        return 0;
    }

    public static DuelPlayerIds parseIdsFromCallbackData(String data) {
        String[] parts = data.split("_");
        return new DuelPlayerIds(parts[0], UUID.fromString(parts[1]), Long.parseLong(parts[2]));
    }

}
