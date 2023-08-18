package me.fortibrine.bowspleef.arena;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Arena {

    private String name;
    private String server;
    private int needPlayers;
    private int maxPlayers;
    private List<UUID> players = new ArrayList<>();

    @Setter
    private boolean inGame = false;

    public Arena(String name, String server, int needPlayers, int maxPlayers) {
        this.name = name;
        this.server = server;
        this.needPlayers = needPlayers;
        this.maxPlayers = maxPlayers;
    }

}
