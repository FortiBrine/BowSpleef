package me.fortibrine.bowspleef.arena;

import lombok.Getter;
import lombok.AllArgsConstructor;

@Getter
@AllArgsConstructor
public class Arena {

    private String name;
    private String server;
    private int needPlayers;
    private int maxPlayers;

}
