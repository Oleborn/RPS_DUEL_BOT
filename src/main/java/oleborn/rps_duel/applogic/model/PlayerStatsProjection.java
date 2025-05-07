package oleborn.rps_duel.applogic.model;

public interface PlayerStatsProjection {
    String getPlayerName();
    int getPoints();
    int getTotalGames();
    int getPosition();
}