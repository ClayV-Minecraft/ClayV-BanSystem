package de.clayv.bansystem.objects;

import java.util.concurrent.TimeUnit;

public class BanReason {

    public enum Reason {
        HACKING, BUGUSING, TEAMING,
        SPAWNTRAPPING, BANNUMGEHUNG,
        SICHERHEITSBAN, SKIN, NAME,
        STATSBOOSTING, BELEIDIGUNG,
        RASSISMUS, WERBUNG,
        PROVOKATION, SPAMMING
    }


    public static long getMillis(Reason reason) {
        long seconds = 0;
        switch (reason) {
            case HACKING:
            case NAME:
                seconds = TimeUnit.DAYS.toMillis(30);
                break;
            case BUGUSING:
                seconds = TimeUnit.DAYS.toMillis(5);
                break;
            case TEAMING:
                seconds = TimeUnit.DAYS.toMillis(2);
                break;
            case SPAWNTRAPPING:
                seconds = TimeUnit.HOURS.toMillis(2);
                break;
            case BANNUMGEHUNG:
            case SICHERHEITSBAN:
                seconds = -1;
                break;
            case SKIN:
            case BELEIDIGUNG:
            case PROVOKATION:
                seconds = TimeUnit.HOURS.toMillis(1);
                break;
            case STATSBOOSTING:
                seconds = TimeUnit.HOURS.toMillis(10);
                break;
            case RASSISMUS:
                seconds = TimeUnit.DAYS.toMillis(14);
                break;
            case WERBUNG:
                seconds = TimeUnit.DAYS.toMillis(1);
                break;
            case SPAMMING:
                seconds = TimeUnit.MINUTES.toMillis(30);
                break;
            default:
                break;
        }
        return seconds;
    }
}
