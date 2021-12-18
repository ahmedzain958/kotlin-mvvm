package com.emedinaa.kotlinmvvm.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.emedinaa.kotlinmvvm.data.db.DbDataSource
import com.emedinaa.kotlinmvvm.data.db.MuseumDTO

class MuseumDbRepository(private val dataSource: DbDataSource) {

    fun getMuseums(): LiveData<List<Museum>> {
        val museumsFromDbDataSourceLiveData: LiveData<List<MuseumDTO>> = dataSource.museums()
        //converts from LiveData<List<MuseumDTO>> to LiveData<List<Museum>>
        return Transformations.map(museumsFromDbDataSourceLiveData) {
            it.map { itItem -> Museum(itItem.id, itItem.name, itItem.photo) }//here is List<Museum>
        }
        //converts from LiveData<List<MuseumDTO>> to LiveData<List<Museum>>
        return Transformations.switchMap(museumsFromDbDataSourceLiveData) {
            MutableLiveData<List<Museum>>()//here is MutableLiveData<List<Museum>>
        }
    }

    suspend fun sync(museumList: List<Museum>) {
        dataSource.deleteMuseums()
        dataSource.addMuseums(museumList.map {
            MuseumDTO(it.id, it.name, it.photo)
        })
    }
}