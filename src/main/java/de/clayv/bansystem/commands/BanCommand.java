package de.clayv.bansystem.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import de.clayv.bansystem.BanSystem;
import de.clayv.bansystem.objects.Ban;
import de.clayv.bansystem.objects.BanReason;
import de.clayv.bansystem.uuid.UUIDFetcher;
import net.kyori.adventure.text.Component;

import java.util.Objects;
import java.util.Optional;

public class BanCommand implements SimpleCommand {

    private final BanSystem banSystem;

    public BanCommand(BanSystem banSystem) {
        this.banSystem = banSystem;
        banSystem.getProxyServer().getCommandManager().register("ban", this);
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        Player player = (Player) source;
        String[] args = invocation.arguments();

        if(args.length != 2) {
            player.sendMessage(Component.text("Nutze /ban <name> <1-14>"));
            return;
        }

        String banPlayer = args[0];

        //if(player.getUsername().equalsIgnoreCase(banPlayer)) {
            //player.sendMessage(Component.text("Selbstbann"));
            //return;
        //}

        if(UUIDFetcher.getUUID(banPlayer) == null) {
            player.sendMessage(Component.text("Spieler nicht gefunden!"));
            return;
        }

        if(banSystem.getDb().getBan(Objects.requireNonNull(UUIDFetcher.getUUID(banPlayer))) != null) {
            player.sendMessage(Component.text("Bereits gebannt!"));
            return;
        }


        String banID = args[1];
        BanReason.Reason reason;

        switch(banID) {
            default:
                player.sendMessage(Component.text("Nutze /ban <name> <1-14>"));
                return;
            case "1": reason = BanReason.Reason.HACKING; break;
            case "2": reason = BanReason.Reason.BUGUSING; break;
            case "3": reason = BanReason.Reason.TEAMING; break;
            case "4": reason = BanReason.Reason.SPAWNTRAPPING; break;
            case "5": reason = BanReason.Reason.BANNUMGEHUNG; break;
            case "6": reason = BanReason.Reason.SICHERHEITSBAN; break;
            case "7": reason = BanReason.Reason.SKIN; break;
            case "8": reason = BanReason.Reason.NAME; break;
            case "9": reason = BanReason.Reason.STATSBOOSTING; break;
            case "10": reason = BanReason.Reason.BELEIDIGUNG; break;
            case "11": reason = BanReason.Reason.RASSISMUS; break;
            case "12": reason = BanReason.Reason.WERBUNG; break;
            case "13": reason = BanReason.Reason.PROVOKATION; break;
            case "14": reason = BanReason.Reason.SPAMMING; break;
        }

        String serverName;

        Optional<Player> optionalPlayer = banSystem.getProxyServer().getPlayer(banPlayer);
        if(optionalPlayer.isPresent()) {
            serverName = optionalPlayer.get().getCurrentServer().get().getServer().getServerInfo().getName();
        } else {
            serverName = "-";
        }

        Ban ban = new Ban(UUIDFetcher.getUUID(banPlayer), player.getUniqueId(), reason, serverName);

        if(reason.equals(BanReason.Reason.BANNUMGEHUNG) || reason.equals(BanReason.Reason.SICHERHEITSBAN)) {
            ban.setEnd(-1);
            optionalPlayer.ifPresent(optPlayer -> banSystem.getBanManager().tryKick(optPlayer, reason, true));
        } else {
            optionalPlayer.ifPresent(optPlayer -> banSystem.getBanManager().tryKick(optPlayer, reason, false));
        }

        banSystem.getDb().addBan(ban);
        banSystem.getNotifyManager().notifyBan(ban);

    }

}
