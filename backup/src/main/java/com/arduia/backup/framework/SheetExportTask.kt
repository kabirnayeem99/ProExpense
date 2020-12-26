package com.arduia.backup.framework

import jxl.write.Label
import jxl.write.WritableSheet

class SheetExportTask<E>(
    private val sheetEntity: ExportEntity<E>,
    dataSource: ReadDataSource<E>,
    private val paging: SheetPaging = NonSheetPaging()
) :
    ExportTask<E>(dataSource) {

    override suspend fun export(source: ReadWriteExcel, param: TaskParam): TaskResult {

        val sheetIndexOffset  = 1

        val itemCount = dataSource.totalCount()
        val pagination = paging.pagination
        val pageCount = (itemCount / pagination) + 1 // Page Count Upper flow

        (1..pageCount).forEachIndexed { offset, pageIndex ->
            //Read Items with pagination
            val backupItems = dataSource.readItems(offset * pagination, pageIndex * pagination)
            //name pro
            val sheetName = paging.provideName(pageIndex)
            val sheet = source.book.createSheet(sheetName, sheetIndexOffset + offset)
            val fields = sheetEntity.getFieldInfo().keys
            //Firstly Add Header
            writeHeaders(sheet, fields)
            writeItems(sheet, fields, backupItems)
        }

        return param
    }

    private fun writeHeaders(sheet: WritableSheet, fields: Set<String>) {
        fields.forEachIndexed { column, label ->
            sheet.addCell(Label(column, 0, label))
        }
    }

    private suspend fun writeItems(sheet: WritableSheet, fields: Set<String>, items: List<E>) {
        items.forEachIndexed { rowNo, data ->
            val dataRowNo = rowNo + 1
            val rawData = sheetEntity.mapToRow(data)
            fields.forEachIndexed { columnNo, columnName ->
                val cellContent = rawData[columnName] ?: ""
                sheet.addCell(Label(columnNo, dataRowNo, cellContent))
            }
        }
    }

}

private class NonSheetPaging : SheetPaging(pagination = Int.MAX_VALUE) {
    override fun provideName(pageIndex: Int): String {
        return "sheet_$pageIndex"
    }
}