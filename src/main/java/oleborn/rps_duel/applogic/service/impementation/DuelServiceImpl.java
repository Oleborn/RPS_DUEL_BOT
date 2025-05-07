package oleborn.rps_duel.applogic.service.impementation;

import lombok.RequiredArgsConstructor;
import oleborn.rps_duel.applogic.model.entities.Duel;
import oleborn.rps_duel.applogic.model.entities.Player;
import oleborn.rps_duel.applogic.repository.DuelRepository;
import oleborn.rps_duel.applogic.service.interfaces.DuelService;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DuelServiceImpl implements DuelService {

    private final DuelRepository duelRepository;

    @Override
    public Duel createDuel(Player player1, Player player2, long chatId) {
        return Duel.builder()
                .id(UUID.randomUUID())
                .chatId(chatId)
                .player1(player1)
                .player2(player2)
                .startedAt(ZonedDateTime.now())
                .build();
    }

    @Override
    public Duel save(Duel duel) {
        return duelRepository.save(duel);
    }

}
