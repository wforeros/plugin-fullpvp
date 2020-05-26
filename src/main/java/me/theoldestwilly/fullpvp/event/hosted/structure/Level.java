package me.theoldestwilly.fullpvp.event.hosted.structure;

import lombok.Getter;
import me.theoldestwilly.fullpvp.clan.ClanMember;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import javax.xml.stream.Location;
import java.util.*;

public class Level implements ConfigurationSerializable {

    private Location spawn, finish;
    private int level;
    private List<Checkpoint> checkPoints = new ArrayList<>();
    private Long endTimeMillis = null;

    public Level (int level) {
        this.level = level;
    }

    public Level (Map<String, Object> map) {
        this((Integer) map.get("level"), (Location) map.get("spawn"), (Location) map.get("finish"), (List) map.get("checkpoints"));
    }

    public Level(int level, Location spawn, Location finish, List checkPoints) {
        this.spawn = spawn;
        this.finish = finish;
        this.level = level;
        setCheckPoints(checkPoints);
    }

    public void setCheckPoints(List object) {
        if (object != null) {
            Iterator<Checkpoint> var2 = object.iterator();
            while (var2.hasNext()) {
                this.checkPoints.add(var2.next());
            }
        } else {
            this.checkPoints = new ArrayList();
        }
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("level", level);
        map.put("spawn", spawn);
        map.put("finish", finish);
        map.put("checkpoints", checkPoints);
        return map;
    }

    public class Checkpoint implements ConfigurationSerializable {
        @Getter int number;
        @Getter  Location location;

        public Checkpoint(int number, Location location) {
            this.number = number;
            this.location = location;
        }

        @Override
        public Map<String, Object> serialize() {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("number", number);
            map.put("location", location);
            return map;
        }
    }

}
