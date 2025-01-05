package net.artux.pdanetwork.service.user

import net.artux.pdanetwork.entity.user.UserEntity
import net.artux.pdanetwork.models.Status
import net.artux.pdanetwork.models.user.dto.AdminEditUserDto
import net.artux.pdanetwork.models.user.dto.AdminUserDto
import net.artux.pdanetwork.models.user.dto.RegisterUserDto
import net.artux.pdanetwork.models.user.dto.UserDto
import java.io.ByteArrayInputStream
import java.io.IOException
import java.util.UUID

@Suppress("TooManyFunctions")
interface UserService {
    fun registerUser(registerUser: RegisterUserDto): Status

    fun handleConfirmation(token: String): Status

    fun getCurrentUser(): UserEntity

    fun getUserDto(): UserDto
    fun getCurrentUser(objectId: UUID): UserEntity
    fun getUserForAdminById(objectId: UUID): AdminUserDto
    fun getCurrentId(): UUID
    fun getUserByEmail(email: String): UserEntity
    fun getUserByLogin(login: String): UserEntity
    fun updateUser(userId: UUID, adminEditUserDto: AdminEditUserDto): AdminUserDto
    fun setChatBan(userId: UUID): Boolean
    fun editUser(user: RegisterUserDto): Status
    fun deleteUserById(id: UUID)

    @Throws(IOException::class)
    fun exportMailContacts(): ByteArrayInputStream
    fun changeEmailSetting(id: UUID): Boolean
    fun getUsersByIds(ids: Collection<UUID>): List<UserEntity>
    fun getUsersByLogins(logins: Collection<String>): List<UserEntity>
}
