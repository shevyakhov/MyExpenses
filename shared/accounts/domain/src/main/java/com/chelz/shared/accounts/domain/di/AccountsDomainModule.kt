package com.chelz.shared.accounts.domain.di

import com.chelz.shared.accounts.domain.usecase.account.DeleteAccountUseCase
import com.chelz.shared.accounts.domain.usecase.account.GetAccountByIdUseCase
import com.chelz.shared.accounts.domain.usecase.account.GetAllAccountsUseCase
import com.chelz.shared.accounts.domain.usecase.account.InsertAccountUseCase
import com.chelz.shared.accounts.domain.usecase.account.UpdateAccountUseCase
import com.chelz.shared.accounts.domain.usecase.categories.DeleteCategoryUseCase
import com.chelz.shared.accounts.domain.usecase.categories.GetAllCategoriesUseCase
import com.chelz.shared.accounts.domain.usecase.categories.GetCategoryByIdUseCase
import com.chelz.shared.accounts.domain.usecase.categories.InsertCategoryUseCase
import com.chelz.shared.accounts.domain.usecase.categories.UpdateCategoryUseCase
import com.chelz.shared.accounts.domain.usecase.operation.DeleteOperationUseCase
import com.chelz.shared.accounts.domain.usecase.operation.GetAllOperationsUseCase
import com.chelz.shared.accounts.domain.usecase.operation.GetOperationByIdUseCase
import com.chelz.shared.accounts.domain.usecase.operation.GetOperationsByCategoryUseCase
import com.chelz.shared.accounts.domain.usecase.operation.GetOperationsByDateUseCase
import com.chelz.shared.accounts.domain.usecase.operation.InsertOperationUseCase
import com.chelz.shared.accounts.domain.usecase.operation.UpdateOperationUseCase
import org.koin.dsl.module

val AccountsDomainModule = module {
	factory { DeleteCategoryUseCase(get()) }
	factory { GetAllCategoriesUseCase(get()) }
	factory { GetCategoryByIdUseCase(get()) }
	factory { InsertCategoryUseCase(get()) }
	factory { UpdateCategoryUseCase(get()) }
	factory { DeleteAccountUseCase(get()) }
	factory { GetAccountByIdUseCase(get()) }
	factory { GetAllAccountsUseCase(get()) }
	factory { InsertAccountUseCase(get()) }
	factory { UpdateAccountUseCase(get()) }
	factory { DeleteOperationUseCase(get()) }
	factory { GetAllOperationsUseCase(get()) }
	factory { GetOperationByIdUseCase(get()) }
	factory { GetOperationsByCategoryUseCase(get()) }
	factory { GetOperationsByDateUseCase(get()) }
	factory { InsertOperationUseCase(get()) }
	factory { UpdateOperationUseCase(get()) }
}