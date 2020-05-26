package me.theoldestwilly.fullpvp.event.hosted.structure;

import java.util.UUID;

public interface DuelsSystem {
    boolean isInDuel(UUID uuid);
    void selectDuel();
}
