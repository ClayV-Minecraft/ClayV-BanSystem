package de.clayv.bansystem.utils;

import com.velocitypowered.api.proxy.Player;
import de.clayv.bansystem.BanSystem;
import de.clayv.bansystem.objects.BanReason;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class BanManager {

    private final BanSystem banSystem;

    public BanManager(BanSystem banSystem) {
        this.banSystem = banSystem;
    }

    public void tryKick(Player player, BanReason.Reason reason, boolean perma) {
        if(player == null) { return; }
        String remainingTime;
        if(perma) {
            remainingTime = "Permanent";
        } else {
            remainingTime = convertToDisplayTime(getEndTimeAsLong(reason));
        }
        Component message = Component.text("Du bist gebannt! ", NamedTextColor.RED)
                .append(Component.text().content("Grund: " + reason + " ").color(NamedTextColor.GRAY))
                .append(Component.text().content("Dauer: " + remainingTime).color(NamedTextColor.GRAY));
        player.disconnect(message);
    }

    public static long getEndTimeAsLong(BanReason.Reason reason) {
        long millis = BanReason.getMillis(reason);
        if (millis == -1) {
            return -1;
        } else {
            long current = System.currentTimeMillis();
            return current + millis;
        }
    }

    public String convertToDisplayTime(long time) {
        if(time == -1) {
            return "Permanent";
        }

        time -= System.currentTimeMillis();
        long minutes = 0;
        while ((time / 1000) >= 60) {
            time -= 1000 * 60;
            minutes++;
        }
        long hours = 0;
        while (minutes >= 60) {
            minutes -= 60;
            hours++;
        }
        long days = 0;
        while (hours >= 24) {
            hours -= 24;
            days++;
        }
        if (days == 0) {
            if (hours == 0) {
                if (minutes == 0) {
                    return (time / 1000) + "sec";
                } else {
                    return minutes + "min";
                }
            } else {
                return hours + "h " + minutes + "min";
            }
        } else {
            return days + "d " + hours + "h";
        }
    }
}
