package com.example.moviedisplayapp.utils

interface PagingSource <Item>{

    suspend fun loadNext()

    suspend fun reset()
}

class DefaultPagingSource<Key, Item>(
    private val initialKey : Key,
    private val requestedItems: suspend (key : Key) -> List<Item>,
    private val onSuccess: suspend (nextItems: List<Item>, nextKey: Key) -> Unit,
    private val getNextKey: suspend (List<Item>) -> Key,
) : PagingSource<Item>{
    private var currentKey = initialKey

    override suspend fun loadNext() {
        val items = requestedItems(this.currentKey)
    }

    override suspend fun reset() {
        TODO("Not yet implemented")
    }

}