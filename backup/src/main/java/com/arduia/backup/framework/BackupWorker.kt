package com.arduia.backup.framework

import java.util.*

interface ExcelWorker<S : ExcelSource> {

    suspend fun start(param: TaskParam): TaskResult

    interface Builder<S : ExcelSource>  {
        fun build(factory: ExcelSourceFactory<S>): ExcelWorker<S>
    }
}

class ExcelBackupWorker<S : ExcelSource> private constructor(
    private val source: S,
    private val tasks: Queue<ExcelTask<S>>
) : ExcelWorker<S> {

    override suspend fun start(param: TaskParam): TaskResult {
        var currentParam = param
        tasks.forEach {
            it.setSource(source = source)
            currentParam = it.start(currentParam) as TaskParam
        }
        source.close()
        return currentParam
    }

    open class Builder<S : ExcelSource> : ExcelWorker.Builder<S> {
        private val tasks: Queue<ExcelTask<S>> = ArrayDeque()

        fun enqueue(task: ExcelTask<S>): Builder<S>{
            tasks.add(task)
            return this
        }

        override fun build(factory: ExcelSourceFactory<S>): ExcelWorker<S> {
            return ExcelBackupWorker(factory.create(), tasks)
        }
    }
}

class ExportWorkerBuilder: ExcelBackupWorker.Builder<ReadWriteExcel>()

class ImportWorkerBuilder: ExcelBackupWorker.Builder<ReadOnlyExcel>()
