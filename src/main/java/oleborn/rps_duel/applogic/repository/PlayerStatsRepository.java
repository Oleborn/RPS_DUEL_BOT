package oleborn.rps_duel.applogic.repository;

import oleborn.rps_duel.applogic.model.PlayerStatsProjection;
import oleborn.rps_duel.applogic.model.entities.PlayerStats;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface PlayerStatsRepository extends JpaRepository<PlayerStats, Long> {

    @Query(value = """
        SELECT 
            p.username as playerName,
            ps.points as points,
            ps.total_games as totalGames,
            ROW_NUMBER() OVER(
                ORDER BY 
                    ps.points DESC, 
                    ps.total_games DESC,
                    ps.wins DESC
            ) as position
        FROM player_stats ps
        JOIN players p ON ps.player_id = p.id
        """, nativeQuery = true)
    Page<PlayerStatsProjection> findAllWithRanking(Pageable pageable);

}
