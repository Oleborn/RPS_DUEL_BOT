package oleborn.rps_duel.applogic.service;

import lombok.RequiredArgsConstructor;
import oleborn.rps_duel.applogic.dictionary.Messages;
import oleborn.rps_duel.applogic.dictionary.RPSName;
import oleborn.rps_duel.applogic.dictionary.WinMessage1Player;
import oleborn.rps_duel.applogic.dictionary.WinMessage2Players;
import oleborn.rps_duel.applogic.model.dto.DuelPlayerIds;
import oleborn.rps_duel.applogic.model.dto.WinResultDto;
import oleborn.rps_duel.applogic.model.entities.Duel;
import oleborn.rps_duel.applogic.model.entities.Player;
import oleborn.rps_duel.applogic.service.interfaces.DuelService;
import oleborn.rps_duel.applogic.service.interfaces.PlayerStatService;
import oleborn.rps_duel.bot.outputMethods.OutputsMethods;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class DuelManager {

    private final OutputsMethods outputsMethods;
    private final DuelService duelService;
    private final PlayerStatService playerStatService;

    // Активные дуэли по ID
    private final Map<UUID, Duel> activeDuels = new ConcurrentHashMap<>();

    // Планировщик задач (ожидание выбора)
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public void startDuel(Player player1, Player player2, Long chatId) {

        Duel duel = duelService.createDuel(player1, player2, chatId);

        if (player2.getUsername().equals("VirtualBot")) {
            duel.setChoice2(RPSName.getRandomRPC().name());
        }

        if (player1.getUsername().equals("VirtualBot")) {
            duel.setChoice1(RPSName.getRandomRPC().name());
        }

        activeDuels.put(duel.getId(), duel);

        sendMessageToPlayerAndChat(duel);

        playerStatService.addFight(player1);
        playerStatService.addFight(player2);

        // Планируем завершение дуэли через 1 минуту
        scheduler.schedule(() -> handleTimeout(duel.getId()), 2, TimeUnit.MINUTES);
    }

    private void sendMessageToPlayerAndChat(Duel duel) {
        outputsMethods.outputMessage(
                duel.getChatId(),
                Messages.MESSAGE_TO_START_FIGHT.getText().formatted(duel.getPlayer2().getUsername(), duel.getPlayer1().getUsername())
        );

        if (!duel.getPlayer1().getUsername().equals("VirtualBot")) {
            outputsMethods.outputMessage(
                    duel.getPlayer1().getId(),
                    Messages.MESSAGE_TO_CHOICE_RPS.getText(),
                    Messages.MESSAGE_TO_CHOICE_RPS.createKeyboardMarkup(duel, duel.getPlayer1())
            );
        }

        if (!duel.getPlayer2().getUsername().equals("VirtualBot")) {
            outputsMethods.outputMessage(
                    duel.getPlayer2().getId(),
                    Messages.MESSAGE_TO_CHOICE_RPS.getText(),
                    Messages.MESSAGE_TO_CHOICE_RPS.createKeyboardMarkup(duel, duel.getPlayer2())
            );
        }
    }

    // Вызывается из CallbackQueryHandler при ответе игрока
    public void submitPlayerChoice(DuelPlayerIds duelPlayerIds) {

        Duel duel = activeDuels.get(duelPlayerIds.duelId());
        if (duel == null) return;

        boolean updated = false;

        if (duel.getPlayer1().getId().equals(duelPlayerIds.playerId()) && duel.getChoice1() == null) {
            duel.setChoice1(duelPlayerIds.choice());
            updated = true;
        } else if (duel.getPlayer2().getId().equals(duelPlayerIds.playerId()) && duel.getChoice2() == null) {
            duel.setChoice2(duelPlayerIds.choice());
            updated = true;
        }

        if (updated && duel.getChoice1() != null && duel.getChoice2() != null) {
            activeDuels.remove(duelPlayerIds.duelId());
            resolveDuel(duel);
        }
    }

    private void handleTimeout(UUID duelId) {

        Duel duel = activeDuels.remove(duelId);
        if (duel == null) return;

        String choice1 = duel.getChoice1();
        String choice2 = duel.getChoice2();

        WinResultDto winResultDto = new WinResultDto();

        if (choice1 == null && choice2 == null) {
            sendToBoth(duel);
            duel.setFinishedAt(ZonedDateTime.now());
            duelService.save(duel);
            return;
        } else if (choice1 == null) {

            winResultDto.setWinner(duel.getPlayer2());
            winResultDto.setLoser(duel.getPlayer1());
            winResultDto.setChoiceWinner(choice2);
            winResultDto.setChoiceLoser(choice1);

            sendToPlayer(duel.getPlayer1(), "Вы не сделали выбор вовремя. Вы проиграли.");
            sendToPlayer(duel.getPlayer2(), "Противник не сделал выбор вовремя. Вы победили!");
        } else if (choice2 == null) {

            winResultDto.setWinner(duel.getPlayer1());
            winResultDto.setLoser(duel.getPlayer2());
            winResultDto.setChoiceWinner(choice1);
            winResultDto.setChoiceLoser(choice2);

            sendToPlayer(duel.getPlayer2(), "Вы не сделали выбор вовремя. Вы проиграли.");
            sendToPlayer(duel.getPlayer1(), "Противник не сделал выбор вовремя. Вы победили!");
        }

        duel.setFinishedAt(ZonedDateTime.now());
        duel.setWinner(winResultDto.getWinner());

        Duel savedDuel = duelService.save(duel);

        String winnerName = winResultDto.getWinner().getUsername() == null ? String.valueOf(winResultDto.getWinner().getId()) : winResultDto.getWinner().getUsername();
        String loserName = winResultDto.getLoser().getUsername() == null ? String.valueOf(winResultDto.getLoser().getId()) : winResultDto.getLoser().getUsername();

        playerStatService.addWin(winResultDto.getWinner());
        playerStatService.addLoss(winResultDto.getLoser());

        outputsMethods.outputMessage(duel.getChatId(),
                WinMessage1Player.getRandomMessage().getText()
                        .formatted(winnerName, loserName, winnerName, winResultDto.getChoiceWinner(), loserName)
        );

    }

    private void resolveDuel(Duel duel) {

        WinResultDto winResultDto = getWinnerPlayer(duel);

        duel.setFinishedAt(ZonedDateTime.now());
        duel.setWinner(winResultDto.getWinner());

        Duel savedDuel = duelService.save(duel);

        if (winResultDto.getWinner() != null && winResultDto.getLoser() != null) {

            playerStatService.addWin(winResultDto.getWinner());
            playerStatService.addLoss(winResultDto.getLoser());

            outputsMethods.outputMessage(duel.getChatId(),
                    WinMessage2Players.getRandomMessage().getText().formatted(
                            winResultDto.getWinner().getUsername(),
                            winResultDto.getLoser().getUsername(),
                            winResultDto.getWinner().getUsername(),
                            winResultDto.getChoiceWinner(),
                            winResultDto.getChoiceLoser()
                    )
            );
        } else {
            String noWinner = "Ничья, игроки выбрали одинаковые варианты - %s!";
            outputsMethods.outputMessage(duel.getChatId(), noWinner.formatted(savedDuel.getChoice1()));
        }

    }

    private WinResultDto getWinnerPlayer(Duel duel) {
        String choice1 = duel.getChoice1();
        String choice2 = duel.getChoice2();

        WinResultDto winResultDto = new WinResultDto();

        if (choice1.equals(choice2)) {
            winResultDto.setChoiceWinner(choice1);
            winResultDto.setChoiceLoser(choice2);
        } else if (
                (choice1.equals("Rock") && choice2.equals("Scissors")) ||
                (choice1.equals("Scissors") && choice2.equals("Paper")) ||
                (choice1.equals("Paper") && choice2.equals("Rock"))
        ) {
            winResultDto.setWinner(duel.getPlayer1());
            winResultDto.setLoser(duel.getPlayer2());
            winResultDto.setChoiceWinner(duel.getChoice1());
            winResultDto.setChoiceLoser(duel.getChoice2());
        } else {
            winResultDto.setWinner(duel.getPlayer2());
            winResultDto.setLoser(duel.getPlayer1());
            winResultDto.setChoiceWinner(duel.getChoice2());
            winResultDto.setChoiceLoser(duel.getChoice1());
        }
        return winResultDto;
    }

    private void sendToBoth(Duel duel) {
        outputsMethods.outputMessage(
                duel.getChatId(),
                "Игрок: %s и Игрок: %s не сделали выбор вовремя. Вы чо уснули там?!".formatted(duel.getPlayer1().getUsername(), duel.getPlayer2().getUsername())
        );
    }

    private void sendToPlayer(Player player, String message) {
        outputsMethods.outputMessage(
                player.getId(),
                message
        );
    }
}
