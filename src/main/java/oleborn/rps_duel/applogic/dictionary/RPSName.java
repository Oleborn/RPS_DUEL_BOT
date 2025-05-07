package oleborn.rps_duel.applogic.dictionary;

import java.util.Random;

public enum RPSName {

    Rock,
    Paper,
    Scissors;

    public static RPSName getRandomRPC() {
        RPSName[] messages = values();
        return messages[new Random().nextInt(messages.length)];
    }

}
