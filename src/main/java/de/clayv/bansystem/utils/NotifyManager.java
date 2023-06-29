package de.clayv.bansystem.utils;

import com.velocitypowered.api.proxy.Player;
import de.clayv.bansystem.BanSystem;
import de.clayv.bansystem.objects.Ban;

import de.clayv.bansystem.uuid.NameFetcher;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class NotifyManager {

    private final BanSystem banSystem;

    public NotifyManager(BanSystem banSystem) {
        this.banSystem = banSystem;
    }

    public void notifyBan(Ban ban) {
        if(ban == null) { return; }
        banSystem.getThreadPool().execute(() -> {
            banSystem.getProxyServer().getAllPlayers().forEach( player -> {
                //if(player.hasPermission("bansystem.notify.ban")) {
                    Component message = Component.text("System -> ", NamedTextColor.DARK_RED)
                            .append(Component.text().content(
                                    NameFetcher.getName(ban.getPlayerUUID()) +
                                    " wurde von " +
                                    NameFetcher.getName(ban.getBannerUUID())).color(NamedTextColor.GRAY))
                            .append(Component.text().content(" gebannt! ").color(NamedTextColor.RED));
                    player.sendMessage(message);
                //}
            });
        });
    }

    public void notifyUnban(Ban ban, Player sender) {
        if(ban == null) { return; }
        banSystem.getThreadPool().execute(() -> {
            banSystem.getProxyServer().getAllPlayers().forEach( player -> {
                //if(player.hasPermission("bansystem.notify.unban")) {
                    Component message = Component.text("System -> ", NamedTextColor.DARK_RED)
                            .append(Component.text().content(
                                    NameFetcher.getName(ban.getPlayerUUID()) +
                                            " wurde von " +
                                            sender.getUsername()).color(NamedTextColor.GRAY))
                            .append(Component.text().content(" entbannt! ").color(NamedTextColor.GREEN));
                    player.sendMessage(message);
                //}
            });
        });
    }
}
