package com.dgnt.quickTournamentMaker.data.tournament

import androidx.lifecycle.LiveData

interface ITournamentRepository {


    fun getAll(epoch: Long): LiveData<List<TournamentEntity>>
    suspend fun insert(vararg entity: TournamentEntity): List<Long>
    suspend fun insert(entities: List<TournamentEntity>): List<Long>
    suspend fun update(vararg entity: TournamentEntity):Int
    suspend fun update(entities: List<TournamentEntity>):Int
    suspend fun delete(vararg entity: TournamentEntity)
    suspend fun delete(entities: List<TournamentEntity>)
}