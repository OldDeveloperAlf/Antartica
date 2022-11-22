package wtf.myles.hcfcore.utils.mongo;

import com.mongodb.DB;
import com.mongodb.DBAddress;
import com.mongodb.MongoClient;

/**
 * Created by Myles on 26/06/2015.
 */
public class MongoDB
{
    private DB database;

    public MongoDB(DBAddress address)
    {
        this.database = MongoClient.connect(address);
    }

    public MongoDB(DBAddress address, String username, char[] password)
    {
        this.database = MongoClient.connect(address);
        this.database.authenticate(username, password);
    }

    public DB getDatabase()
    {
        return this.database;
    }
}
