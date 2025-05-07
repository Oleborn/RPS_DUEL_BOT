package oleborn.rps_duel.bot.outputMethods;

import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;


/**
 * Создаем постоянную клавиатуру снизу
 */
@NoArgsConstructor
public class ReplyKeyboardBuilder {
    private final List<KeyboardRow> keyboard = new ArrayList<>();
    private KeyboardRow currentRow = null;


    // Добавить кнопку в текущую строку
    public ReplyKeyboardBuilder addButton(String text) {
        if (currentRow == null || currentRow.size() >= 9) {
            currentRow = new KeyboardRow();
            keyboard.add(currentRow);
        }
        currentRow.add(new KeyboardButton(text));
        return this;
    }

    // Закончить текущую строку и начать новую
    public ReplyKeyboardBuilder nextRow() {
        currentRow = new KeyboardRow();
        keyboard.add(currentRow);
        return this;
    }

    // Собрать и вернуть готовую клавиатуру
    public ReplyKeyboardMarkup build() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true); // Уменьшает размер кнопок
        keyboardMarkup.setOneTimeKeyboard(false); // Клавиатура остается после нажатия
        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }
}
