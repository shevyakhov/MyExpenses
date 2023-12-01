package com.chelz.shared.accounts.domain.mapper

import com.chelz.shared.accounts.domain.dto.AccountDto
import com.chelz.shared.accounts.domain.dto.AccountWithUsersDto
import com.chelz.shared.accounts.domain.dto.CategoryDto
import com.chelz.shared.accounts.domain.dto.OperationDto
import com.chelz.shared.accounts.domain.dto.UserDto
import com.chelz.shared.accounts.domain.entity.Account
import com.chelz.shared.accounts.domain.entity.AccountWithUsers
import com.chelz.shared.accounts.domain.entity.Category
import com.chelz.shared.accounts.domain.entity.Operation
import com.chelz.shared.accounts.domain.entity.User

internal fun Operation.toDto() = OperationDto(id, name, quantity, category, date, account)

internal fun OperationDto.toEntity() = Operation(id, name, quantity, category, date, account)

internal fun Account.toDto() = AccountDto(accountId, name, number, color, money)

internal fun AccountDto.toEntity() = Account(accountId, name, number, color, money)

internal fun Category.toDto() = CategoryDto(categoryId, name, isEarning, color)

internal fun CategoryDto.toEntity() = Category(categoryId, name, isEarning, color)

internal fun List<AccountWithUsersDto>.toEntity(): List<AccountWithUsers> =
	map { AccountWithUsers(it.account.toEntity(), it.users.map { user -> user.toEntity() }) }

internal fun UserDto.toEntity(): User = User(userId, name, email)

fun User.toDto(): UserDto = UserDto(userId, name, email)