package gopdu.pdu.gopduversiondriver.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import gopdu.pdu.gopduversiondriver.object.Driver;

import static androidx.room.OnConflictStrategy.IGNORE;
import static androidx.room.OnConflictStrategy.REPLACE;

public @Dao interface DataDriverDao {
    @Insert(onConflict = REPLACE)
    void insertEmploy(Driver driver);
    @Insert(onConflict = IGNORE)
    void insertOrReplaceEmploy(Driver... drivers);
    @Update(onConflict = REPLACE)
    void updateEmploy(Driver driver);
    @Query("DELETE FROM Driver")
    void deleteAll();
    @Query("SELECT * FROM Driver")
    public List<Driver> findAllEmploySync();
}
