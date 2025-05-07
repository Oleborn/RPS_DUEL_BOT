package oleborn.rps_duel.applogic.repository;

import oleborn.rps_duel.applogic.model.entities.Duel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DuelRepository extends JpaRepository<Duel, Long> {

    @Query("SELECT d FROM Duel d WHERE d.player1.id = :playerId OR d.player2.id = :playerId ORDER BY d.startedAt DESC")
    List<Duel> findRecentDuelsByPlayer(@Param("playerId") Long playerId, Pageable pageable);

    @Query("SELECT COUNT(d) FROM Duel d WHERE d.winner.id = :playerId")
    long countWinsByPlayer(@Param("playerId") Long playerId);
}
