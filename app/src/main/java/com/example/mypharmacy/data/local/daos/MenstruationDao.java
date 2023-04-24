package com.example.mypharmacy.data.local.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.mypharmacy.data.local.entities.Menstruation;
@Dao
public interface MenstruationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertMenstruation(Menstruation menstruation);

    @Query("SELECT * FROM Menstruation order by id desc LIMIT 1;")
    Menstruation getMenstruation();
}



