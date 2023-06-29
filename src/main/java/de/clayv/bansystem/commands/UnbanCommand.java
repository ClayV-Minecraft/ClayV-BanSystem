package de.clayv.bansystem.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import de.clayv.bansystem.BanSystem;
import de.clayv.bansystem.objects.Ban;
import de.clayv.bansystem.uuid.UUIDFetcher;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class UnbanCommand implements SimpleCommand {

    private final BanSystem banSystem;

    public UnbanCommand(BanSystem banSystem) {
        this.banSystem = banSystem;
        banSystem.getProxyServer().getCommandManager().register("unban", this);
    }


    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        Player player = (Player) source;
        String[] args = invocation.arguments();

        if(args.length != 1) {
            // send Usage
            return;
        }

        String unbanPlayer = args[0];

        if(UUIDFetcher.getUUID(unbanPlayer) == null) {
            // Spieler existiert nicht!
            return;
        }

        Ban ban = banSystem.getDb().getBan(Objects.requireNonNull(UUIDFetcher.getUUID(unbanPlayer)));
        if(ban == null) {
            // Spieler ist nicht gebannt
        } else {
            banSystem.getDb().deleteBan(ban);
            banSystem.getNotifyManager().notifyUnban(ban, player);
        }
    }

}
