package com.arduia.expense.data.backup

import com.arduia.backup.framework.ReadWriteDataSource
import com.arduia.expense.data.local.ExpenseEnt

class ExpenseReadWriteDataSource: ReadWriteDataSource<ExpenseEnt> {
    override fun readItem(): ExpenseEnt {
        TODO("Not yet implemented")
    }

    override fun readItemAll(): List<ExpenseEnt> {
        TODO("Not yet implemented")
    }

    override fun writeItem(item: ExpenseEnt) {
        TODO("Not yet implemented")
    }

    override fun writeItemAll(list: List<ExpenseEnt>) {
        TODO("Not yet implemented")
    }
}