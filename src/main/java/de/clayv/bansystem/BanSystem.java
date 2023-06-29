package de.clayv.bansystem;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import de.clayv.bansystem.commands.BanCommand;
import de.clayv.bansystem.commands.InfoCommand;
import de.clayv.bansystem.commands.UnbanCommand;
import de.clayv.bansystem.database.MongoManager;
import de.clayv.bansystem.events.ProxyJoinEvent;
import de.clayv.bansystem.utils.BanManager;
import de.clayv.bansystem.utils.NotifyManager;
import lombok.Getter;
import org.slf4j.Logger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Plugin(
        id = "bansystem",
        name = "BanSystem",
        version = BuildConstants.VERSION
)
public class BanSystem {

    private final Logger logger;
    @Getter private final ProxyServer proxyServer;
    @Getter private NotifyManager notifyManager;
    @Getter private MongoManager db;
    @Getter private BanManager banManager;
    @Getter private Gson gson;

    private ExecutorService executorService;

    @Inject
    public BanSystem(ProxyServer proxyServer, Logger logger) {
        this.proxyServer = proxyServer;
        this.logger = logger;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        this.notifyManager = new NotifyManager(this);
        this.db = new MongoManager(this);
        this.gson = new Gson();
        this.banManager = new BanManager(this);
        this.executorService = Executors.newCachedThreadPool();

        new BanCommand(this);
        new InfoCommand(this);
        new UnbanCommand(this);
        new ProxyJoinEvent(this);
    }

    public ExecutorService getThreadPool() {
        return executorService;
    }
}
