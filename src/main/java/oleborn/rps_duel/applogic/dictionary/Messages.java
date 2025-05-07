package oleborn.rps_duel.applogic.dictionary;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import oleborn.rps_duel.applogic.model.entities.Duel;
import oleborn.rps_duel.applogic.model.entities.Player;
import oleborn.rps_duel.bot.outputMethods.InlineKeyboardBuilder;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Getter
@RequiredArgsConstructor
public enum Messages {

    MESSAGE_TO_START_FIGHT("""
            %s принял вызов %s, ждем их ходов!
            """),

    MESSAGE_TO_START("""
            Привет %s!
            
            Я бот, который позволяет играть в
            камень \uD83E\uDEA8 - ножницы ✂️ - бумага \uD83D\uDCC4!
            
            Как это будет:
            
            - Теперь в чате ты можешь ввести команду /duel и попадаешь в очередь ожидания соперника.
            - Как только тебе найдется оппонент -  стартует дуэль в которой я пришлю тебе в лс вариант выбора.
            - После того как ты и твой соперник сделаете выбор, я в общем чате оглашу результаты.
             
            Видишь, как все просто! 
            
            Чтобы потом получить общую турнирную таблицу введи тут или в общем чате команду /table
            Чтобы выбрать иной лист таблицы введи /table_номер листа (пример: /table_2)
            """),

    MESSAGE_TO_CHOICE_RPS(
            "Выбери чем будем ходить?"

    );

    private final String text;

    public InlineKeyboardMarkup createKeyboardMarkup(Duel duel, Player player) {
        return new InlineKeyboardBuilder()
                .addButton("Выбрать ножницы - ✂\uFE0F", "Scissors_" + duel.getId() + "_" + player.getId())
                .nextRow()
                .addButton("Выбрать камень - \uD83E\uDEA8", "Rock_" + duel.getId() + "_" + player.getId())
                .nextRow()
                .addButton("Выбрать бумагу - \uD83D\uDCC4", "Paper_" + duel.getId() + "_" + player.getId())
                .build();
    }
}
