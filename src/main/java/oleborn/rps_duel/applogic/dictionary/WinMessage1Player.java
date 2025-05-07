package oleborn.rps_duel.applogic.dictionary;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Random;

@Getter
@RequiredArgsConstructor
public enum WinMessage1Player {

    MESSAGE_TO_WIN_FIGHT_1("""
            В дуэли между %s и %s, победил %s использовав %s, так как %s не явился на дуэль!
            """),
    MESSAGE_TO_WIN_FIGHT_2("""
            Между %s и %s, одержал победу %s, выбрав %s, ведь %s не пришел на бой!
            """),
    MESSAGE_TO_WIN_FIGHT_3("""
            В схватке %s против %s, %s триумфировал с %s, так как %s уклонился от дуэли!
            """),
    MESSAGE_TO_WIN_FIGHT_4("""
            В поединке %s и %s, %s одержал победу с %s, так как %s не пришел!
            """),
    MESSAGE_TO_WIN_FIGHT_5("""
            В битве %s против %s, %s стал чемпионом с %s, ведь %s не явился!
            """),
    MESSAGE_TO_WIN_FIGHT_6("""
            В дуэли %s и %s, %s победил с %s, так как %s не пришел на бой!
            """),
    MESSAGE_TO_WIN_FIGHT_7("""
            В поединке %s против %s, %s одержал победу с %s, потому что %s струсил!
            """),
    MESSAGE_TO_WIN_FIGHT_8("""
            В схватке %s и %s, %s стал победителем с %s, так как %s не явился!
            """);

    private final String text;

    public static WinMessage1Player getRandomMessage() {
        WinMessage1Player[] messages = values();
        return messages[new Random().nextInt(messages.length)];
    }

}
