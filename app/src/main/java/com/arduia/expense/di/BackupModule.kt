package com.arduia.expense.di

import android.app.Activity
import android.content.Context
import com.arduia.backup.BackupSheet
import com.arduia.backup.ExcelBackup
import com.arduia.backup.FileNameGenerator
import com.arduia.backup.generator.BackupNameGenerator
import com.arduia.expense.data.ExpenseRepository
import com.arduia.expense.data.backup.ExpenseBackupSheet
import com.arduia.expense.data.backup.ExpenseBackupSource
import com.arduia.expense.data.backup.SchemaBackupSource
import com.arduia.expense.data.local.ExpenseEnt
import com.arduia.expense.ui.BackupMessageReceiver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Qualifier
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
object BackupModule {

    @Provides
    @Singleton
    fun provideExpenseSheet(source: ExpenseBackupSource): BackupSheet<ExpenseEnt> =
        ExpenseBackupSheet("expense_1", source)

    @Provides
    fun provideSchemaFactory(@ApplicationContext context: Context) =
        SchemaBackupSource.Factory(context)

    @Provides
    @Singleton
    fun provideExpenseSource(repo: ExpenseRepository) =
        ExpenseBackupSource(repo)

    @Provides
    @Singleton
    fun provideExcelBackup(expenseSheet: BackupSheet<ExpenseEnt>) =
        ExcelBackup.Builder()
            .addBackupSheet(expenseSheet)
            .build()

    @Provides
    @Singleton
    @BackupNameGen
    fun provideBackupNameGen(): FileNameGenerator = BackupNameGenerator()

}

@Qualifier
@MustBeDocumented
@Retention(AnnotationRetention.BINARY)
annotation class BackupNameGen
