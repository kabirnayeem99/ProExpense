package com.arduia.expense.ui.backup

import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.arduia.expense.data.BackupRepository
import com.arduia.expense.data.backup.ImportWorker
import com.arduia.expense.di.IntegerDecimal
import com.arduia.mvvm.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.DecimalFormat

class ImportViewModel @ViewModelInject constructor(
    private val backupRepo: BackupRepository,
    private val contentResolver: ContentResolver,
    @IntegerDecimal
    private val decimalFormat: DecimalFormat,
    private val workManger: WorkManager
) : ViewModel() {

    private val _fileName = BaseLiveData<String>()
    val fileName = _fileName.asLiveData()

    private val _totalCount = BaseLiveData<String>()
    val totalCount = _totalCount.asLiveData()

    private val _closeEvent = EventLiveData<Unit>()
    val closeEvent = _closeEvent.asLiveData()

    private val _fileNotFoundEvent = EventLiveData<Unit>()
    val fileNotFoundEvent = _fileNotFoundEvent.asLiveData()

    private var currentSelectedUri: Uri? = null

    fun setFileUri(uri: Uri) {

        currentSelectedUri = uri

        viewModelScope.launch(Dispatchers.IO) {

            val itemCount = backupRepo.getItemCount(uri).first()
            val isInvalidItem = (itemCount < 0)

            if (isInvalidItem) {
                _fileNotFoundEvent post EventUnit
                _closeEvent post EventUnit
                return@launch
            }

            val fileName = getFileNameFromUri(uri = uri)

            _fileName post fileName

            _totalCount post decimalFormat.format(itemCount)
        }
    }

    private fun getFileNameFromUri(uri: Uri): String {
        val cursor = contentResolver.query(uri, null, null, null, null)

        val fileNameColumnIndex = cursor!!.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        cursor.moveToFirst()
        return cursor.getString(fileNameColumnIndex)
    }

    fun startImportData() {
        val fileUri = currentSelectedUri?.toString() ?: return

        val inputData = Data.Builder()
            .putString(ImportWorker.FILE_URI, fileUri).build()

        val importRequest = OneTimeWorkRequestBuilder<ImportWorker>()
            .setInputData(inputData)
            .build()

        workManger.enqueue(importRequest)

        _closeEvent post EventUnit
    }
}