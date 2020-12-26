package com.arduia.expense.data.backup

import com.arduia.backup.SheetFieldInfo
import com.arduia.backup.SheetRow
import com.arduia.backup.framework.ExportEntity
import com.arduia.backup.framework.ImportExportEntity
import com.arduia.expense.backup.schema.table.Field
import com.arduia.expense.data.local.ExpenseEnt
import com.arduia.expense.domain.Amount
import java.math.BigDecimal
import java.util.*

class ExpenseSheetEntity : ImportExportEntity<ExpenseEnt> {
    override suspend fun mapToRow(entity: ExpenseEnt): SheetRow {
        val data = mutableMapOf<String, String>().apply {
            put(FIELD_NAME, entity.name ?: "")
            put(FIELD_AMOUNT, entity.amount.getActual().toString())
            put(FIELD_CATEGORY, entity.category.toString())
            put(FIELD_NOTE, entity.note ?: "")
            put(FIELD_DATE, entity.createdDate.toString())
        }

        return SheetRow.createFromMap(data)
    }

    override fun getFieldInfo() = ExpenseSheetEntity.getFieldInfo()

    override suspend fun mapToEntity(row: SheetRow): ExpenseEnt {

        val name = row[FIELD_NAME]
        val amount = BigDecimal(row[FIELD_AMOUNT] ?: "0.0")
        val category = row[FIELD_CATEGORY]?.toInt() ?: 0
        val note = row[FIELD_NOTE]
        val date = row[FIELD_DATE]?.toLong() ?: 0

        return ExpenseEnt(
            0,
            name,
            Amount.createFromActual(amount),
            category,
            note,
            createdDate = Date().time,
            modifiedDate = date
        )
    }

    companion object {

        private const val FIELD_NAME = "Name"
        private const val FIELD_AMOUNT = "Amount"
        private const val FIELD_CATEGORY = "Category"
        private const val FIELD_NOTE = "Note"
        private const val FIELD_DATE = "Date"

        fun getFieldInfo(): SheetFieldInfo {

            val mutableMap = mutableMapOf<String, String>()

            mutableMap[FIELD_NAME] = "string"
            mutableMap[FIELD_DATE] = "long"
            mutableMap[FIELD_CATEGORY] = "integer"
            mutableMap[FIELD_AMOUNT] = "long"
            mutableMap[FIELD_NOTE] = "long"

            return SheetFieldInfo.createFromMap(mutableMap)
        }

        fun getSchemaFieldInfo() = getFieldInfo().map {
            Field(it.key, it.value)
        }
    }
}