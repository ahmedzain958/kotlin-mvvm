package com.emedinaa.kotlinmvvm.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.emedinaa.kotlinmvvm.data.db.DbDataSource
import com.emedinaa.kotlinmvvm.data.db.MuseumDTO

class MuseumDbRepository(private val dataSource: DbDataSource) {

    fun getMuseums(): LiveData<List<Museum>> {
        val museumsFromDataSourceBeforeTransmission = dataSource.museums()
        return Transformations.map(museumsFromDataSourceBeforeTransmission){
            it.map {itItem ->
                Museum(itItem.id,itItem.name, itItem.photo)
            }
        }
    }

    suspend fun sync(museumList:List<Museum>){
        dataSource.deleteMuseums()
        dataSource.addMuseums(museumList.map {
            MuseumDTO(it.id,it.name,it.photo)
        })
    }
}