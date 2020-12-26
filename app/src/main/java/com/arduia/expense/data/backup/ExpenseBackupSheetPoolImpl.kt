package com.arduia.expense.data.backup

class ExpenseBackupSheetPoolImpl(private val source: ExpenseBackupSource) : ExpenseBackupSheetPool {

    override var sheetIndex: Int = 0

    override fun getNewSheet(): ExpenseBackupSheet {
        sheetIndex++
        return ExpenseBackupSheet(generateName(), source)
    }

    private fun generateName(): String {
        return "$SHEET_NAME_SUFFIX$sheetIndex"
    }

    companion object {
        private const val SHEET_NAME_SUFFIX = "expense_"
    }

}