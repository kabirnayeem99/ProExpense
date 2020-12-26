package com.arduia.backup.framework

interface ExcelTask <S: ExcelSource>{

    suspend fun start(param: TaskParam): TaskResult

    fun setSource(source: S)
}

