package gopdu.pdu.gopduversiondriver.Database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import gopdu.pdu.gopduversiondriver.Dao.DataDriverDao;
import gopdu.pdu.gopduversiondriver.object.Driver;

@Database(entities = {Driver.class}, version = 1)
public abstract class DataDriver extends RoomDatabase {

    private static DataDriver INSTANCE;


    public abstract DataDriverDao driverDao();


    public static DataDriver getInMemoryDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.inMemoryDatabaseBuilder(context.getApplicationContext(), DataDriver.class)
                            // To simplify the codelab, allow queries on the main thread.
                            // Don't do this on a real app! See PersistenceBasicSample for an example.
                            .allowMainThreadQueries()
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

}
