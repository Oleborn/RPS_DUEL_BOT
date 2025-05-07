package oleborn.rps_duel.bot.updatehandler;

import lombok.RequiredArgsConstructor;
import oleborn.rps_duel.bot.outputMethods.OutputsMethods;
import oleborn.rps_duel.util.UtilsMethods;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;


@Component
@RequiredArgsConstructor
public class MessagesHandler implements Handler {

    private final OutputsMethods outputsMethods;
    private final CommandHandler commandHandler;

    @Override
    public void handleUpdate(Update update) {
        if (update.getMessage().getText().startsWith("/")) {
            commandHandler.handleUpdate(update);
        } else {
           //outputsMethods.outputMessage(update.getMessage().getFrom().getId(), update.getMessage().getText());
        }
    }
}
