package oleborn.rps_duel.bot.updatehandler;

import lombok.RequiredArgsConstructor;
import oleborn.rps_duel.applogic.model.dto.DuelPlayerIds;
import oleborn.rps_duel.applogic.service.DuelManager;
import oleborn.rps_duel.bot.outputMethods.OutputsMethods;
import oleborn.rps_duel.util.UtilsMethods;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;


@Component
@RequiredArgsConstructor
public class CallbackQueryHandler implements Handler {

    private final DuelManager duelManager;
    private final OutputsMethods outputsMethods;


    @Override
    public void handleUpdate(Update update) {

        switch (update.getCallbackQuery().getData()) {
            case String s when s.startsWith("Scissors") -> {
                duelManager.submitPlayerChoice(UtilsMethods.parseIdsFromCallbackData(update.getCallbackQuery().getData()));
                outputsMethods.deleteMessageInChat(update.getCallbackQuery().getMessage().getChatId(), update.getCallbackQuery().getMessage().getMessageId());
            }
            case String s when s.startsWith("Rock") -> {
                duelManager.submitPlayerChoice(UtilsMethods.parseIdsFromCallbackData(update.getCallbackQuery().getData()));
                outputsMethods.deleteMessageInChat(update.getCallbackQuery().getMessage().getChatId(), update.getCallbackQuery().getMessage().getMessageId());
            }
            case String s when s.startsWith("Paper") -> {
                duelManager.submitPlayerChoice(UtilsMethods.parseIdsFromCallbackData(update.getCallbackQuery().getData()));
                outputsMethods.deleteMessageInChat(update.getCallbackQuery().getMessage().getChatId(), update.getCallbackQuery().getMessage().getMessageId());
            }
            default -> System.out.println("Invalid callback query: " + update.getCallbackQuery().getData());

        }
    }
}
