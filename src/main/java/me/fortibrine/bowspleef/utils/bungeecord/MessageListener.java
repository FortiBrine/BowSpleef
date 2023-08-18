package me.fortibrine.bowspleef.utils.bungeecord;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import me.fortibrine.bowspleef.main.Main;
import me.fortibrine.bowspleef.utils.SQLManager;
import me.fortibrine.bowspleef.utils.ServerType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class MessageListener implements PluginMessageListener {

    private ServerType serverType;
    private SQLManager sqlManager;
    public MessageListener(Main plugin) {
        this.sqlManager = plugin.getSqlManager();
        this.serverType = plugin.getVariableManager().getServerType();
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("bowspleef")) return;
        if (serverType != ServerType.LOBBY) return;

        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();

        if (subchannel.equals("win")) {
            this.sqlManager.setWins(player.getUniqueId(), this.sqlManager.getWins(player.getUniqueId())+1);
        }
    }

}
