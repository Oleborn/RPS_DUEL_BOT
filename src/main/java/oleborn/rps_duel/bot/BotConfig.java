package oleborn.rps_duel.bot;

import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class BotConfig {

    @Resource
    private Bot bot;

    @Bean
    public TelegramBotsApi startBot() {
        try {
            DefaultBotSession botSession = new DefaultBotSession();
            botSession.setOptions(new DefaultBotOptions());
            TelegramBotsApi botsApi = new TelegramBotsApi(botSession.getClass());
            botsApi.registerBot(bot);
            return botsApi;
        } catch (TelegramApiException e) {
            throw new RuntimeException("Ошибка запуска бота. Ищи в классе BotConfig", e);
        }
    }
}