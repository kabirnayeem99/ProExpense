package com.arduia.backup.framework

abstract class ExportTask<Entity>(protected val dataSource: ReadDataSource<Entity>) :
    ExcelTask<ReadWriteExcel> {

    private lateinit var excel: ReadWriteExcel

    final override suspend fun start(param: TaskParam): TaskResult {
        return export(excel, param)
    }

    final override fun setSource(source: ReadWriteExcel) {
         this.excel = source
    }

    abstract suspend fun export(source: ReadWriteExcel, param: TaskParam): TaskResult

}
