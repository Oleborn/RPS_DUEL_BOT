package oleborn.rps_duel.applogic.model.dto;

import java.util.UUID;

public record DuelPlayerIds(String choice, UUID duelId, Long playerId) {}