package de.clayv.bansystem.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import de.clayv.bansystem.BanSystem;
import de.clayv.bansystem.objects.Ban;
import de.clayv.bansystem.uuid.NameFetcher;
import de.clayv.bansystem.uuid.UUIDFetcher;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class InfoCommand implements SimpleCommand {

    private final BanSystem banSystem;

    public InfoCommand(BanSystem banSystem) {
        this.banSystem = banSystem;
        banSystem.getProxyServer().getCommandManager().register("info", this);
        System.out.println("Info Registriert!");
    }

    @Override
    public void execute(final Invocation invocation) {
        CommandSource source = invocation.source();
        Player player = (Player) source;
        String[] args = invocation.arguments();

        if(args.length != 1) {
            player.sendMessage(Component.text("Nutze /info <name>"));
            return;
        }

        String infoPlayer = args[0];

        if(UUIDFetcher.getUUID(infoPlayer) == null) {
            player.sendMessage(Component.text("Spieler " + args[0] + " existiert nicht!"));
            return;
        }

        banSystem.getThreadPool().execute(() -> {
            Ban ban = banSystem.getDb().getBan(Objects.requireNonNull(UUIDFetcher.getUUID(infoPlayer)));

            Component message1 = Component.text("Informationen zu " +
                    NameFetcher.getName(Objects.requireNonNull(UUIDFetcher.getUUID(infoPlayer)))).color(NamedTextColor.GRAY);
            player.sendMessage(message1);

            Component message2;
            if(ban == null) {
                message2 = Component.text("Spieler ist nicht gebannt!").color(NamedTextColor.GREEN);
            } else {
                message2 = Component.text("Gebannt wegen " + ban.getReason() + " für " +
                        banSystem.getBanManager().convertToDisplayTime(ban.getEnd())).color(NamedTextColor.RED);
            }
            player.sendMessage(message2);
        });
    }
}
