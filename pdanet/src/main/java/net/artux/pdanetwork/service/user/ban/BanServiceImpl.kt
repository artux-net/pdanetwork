package net.artux.pdanetwork.service.user.ban

import lombok.RequiredArgsConstructor
import mu.KLogging
import net.artux.pdanetwork.entity.mappers.BanMapper
import net.artux.pdanetwork.entity.user.BanEntity
import net.artux.pdanetwork.models.user.ban.BanDto
import net.artux.pdanetwork.repository.user.BanRepository
import net.artux.pdanetwork.service.user.UserService
import net.artux.pdanetwork.utils.security.ModeratorAccess
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Duration
import java.util.Timer
import java.util.TimerTask
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@Service
@RequiredArgsConstructor
open class BanServiceImpl(
    private val banMapper: BanMapper,
    private val repository: BanRepository,
    private val userService: UserService,
) : BanService {

    private val timer = Timer()

    override fun getBans(userId: UUID): List<BanDto> {
        return banMapper.dto(repository.findAllByUserId(userId))
    }

    override fun isBanned(userId: UUID): Boolean {
        return currentBanMap.containsKey(userId)
    }

    override fun setChatBan(userId: UUID): Boolean {
        return userService.setChatBan(userId)
    }

    override fun applySystemBan(userId: UUID, reason: String, message: String, secs: Long): BanDto {
        val banEntity = BanEntity()
        banEntity.by = null
        return banUser(userId, reason, message, secs, banEntity)
    }

    private fun banUser(userId: UUID, reason: String, message: String, secs: Long, banEntity: BanEntity): BanDto {
        val user = userService.getCurrentUser(userId)
        banEntity.user = user
        banEntity.reason = reason
        banEntity.message = message
        banEntity.seconds = secs.toInt()
        currentBanMap[userId] = repository.save(banEntity)
        timer.schedule(
            object : TimerTask() {
                override fun run() {
                    currentBanMap.remove(userId)
                }
            },
            Duration.ofSeconds(secs).toMillis()
        )
        logger.info("User ${user.login} banned for $secs seconds, reason: $reason")
        return getCurrentBan(userId)
    }

    @ModeratorAccess
    override fun applyBan(userId: UUID, reason: String, message: String, secs: Long): BanDto {
        val user = userService.getCurrentUser()
        val banEntity = BanEntity()
        banEntity.by = user
        return banUser(userId, reason, message, secs, banEntity)
    }

    override fun getCurrentBan(userId: UUID): BanDto {
        return banMapper.dto(currentBanMap[userId])
    }

    @Transactional
    override fun clearAllBans(uuid: UUID): Boolean {
        currentBanMap.remove(uuid)
        repository.deleteAllByUserId(uuid)
        return true
    }

    @Transactional
    override fun clearBan(banId: UUID): Boolean {
        val ban = repository.findById(banId).orElseThrow { RuntimeException("Ban not found") }
        currentBanMap.remove(ban.user.id)
        repository.deleteById(banId)
        return true
    }

    override fun getCurrentBans(): Map<UUID, BanDto> {
        val bans: MutableMap<UUID, BanDto> = HashMap()
        currentBanMap.forEach { (uuid: UUID, banEntity: BanEntity?) -> bans[uuid] = banMapper.dto(banEntity) }
        return bans
    }

    companion object : KLogging() {
        private val currentBanMap = ConcurrentHashMap<UUID, BanEntity>()
    }
}
