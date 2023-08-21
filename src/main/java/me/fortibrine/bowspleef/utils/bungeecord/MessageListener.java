package me.fortibrine.bowspleef.utils.bungeecord;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import me.fortibrine.bowspleef.main.Main;
import me.fortibrine.bowspleef.utils.ServerType;
import me.fortibrine.bowspleef.utils.sql.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class MessageListener implements PluginMessageListener {

    private ServerType serverType;
    private PlayerManager playerManager;
    public MessageListener(Main plugin) {
        this.playerManager = plugin.getPlayerManager();
        this.serverType = plugin.getVariableManager().getServerType();
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("bowspleef")) return;
        if (serverType != ServerType.LOBBY) return;

        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();

        if (subchannel.equals("win")) {
            this.playerManager.addWin(player);
        }
    }

}
