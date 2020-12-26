package com.arduia.backup.framework

import com.arduia.backup.SheetFieldInfo
import com.arduia.backup.SheetRow

interface SheetEntity<E>

interface ExportEntity<E> : SheetEntity<E> {

    suspend fun mapToRow(entity: E): SheetRow

    fun getFieldInfo(): SheetFieldInfo
}

interface ImportExportEntity<E> : ExportEntity<E> {

    suspend fun mapToEntity(row: SheetRow): E
}
