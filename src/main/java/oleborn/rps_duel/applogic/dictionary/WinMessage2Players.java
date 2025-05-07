package oleborn.rps_duel.applogic.dictionary;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Random;

@Getter
@RequiredArgsConstructor
public enum WinMessage2Players {

    MESSAGE_TO_WIN_FIGHT_1("""
            В дуэли между %s и %s, победил %s использовав %s, против %s! Поздравляю! 
            """),
    MESSAGE_TO_WIN_FIGHT_2("""
            %s сразился с %s и одержал победу, выбрав %s против %s! Браво!
            """),
    MESSAGE_TO_WIN_FIGHT_3("""
            В эпичной битве %s против %s, %s просто великолепно использовал %s против %s! 
            """),
    MESSAGE_TO_WIN_FIGHT_4("""
            %s и %s скрестили оружие, но %s победил благодаря %s против %s!
            """),
    MESSAGE_TO_WIN_FIGHT_5("""
            В схватке %s с %s, %s вышел победителем, использовав %s против %s!
            """),
    MESSAGE_TO_WIN_FIGHT_6("""
            %s против %s: %s разгромил соперника, выбрав %s над %s! 
            """),
    MESSAGE_TO_WIN_FIGHT_7("""
            Дуэль %s и %s завершилась триумфом %s с %s против %s! 
            """),
    MESSAGE_TO_WIN_FIGHT_9("""
            В поединке %s против %s, %s одержал верх с %s над %s! 
            """),
    MESSAGE_TO_WIN_FIGHT_10("""
            %s и %s сражались, но %s победил, использовав %s против %s! 
            """),
    MESSAGE_TO_WIN_FIGHT_11("""
            %s сокрушил %s, выбрав %s против %s! Поздравляем везунчика! 
            """),
    MESSAGE_TO_WIN_FIGHT_12("""
            В дуэли %s против %s, %s стал победителем с %s над %s! 
            """),
    MESSAGE_TO_WIN_FIGHT_13("""
            %s превзошел %s, использовав %s против %s! Великолепно! 
            """),
    MESSAGE_TO_WIN_FIGHT_14("""
            %s и %s столкнулись, но %s одержал победу с %s против %s! 
            """),
    MESSAGE_TO_WIN_FIGHT_15("""
            В битве %s против %s, %s вышел вперед благодаря %s над %s! 
            """),
    MESSAGE_TO_WIN_FIGHT_17("""
            %s против %s: %s стал триумфатором с %s над %s! 
            """),
    MESSAGE_TO_WIN_FIGHT_19("""
            В схватке %s и %s, %s одержал победу с %s против %s! Ура! 
            """),
    MESSAGE_TO_WIN_FIGHT_20("""
            %s и %s бились не на жизнь, а на смерть, но %s победил с %s против %s!
            """);

    private final String text;

    public static WinMessage2Players getRandomMessage() {
        WinMessage2Players[] messages = values();
        return messages[new Random().nextInt(messages.length)];
    }

}
