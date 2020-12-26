package com.arduia.expense.data.backup

interface ExpenseBackupSheetPool {

    var sheetIndex: Int

    fun getNewSheet(): ExpenseBackupSheet

}