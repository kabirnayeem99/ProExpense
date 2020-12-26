package com.arduia.backup.framework

interface DataSource<T>

interface ReadDataSource<T> : DataSource<T> {

    fun readItem(): T

    fun readItems(offset: Int, limit: Int): List<T>

    fun totalCount(): Int

}

interface ReadWriteDataSource<T> : ReadDataSource<T> {

    fun writeItem(item: T)

    fun writeItemAll(list: List<T>)

}