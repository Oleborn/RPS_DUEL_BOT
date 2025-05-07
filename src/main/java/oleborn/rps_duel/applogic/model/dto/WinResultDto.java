package oleborn.rps_duel.applogic.model.dto;

import lombok.*;
import oleborn.rps_duel.applogic.model.entities.Player;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class WinResultDto {
        private Player winner;
        private Player loser;
        private String choiceWinner;
        private String choiceLoser;
}
