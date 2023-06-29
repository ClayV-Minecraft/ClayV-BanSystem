package de.clayv.bansystem.objects;

import de.clayv.bansystem.utils.BanManager;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

public class Ban {

    @Getter private final UUID playerUUID;
    @Getter private final UUID bannerUUID;
    @Getter private final BanReason.Reason reason;
    @Getter private final long timeStampAsLong;
    @Getter private final String serverName;
    @Getter @Setter private long end;

    public Ban(
            UUID playerUUID,
            UUID bannerUUID,
            BanReason.Reason reason,
            String serverName)
    {
        this.playerUUID = playerUUID;
        this.bannerUUID = bannerUUID;
        this.reason = reason;
        this.timeStampAsLong = System.currentTimeMillis();
        this.serverName = serverName;
        this.end = BanManager.getEndTimeAsLong(reason);
    }
}
