package net.artux.pdanetwork.service.user.reset

import net.artux.pdanetwork.models.Status
import net.artux.pdanetwork.models.user.dto.StoryData

interface ResetService {
    fun sendResetPasswordLetter(email: String): Status
    fun changePassword(token: String, password: String): Status
    fun resetData(): StoryData
    fun getTokens(): Collection<String>
    fun deleteAccount(): Boolean
}
