package com.arduia.expense.data.backup

import android.content.Context
import com.arduia.backup.BackupSource
import com.arduia.core.content.getApplicationVersionCode
import com.arduia.expense.backup.schema.BackupSchema
import com.arduia.expense.backup.schema.table.Table
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SchemaBackupSource private constructor(
    private val context: Context,
    private val currencyCode: String,
    private val list: List<Table>
) : BackupSource<BackupSchema> {

    override suspend fun write(item: BackupSchema) {}

    override suspend fun writeAll(items: List<BackupSchema>) {}

    override suspend fun readAll(): List<BackupSchema> {
        return listOf(
            BackupSchema(
                appVersionCode = context.getApplicationVersionCode(),
                exportTables = list,
                currencyCode = currencyCode
            )
        )
    }

    override suspend fun totalCountAll() = 1

    class Factory @Inject constructor(
        @ApplicationContext private val context: Context
    ) {
        fun create(currencyCode: String, tables: List<Table>): SchemaBackupSource {
            return SchemaBackupSource(context, currencyCode, tables)
        }
    }
}