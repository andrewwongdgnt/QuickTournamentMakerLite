package com.dgnt.quickTournamentMaker.data.management

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PersonDAO {

    @Query("SELECT * FROM $PERSON_TABLE")
    fun getAll():LiveData<List<PersonEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg entity:PersonEntity):List<Long>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entities:List<PersonEntity>):List<Long>

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun update(vararg entity:PersonEntity):Int

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun update(entities:List<PersonEntity>):Int

    @Delete
    suspend fun delete(vararg entity:PersonEntity)

    @Delete
    suspend fun delete(entities:List<PersonEntity>)

    @Query("UPDATE $PERSON_TABLE SET groupName = :groupName WHERE groupName IN (:oldGroupNames)")
    suspend fun updateGroup(oldGroupNames: List<String>, groupName: String):Int

    @Query("DELETE FROM $PERSON_TABLE WHERE groupName IN (:groupNames)")
    suspend fun deleteViaGroup(groupNames:List<String>)
}