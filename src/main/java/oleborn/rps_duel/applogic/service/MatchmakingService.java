package oleborn.rps_duel.applogic.service;

import lombok.RequiredArgsConstructor;
import oleborn.rps_duel.applogic.model.entities.Player;
import oleborn.rps_duel.applogic.repository.PlayerRepository;
import oleborn.rps_duel.bot.outputMethods.OutputsMethods;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.*;


@Service
@RequiredArgsConstructor
public class MatchmakingService {

    private final OutputsMethods outputsMethods;
    private final DuelManager duelManager;
    private final PlayerRepository playerRepository;

    // Потокобезопасная очередь для ожидания игроков на матч
    private final Queue<WaitingPlayer> queue = new ConcurrentLinkedQueue<>();

    // Планировщик задач — используется для установки таймаута на каждого игрока
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(4);

    private final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

    // Метод, вызываемый при добавлении игрока в очередь на матч
    // synchronized — чтобы избежать состояния гонки при одновременном добавлении игроков
    public synchronized void joinQueue(Player player, long chatId) {
        // Проверяем, не находится ли игрок уже в очереди
        if (checkQueue(player)) return; // если уже есть — ничего не делаем

        // Оборачиваем игрока в вспомогательный класс WaitingPlayer
        WaitingPlayer waitingPlayer = new WaitingPlayer(player);

        // Добавляем в очередь ожидания
        queue.add(waitingPlayer);

        if (queue.size() == 1) {
            outputsMethods.outputMessage(
                    chatId,
                    "%s вызвался на дуэль! Кто станет противником?".formatted(waitingPlayer.player.getUsername()));
        }

        // Планируем задачу: если через 1 минуту соперник не найден — удалить игрока из очереди
        scheduler.schedule(() -> timeoutPlayer(waitingPlayer, chatId), 1, TimeUnit.MINUTES);

        // Если после добавления в очереди стало минимум 2 игрока — запускаем матч
        if (queue.size() >= 2) {
            startMatchIfReady(chatId);
        }
    }

    // Метод, проверяющий, есть ли 2 игрока для матча, и запускающий дуэль
    // synchronized — чтобы избежать конфликтов, если два игрока одновременно добавились
    private synchronized void startMatchIfReady(long chatId) {

        // Извлекаем первых двух игроков из очереди
        Player p1 = Objects.requireNonNull(queue.poll()).player();
        Player p2 = Objects.requireNonNull(queue.poll()).player();

        System.out.println("Матч стартовал с игроками: " + p1.getUsername() + " и " + p2.getUsername());

        // Запускаем дуэль между ними
        executor.submit(() -> duelManager.startDuel(p1, p2, chatId));

    }

    // Метод, вызываемый по таймеру через 1 минуту, чтобы удалить игрока, если он так и не дождался дуэли
    private synchronized void timeoutPlayer(WaitingPlayer wp, long chatId) {
        // Удаляем игрока, если он всё ещё в очереди
        queue.add(new WaitingPlayer(playerRepository.findByUsername("VirtualBot").get()));


        outputsMethods.outputMessage(
                wp.player.getId(),
                "Никто не захотел с тобой сражаться поэтому с тобой сразится виртуальный противник!");

        System.out.println("Игрок " + wp.player().getUsername() + " сражается с виртуальным противником");

        // Если после добавления в очереди стало минимум 2 игрока — запускаем матч
        if (queue.size() >= 2) {
            startMatchIfReady(chatId);
        }
    }

    public boolean checkQueue(Player player) {
        return queue.stream()
                .anyMatch(wp -> wp.player().getId().equals(player.getId()));
    }

    // Вспомогательная структура — просто обёртка над Player
    // Нужна, чтобы мы могли различать объекты в очереди (в случае если в будущем добавим время добавления, id и т.п.)
    private record WaitingPlayer(Player player) {
    }
}
