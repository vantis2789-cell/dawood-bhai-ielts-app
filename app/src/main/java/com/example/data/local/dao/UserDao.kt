package com.example.data.local.dao

import androidx.room.*
import com.example.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    fun getUserById(userId: String): Flow<UserEntity?>

    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    suspend fun getUserByIdSync(userId: String): UserEntity?

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("UPDATE users SET currentBand = :band WHERE id = :userId")
    suspend fun updateCurrentBand(userId: String, band: Double)

    @Query("UPDATE users SET streakDays = streakDays + 1, lastActive = :timestamp WHERE id = :userId")
    suspend fun incrementStreak(userId: String, timestamp: Long)

    @Query("UPDATE users SET xp = xp + :gainedXp, studyHoursTotal = studyHoursTotal + :hours WHERE id = :userId")
    suspend fun addProgress(userId: String, gainedXp: Int, hours: Double)

    @Query("SELECT * FROM users ORDER BY currentBand DESC, xp DESC")
    fun getLeaderboard(): Flow<List<UserEntity>>

    @Query("SELECT * FROM users ORDER BY registrationDate DESC")
    fun getAllUsers(): Flow<List<UserEntity>>

    @Query("UPDATE users SET role = :newRole WHERE id = :userId")
    suspend fun updateUserRole(userId: String, newRole: String)

    @Query("DELETE FROM users WHERE id = :userId")
    suspend fun deleteUser(userId: String)
}
