package com.arduia.expense.data.backup

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.hilt.Assisted
import androidx.hilt.work.WorkerInject
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.arduia.backup.ExcelBackup
import com.arduia.backup.SheetImportTask
import com.arduia.backup.framework.*
import com.arduia.backup.task.getDataOrError
import com.arduia.expense.backup.schema.table.Table
import com.arduia.expense.data.BackupRepository
import com.arduia.expense.data.CurrencyRepository
import com.arduia.expense.data.ExpenseRepository
import com.arduia.expense.data.local.BackupEnt
import com.arduia.expense.model.awaitValueOrError
import java.util.*

class ExportWorker @WorkerInject constructor(
    @Assisted context: Context,
    @Assisted param: WorkerParameters,
    private val contentResolver: ContentResolver,
    private val expenseRepo: ExpenseRepository,
    private val schemaSourceFactory: SchemaBackupSource.Factory,
    private val currencyRepo: CurrencyRepository,
    private val backupRepo: BackupRepository
) : CoroutineWorker(context, param) {

    override suspend fun doWork(): Result {

        val inputFileUri = inputData.getString(FILE_URI) ?: return Result.failure()

        val inputFileName = inputData.getString(FILE_NAME) ?: return Result.failure()

        val excelBackup = createExcelBackup()

        val initialBackupLog =
            createBackupEntityForExportWork(exportName = inputFileName, exportUri = inputFileUri)

        backupRepo.insertBackup(item = initialBackupLog)

        val exportUri = Uri.parse(inputFileUri)

        val outputStream = contentResolver.openOutputStream(exportUri) ?: return Result.failure()

        val exportedItemCount = excelBackup.export(outputStream).getDataOrError()

        updateBackupLogAsCompleted(itemCount = exportedItemCount)

        return Result.success()
    }

    private fun createExcelBackup(): ExcelBackup {

        val backupBuilder = ExcelBackup.Builder()

        val expenseBackupSheetPool = ExpenseBackupSheetPoolImpl(ExpenseBackupSource(expenseRepo))

        val totalCount = expenseRepo.getExpenseTotalCount().awaitValueOrError()

        val count = (totalCount / 100_00) + 1

        val backupSheets = mutableListOf<ExpenseBackupSheet>()

        (1..count).forEach { _ ->
            backupSheets += expenseBackupSheetPool.getNewSheet()
        }

        val schemaSheets = getSchemaBackupSheet(backupSheets.map { it.getName() })

        backupSheets.forEach(backupBuilder::addBackupSheet)

        backupBuilder.addBackupSheet(schemaSheets)

        return backupBuilder.build()
    }

    private fun getSchemaBackupSheet(tableNames: List<String>): SchemaBackupSheet {

        val currencyCode = currencyRepo.getSelectedCacheCurrency().awaitValueOrError().code

        val table = Table(tableNames, ExpenseBackupSheet.getSchemaFieldInfo())

        return SchemaBackupSheet(schemaSourceFactory.create(currencyCode, listOf(table)))
    }

    private suspend fun updateBackupLogAsCompleted(itemCount: Int) {

        val oldBackupLog = backupRepo.getBackupByWorkerID(id.toString()).awaitValueOrError()

        oldBackupLog.isCompleted = true

        oldBackupLog.itemTotal = itemCount

        backupRepo.updateBackup(oldBackupLog)
    }

    private fun createBackupEntityForExportWork(exportName: String, exportUri: String) =
        BackupEnt(
            backupId = 0,
            name = exportName,
            filePath = exportUri,
            createdDate = Date().time,
            itemTotal = 0,
            workerId = id.toString(),
            isCompleted = false
        )

    companion object {
        const val FILE_URI = "FILE_URI"
        const val FILE_NAME = "FILE_NAME"
    }
}
