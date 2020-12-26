package com.arduia.expense.data.backup

import com.arduia.backup.BackupSheet
import com.arduia.backup.SheetFieldInfo
import com.arduia.backup.SheetRow
import com.arduia.expense.backup.Metadata
import com.arduia.expense.backup.schema.BackupSchema
import com.google.gson.Gson

class SchemaBackupSheet(source: SchemaBackupSource) :
    BackupSheet<BackupSchema>(source) {

    private val gson = Gson()

    override val sheetName: String
        get() = Metadata.SHEET_NAME

    override fun getFieldInfo(): SheetFieldInfo {
        val map = mutableMapOf<String, String>()
        map[Metadata.FIELD_NAME] = "string"
        map[Metadata.FIELD_VALUE] = "string"
        return SheetFieldInfo.createFromMap(map)
    }

    override fun mapToEntity(row: SheetRow): BackupSchema {
        val schemaData = row[Metadata.FIELD_VALUE]!!
        return gson.fromJson(schemaData, BackupSchema::class.java)
    }

    override fun mapToSheetRow(item: BackupSchema): SheetRow {
        val map = mutableMapOf<String, String>()
        map[Metadata.FIELD_NAME] = "schema"
        map[Metadata.FIELD_VALUE] = gson.toJson(item)
        return SheetRow.createFromMap(map)
    }
}