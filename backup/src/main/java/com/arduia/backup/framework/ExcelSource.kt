package com.arduia.backup.framework

import jxl.Workbook
import jxl.write.WritableWorkbook
import java.io.Closeable
import java.io.InputStream
import java.io.OutputStream

interface ExcelSource : Closeable

abstract class ReadWriteExcel : ExcelSource {
    abstract val book: WritableWorkbook

    override fun close() {
        book.close()
    }
}

abstract class ReadOnlyExcel : ExcelSource {
    abstract val book: Workbook
    override fun close() {
        book.close()
    }
}

private class ReadWriteExcelFactoryImpl(private val outputStream: OutputStream) :
    ReadWriteExcelFactory {

    override fun create(): ReadWriteExcel = object : ReadWriteExcel() {
        override val book: WritableWorkbook = Workbook.createWorkbook(outputStream)
    }
}

private class ReadOnlyExcelFactoryImpl(private val inputStream: InputStream) :
    ReadOnlyExcelFactory {
    override fun create(): ReadOnlyExcel = object : ReadOnlyExcel(){
        override val book: Workbook = Workbook.getWorkbook(inputStream)
    }
}


interface ExcelSourceFactory<S : ExcelSource> {
    fun create(): S
}

interface ReadWriteExcelFactory : ExcelSourceFactory<ReadWriteExcel> {
    companion object {
        fun create(outputStream: OutputStream): ReadWriteExcelFactory =
            ReadWriteExcelFactoryImpl(outputStream)
    }
}

interface ReadOnlyExcelFactory : ExcelSourceFactory<ReadOnlyExcel> {
    companion object {
        fun create(inputSource: InputStream): ReadOnlyExcelFactory =
            ReadOnlyExcelFactoryImpl(inputSource)
    }
}


