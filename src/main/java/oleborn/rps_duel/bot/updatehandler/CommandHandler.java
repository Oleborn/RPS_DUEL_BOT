package oleborn.rps_duel.bot.updatehandler;

import lombok.RequiredArgsConstructor;
import oleborn.rps_duel.applogic.model.entities.Player;
import oleborn.rps_duel.applogic.service.MatchmakingService;
import oleborn.rps_duel.applogic.service.interfaces.PlayerService;
import oleborn.rps_duel.applogic.service.interfaces.PlayerStatService;
import oleborn.rps_duel.bot.outputMethods.OutputsMethods;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import static oleborn.rps_duel.applogic.dictionary.Messages.MESSAGE_TO_START;


@Component
@RequiredArgsConstructor
public class CommandHandler implements Handler {

    private final OutputsMethods outputsMethods;
    private final PlayerService playerService;
    private final PlayerStatService playerStatService;
    private final MatchmakingService matchmakingService;


    @Override
    public void handleUpdate(Update update) {

        if (update.getMessage().getText().startsWith("/table")){

            outputsMethods.outputMessage(
                    update.getMessage().getChatId(),
                    playerStatService.getPlayerStatsProjection(getPageable(update.getMessage().getText())),
                    "Markdown"
            );

            outputsMethods.deleteMessageInChat(update.getMessage().getChatId(), update.getMessage().getMessageId());
        }

        switch (update.getMessage().getText()) {
            case "/start" -> {
                outputsMethods.outputMessage(
                        update.getMessage().getFrom().getId(),
                        MESSAGE_TO_START.getText().formatted(update.getMessage().getFrom().getUserName())
                );
            }

            case "/duel" -> {

                if (update.getMessage().getFrom().getId().equals(update.getMessage().getChatId())) {
                    outputsMethods.outputMessage(
                            update.getMessage().getFrom().getId(),
                            "%s! Я не принимаю сообщения и команды в лс, пиши команду в чате!".formatted(update.getMessage().getFrom().getUserName())
                    );
                    return;
                }

                Player player = playerService.saveNewPlayer(update);

                if (!matchmakingService.checkQueue(player)) {
                    try {
                        outputsMethods.outputMessage(
                                update.getMessage().getFrom().getId(),
                                "%s, твоя заявка зарегистрирована. Ждем соперника".formatted(update.getMessage().getFrom().getUserName())
                        );
                    } catch (RuntimeException e) {
                        outputsMethods.outputMessage(
                                update.getMessage().getChatId(),
                                "Привет %s! Чтобы начать матч, перейди в @RPS_DUEL_RU_Bot и нажми старт, чтобы я мог присылать тебе сообщения.".formatted(update.getMessage().getFrom().getUserName())
                        );
                        return;
                    }


                    matchmakingService.joinQueue(player, update.getMessage().getChatId());
                } else {
                    outputsMethods.outputMessage(
                            update.getMessage().getFrom().getId(),
                            "Терпение %s! Ты уже в очереди на матч, дождись соперника!".formatted(update.getMessage().getFrom().getUserName())
                    );
                }

                outputsMethods.deleteMessageInChat(update.getMessage().getChatId(), update.getMessage().getMessageId());
            }

            case "/CRP" -> {
                playerService.createPlayers(100);

                outputsMethods.outputMessage(
                        update.getMessage().getFrom().getId(),
                        "Создано 100 человек!"
                );
                outputsMethods.deleteMessageInChat(update.getMessage().getChatId(), update.getMessage().getMessageId());
            }
        }
    }

    private Pageable getPageable(String query) {
        String[] split = query.split("_");

        int pageNumber = 0;

        try {
            pageNumber = Integer.parseInt(split[1]);
        } catch (IndexOutOfBoundsException e) {
            return PageRequest.of(pageNumber, 10, Sort.Direction.ASC, "position");
        }
        return PageRequest.of(pageNumber-1, 10, Sort.Direction.ASC, "position");
    }
}
