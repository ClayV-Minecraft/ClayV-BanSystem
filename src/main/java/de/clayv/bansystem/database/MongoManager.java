package de.clayv.bansystem.database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import de.clayv.bansystem.BanSystem;
import de.clayv.bansystem.objects.Ban;
import org.bson.Document;

import java.util.UUID;

public class MongoManager {

    private final BanSystem banSystem;
    private final MongoCollection<Document> banCollection;

    public MongoManager(BanSystem banSystem) {
        this.banSystem = banSystem;
        MongoClient mongoClient =
                MongoClients.create("mongodb://root:_2%23UVDM.xXRx%3DNaU%3D%23Hu%26Ue%23@172.18.0.1:27017/?authMechanism=SCRAM-SHA-1");
        MongoDatabase database = mongoClient.getDatabase("BanSystem");
        banCollection = database.getCollection("Ban");
    }

    public void addBan(Ban ban) {
        Document doc = banSystem.getGson().fromJson(banSystem.getGson().toJson(ban), Document.class);
        banCollection.insertOne(doc);
    }

    public Ban getBan(UUID uuid) {
        Document doc = banCollection.find(Filters.eq("playerUUID", uuid.toString())).first();
        if (doc == null) { return null; }
        return banSystem.getGson().fromJson(doc.toJson(), Ban.class);
    }

    public void deleteBan(Ban ban) {
        banCollection.deleteOne(Filters.eq("playerUUID", ban.getPlayerUUID().toString()));
    }
}
