package me.fortibrine.bowspleef.arena;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
public class Arena {

    private String name;
    private String server;
    private int needPlayers;
    private int maxPlayers;
    private Set<UUID> players = new HashSet<>();

    @Setter
    private boolean inGame = false;

    public Arena(String name, String server, int needPlayers, int maxPlayers) {
        this.name = name;
        this.server = server;
        this.needPlayers = needPlayers;
        this.maxPlayers = maxPlayers;
    }

    public String insertArena(String str) {
        if (str == null) return "";

        return str.replace("%name", this.getName()).replace("%server", this.getServer()).replace("%need", String.valueOf(this.getNeedPlayers())).replace("%max", String.valueOf(this.getMaxPlayers())).replace("%all", String.valueOf(this.getPlayers().size()));
    }
}
