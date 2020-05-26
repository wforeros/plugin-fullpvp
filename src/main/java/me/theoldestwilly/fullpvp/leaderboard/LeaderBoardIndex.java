package me.theoldestwilly.fullpvp.leaderboard;

public enum LeaderBoardIndex {
    KILLS("kills", "Kills"), DEATHS("deaths", "Deaths"), EVENT_WINS("event-wins", "Event Wins"), MAX_KILLSTREAK("maximumKillStreak", "Max KS"), ECONOMY("balance", "Balance"), KILLSTREAK("killStreak", "KillStreak"), RANDOM("random", "random");
    private String dbIndex;
    private String displayName;

    LeaderBoardIndex(String dbIndex, String displayName) {
        this.dbIndex = dbIndex;
        this.displayName = displayName;
    }
    public String getDbIndex() {
        return dbIndex;
    }

    public String getDisplayName() {
        return displayName;
    }
}
