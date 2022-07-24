package com.example.moviedisplayapp.utils

import android.content.res.Resources

interface PagingSource <Item>{

    suspend fun loadNext()

    suspend fun reset()
}

class DefaultPagingSource<Key, Item>(
    private val initialKey : Key,
    private val requestedItems: suspend (key : Key) -> List<Item>,
    private val onSuccess: suspend (nextItems: List<Item>, nextKey: Key) -> Unit,
    private val onError: (throwable: Throwable?) -> Unit,
    private val getNextKey: suspend (currentKey : Key) -> Key,
) : PagingSource<Item>{
    private var currentKey = initialKey

    override suspend fun loadNext() {
        val items = requestedItems(this.currentKey)
        if(items.isNotEmpty()){
            this.currentKey = getNextKey(currentKey)
            onSuccess(items, currentKey)
        } else if(items.isEmpty() && currentKey == initialKey){
            onError(Resources.NotFoundException())
        }
    }

    override suspend fun reset() {
        currentKey = initialKey
    }

}