package oleborn.rps_duel.bot.updatehandler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class UpdateHandler {

    private final MessagesHandler messagesHandler;
    private final CallbackQueryHandler callbackQueryHandler;


    public void handler(Update update) {
        if (update.hasMessage()) {
            messagesHandler.handleUpdate(update);
        } else if (update.hasCallbackQuery()) {
            callbackQueryHandler.handleUpdate(update);
        }
    }
}