package com.chelz.shared.accounts.domain.di

import androidx.room.Room
import com.chelz.shared.accounts.domain.database.AccountsDatabase
import com.chelz.shared.accounts.domain.datasource.AccountDataSource
import com.chelz.shared.accounts.domain.datasource.AccountDataSourceImpl
import com.chelz.shared.accounts.domain.datasource.CategoryDataSource
import com.chelz.shared.accounts.domain.datasource.CategoryDataSourceImpl
import com.chelz.shared.accounts.domain.datasource.OperationDataSource
import com.chelz.shared.accounts.domain.datasource.OperationDataSourceImpl
import com.chelz.shared.accounts.domain.datasource.UserDataSource
import com.chelz.shared.accounts.domain.datasource.UserDataSourceImpl
import com.chelz.shared.accounts.domain.repository.AccountRepository
import com.chelz.shared.accounts.domain.repository.AccountRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

internal val accountsDatabaseModule = module {
	single {
		Room.databaseBuilder(androidContext(), AccountsDatabase::class.java, AccountsDatabase.NAME)
			.fallbackToDestructiveMigration()
			.build()
	}
}

internal val DaoModule = module {
	single { get<AccountsDatabase>().accountsDao() }
	single { get<AccountsDatabase>().categoryDao() }
	single { get<AccountsDatabase>().operationDao() }
	single { get<AccountsDatabase>().userDao() }
}

internal val DataSourceModule = module {
	single<AccountDataSource> { AccountDataSourceImpl(get()) }
	single<CategoryDataSource> { CategoryDataSourceImpl(get()) }
	single<OperationDataSource> { OperationDataSourceImpl(get()) }
	single<UserDataSource> { UserDataSourceImpl(get()) }
}

internal val RepositoryModule = module {
	single<AccountRepository> {
		AccountRepositoryImpl(
			accountDataSource = get(),
			operationDataSource = get(),
			categoryDataSource = get(),
			userDataSource = get()
		)
	}

}

val AccountsDataModule = listOf(
	accountsDatabaseModule,
	DaoModule,
	DataSourceModule,
	RepositoryModule
)