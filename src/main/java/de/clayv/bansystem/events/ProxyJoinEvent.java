package de.clayv.bansystem.events;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerPreConnectEvent;
import de.clayv.bansystem.BanSystem;
import de.clayv.bansystem.objects.Ban;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class ProxyJoinEvent {

    private final BanSystem banSystem;

    public ProxyJoinEvent(BanSystem banSystem) {
        this.banSystem = banSystem;
        banSystem.getProxyServer().getEventManager().register(banSystem, this);
    }

    @Subscribe(order = PostOrder.FIRST)
    public void onPlayerJoin(ServerPreConnectEvent e) {
        Ban ban = banSystem.getDb().getBan(e.getPlayer().getUniqueId());
        if(ban == null) { return; }

        long current = System.currentTimeMillis();
        long end = ban.getEnd();

        System.out.println(end);

        if (((current < end ? 1 : 0) | (end == -1L ? 1 : 0)) != 0) {
            Component message = Component.text("Du bist gebannt! ", NamedTextColor.RED)
                    .append(Component.text().content("Grund: " + ban.getReason()+ " ").color(NamedTextColor.GRAY))
                    .append(Component.text().content("Dauer: " + banSystem.getBanManager().convertToDisplayTime(ban.getEnd())).color(NamedTextColor.GRAY));
            e.getPlayer().disconnect(message);
        } else {
            banSystem.getDb().deleteBan(ban);
        }
    }
}
