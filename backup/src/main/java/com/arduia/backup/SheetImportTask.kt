package com.arduia.backup

import com.arduia.backup.framework.*
import jxl.Sheet

class SheetImportTask<E>(
    private val entity: ImportExportEntity<E>,
    dataSource: ReadWriteDataSource<E>
) : ImportTask<E>(dataSource) {
    override suspend fun import(excelSource: ReadWriteExcel, param: TaskParam): TaskResult {
        val sheet = excelSource.book.getSheet("name")
        val items = getDataFromSheet(entity, sheet)
        dataSource.writeItemAll(items)
        return param
    }

    private suspend fun getDataFromSheet(
        sheetEntity: ImportExportEntity<E>,
        sheet: Sheet
    ): List<E> {

        val sheetData = mutableListOf<E>()
        val rowCount = sheet.rows
        val rowDataTmp = mutableMapOf<String, String>()
        val rowNoList = (1 until rowCount)

        val fields = sheetEntity.getFieldInfo().keys

        rowNoList.forEach { rowNo ->
            fields.forEachIndexed { columnNo, columnName ->
                val cellContent = sheet.getCell(columnNo, rowNo).contents
                rowDataTmp[columnName] = cellContent
            }
            val rowItem = sheetEntity.mapToEntity(SheetRow.createFromMap(rowDataTmp))
            sheetData.add(rowItem)
            rowDataTmp.clear()
        }

        return sheetData
    }
}