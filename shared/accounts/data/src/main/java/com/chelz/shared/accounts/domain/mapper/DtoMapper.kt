package com.chelz.shared.accounts.domain.mapper

import com.chelz.shared.accounts.domain.dto.AccountDto
import com.chelz.shared.accounts.domain.dto.CategoryDto
import com.chelz.shared.accounts.domain.dto.OperationDto
import com.chelz.shared.accounts.domain.entity.Account
import com.chelz.shared.accounts.domain.entity.Category
import com.chelz.shared.accounts.domain.entity.Operation

internal fun Operation.toDto() = OperationDto(id, name, quantity, category, date, account)

internal fun OperationDto.toEntity() = Operation(id, name, quantity, category, date, account)

internal fun Account.toDto() = AccountDto(accountId, name, number, color)

internal fun AccountDto.toEntity() = Account(accountId, name, number, color)

internal fun Category.toDto() = CategoryDto(categoryId, name, isEarning, color)

internal fun CategoryDto.toEntity() = Category(categoryId, name, isEarning, color)
