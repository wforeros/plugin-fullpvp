package me.theoldestwilly.fullpvp.users.tutorial;

import lombok.Getter;
import me.theoldestwilly.fullpvp.Lang;
import org.bukkit.Bukkit;
import org.bukkit.Location;

@Getter
public class TutorialStep {
    private Location location;
    private String message;
    private boolean allowedMovement;
    private int number;

    public TutorialStep(String s, int number) {
        String[] split = s.split("::");
        if (split.length < 5) {
            Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + Lang.ERROR_COLOR + "Failed while loading a tutorial location, check into your config.yml file the format. " +
                    "Correct format world::x::y::z::movement-allowed::message. Example world::200::10::-200::false::This is an example");
            return;
        }
        location = new Location(Bukkit.getWorld(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]));
        allowedMovement = Boolean.parseBoolean(split[4]);
        message = split[5];
        this.number = number;
    }
}
