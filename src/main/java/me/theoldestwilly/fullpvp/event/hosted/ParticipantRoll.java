package me.theoldestwilly.fullpvp.event.hosted;

public enum ParticipantRoll {
    SPECTATOR, PLAYER;

    ParticipantRoll() {
    }

    public enum SubRoll {
        PLAYING, DUEL_WINNER, SPECTATOR, WAITING_DUEL, WAITING_NEXT_ROUND;
        SubRoll(){

        }
    }

}
