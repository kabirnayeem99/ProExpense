package com.arduia.backup.framework

abstract class ImportTask<Entity>(protected val dataSource: ReadWriteDataSource<Entity>) :
    ExcelTask<ReadWriteExcel> {

    private lateinit var source: ReadWriteExcel

    final override suspend fun start(param: TaskParam): TaskResult {
        return import(source, param)
    }

    final override fun setSource(source: ReadWriteExcel) {
        this.source = source
    }

    abstract suspend fun import(excelSource: ReadWriteExcel, param: TaskParam): TaskResult

}