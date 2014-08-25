package lando.systems.ld30.utils;

/**
 * Brian Ploeckelman created on 8/25/2014.
 */
public class Stats {

    public static float playTime = 0;

    public static int redEnemiesKilled    = 0;
    public static int yellowEnemiesKilled = 0;
    public static int greenEnemiesKilled  = 0;
    public static int cyanEnemiesKilled   = 0;
    public static int blueEnemiesKilled   = 0;
    public static int purpleEnemiesKilled = 0;

    public static int playerDeaths = 0;

    public static boolean haveKilled() {
        return (redEnemiesKilled    > 0
             || yellowEnemiesKilled > 0
             || greenEnemiesKilled  > 0
             || cyanEnemiesKilled   > 0
             || purpleEnemiesKilled > 0);
    }

}
