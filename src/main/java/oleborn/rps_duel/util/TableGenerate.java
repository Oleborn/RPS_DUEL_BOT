package oleborn.rps_duel.util;

import oleborn.rps_duel.applogic.model.PlayerStatsProjection;

import java.util.List;
import org.springframework.data.domain.Page;

public class TableGenerate {

    final static int MAX_NAME_LENGTH = 20;

    public static String generateTournamentTable(Page<PlayerStatsProjection> page) {

        List<PlayerStatsProjection> players = page.getContent();

        int maxName = players.stream()
                .mapToInt(player -> Math.min(player.getPlayerName().length(), MAX_NAME_LENGTH))
                .max()
                .orElse(15);

        StringBuilder table = new StringBuilder("```\n");

        // Заголовок таблицы
        String header = String.format(
                "%-4s %-" + (maxName + 2) + "s %6s %6s\n",
                "п/п", "Участник", "Матчи", "Очки"
        );

        String divider = "-".repeat(4 + maxName + 2 + 6 + 6 + 3) + "\n";
        table.append(header).append(divider);

        // Тело таблицы
        for (PlayerStatsProjection player : players) {
            String name = player.getPlayerName();
            if (name.length() > MAX_NAME_LENGTH) {
                name = name.substring(0, MAX_NAME_LENGTH - 1) + "…";
            }

            String row = String.format(
                    "%-4d %-" + (maxName + 2) + "s %6d %6d\n",
                    player.getPosition(),
                    name,
                    player.getTotalGames(),
                    player.getPoints()
            );
            table.append(row);
        }

        // Информация о пагинации
        table.append("```\n");
        table.append(String.format(
                "Страница: %d/%d | Размер: %d | Всего записей: %d",
                page.getNumber() + 1,      // Текущая страница (начинается с 0)
                page.getTotalPages(),      // Всего страниц
                page.getSize(),            // Элементов на странице
                page.getTotalElements()    // Всего записей
        ));

        return table.toString();
    }
}
