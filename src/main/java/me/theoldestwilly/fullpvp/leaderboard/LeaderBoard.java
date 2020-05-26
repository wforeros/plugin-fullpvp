package me.theoldestwilly.fullpvp.leaderboard;

import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import lombok.AllArgsConstructor;
import lombok.Getter;
import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.utilities.Config;
import me.theoldestwilly.fullpvp.utilities.JavaUtils;
import me.theoldestwilly.fullpvp.utilities.PersistableLocation;
import org.bson.Document;
import org.bukkit.configuration.ConfigurationSection;

import java.util.*;

@Getter
@AllArgsConstructor
public final class LeaderBoard {

    private final String name;
    private final String displayName;

    private final Map<PersistableLocation, Integer> signs = new HashMap<>();

    public List<LeaderBoardPlayer> getPlayers(int limit) {
        List<LeaderBoardPlayer> result = new ArrayList<>(limit);
        String index = name.equalsIgnoreCase(LeaderBoardIndex.RANDOM.getDbIndex()) ? FullPvP.getInstance().getLeaderBoardManager().getCurrentIndex().getDbIndex():name;
        FindIterable iterable = FullPvP.getInstance().getProfileHandler().getProfilesDatabase().find(Filters.exists(index)).sort(Sorts.descending(index)).limit(limit);
        iterable.forEach((Block<Document>) document -> result.add(new LeaderBoardPlayer((UUID) document.get("_id"), document.get(index))));

        return result;
    }

    public void loadSignLocations(Config config) {
        Object object = config.getConfigurationSection(displayName);
        if (object != null) {
            ConfigurationSection section = (ConfigurationSection) object;
            for (String key : section.getKeys(false)) {
                Integer position = JavaUtils.tryParseInt(key);
                if (position == null) continue;
                signs.put((PersistableLocation) config.get(section.getCurrentPath() + '.' + key), position - 1);
            }
        }
    }

    public boolean isRandom() {
        return name.equalsIgnoreCase("random");
    }

    public void saveSignLocations(Config config) {
        Map<String, PersistableLocation> result = new LinkedHashMap<>(signs.size());

        signs.entrySet().forEach((entry) -> result.put(String.valueOf(entry.getValue() + 1), entry.getKey()));

        config.set(displayName, result);
    }
}