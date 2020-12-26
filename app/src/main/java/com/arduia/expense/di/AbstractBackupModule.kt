package com.arduia.expense.di

import com.arduia.expense.data.backup.SchemaBackupSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
abstract class AbstractBackupModule {


}