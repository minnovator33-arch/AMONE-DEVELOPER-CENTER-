package com.example.data.database

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow

// --- entities ---

@Entity(tableName = "check_ins")
data class CheckInEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val emotion: String,
    val intensity: Int, // 1 to 10
    val somaticArea: String, // e.g. "Head", "Heart", "Solar Plexus", "Gut"
    val note: String
)

@Entity(tableName = "breath_sessions")
data class BreathSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val durationSeconds: Int,
    val startStress: Int, // 1 to 10
    val endStress: Int // 1 to 10
)

@Entity(tableName = "cycle_logs")
data class CycleLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val dateStr: String, // YYYY-MM-DD
    val phase: String, // Menstrual, Follicular, Ovulatory, Luteal
    val cycleDay: Int,
    val physicalEnergy: Int, // 1 to 10
    val emotionalEnergy: Int // 1 to 10
)

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val sender: String, // "USER" or "MODEL"
    val text: String,
    val isThinking: Boolean = false,
    val isSearch: Boolean = false,
    val isMaps: Boolean = false
)

// --- dao ---

@Dao
interface HOSDao {
    // Check-ins (Embodied Reality)
    @Query("SELECT * FROM check_ins ORDER BY timestamp DESC")
    fun getAllCheckIns(): Flow<List<CheckInEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCheckIn(checkIn: CheckInEntity)

    @Query("DELETE FROM check_ins WHERE id = :id")
    suspend fun deleteCheckIn(id: Int)

    // Breathing Sessions (Regulation System)
    @Query("SELECT * FROM breath_sessions ORDER BY timestamp DESC")
    fun getAllBreathSessions(): Flow<List<BreathSessionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBreathSession(session: BreathSessionEntity)

    // Cycle Logs (HerAURA)
    @Query("SELECT * FROM cycle_logs ORDER BY dateStr DESC")
    fun getAllCycleLogs(): Flow<List<CycleLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCycleLog(log: CycleLogEntity)

    // Chat History (AMONE Guide AI)
    @Query("SELECT * FROM chat_messages ORDER BY timestamp ASC")
    fun getAllChatMessages(): Flow<List<ChatMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChatMessage(message: ChatMessageEntity)

    @Query("DELETE FROM chat_messages")
    suspend fun clearChatHistory()
}

// --- database ---

@Database(
    entities = [
        CheckInEntity::class,
        BreathSessionEntity::class,
        CycleLogEntity::class,
        ChatMessageEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class HOSDatabase : RoomDatabase() {
    abstract fun hosDao(): HOSDao

    companion object {
        @Volatile
        private var INSTANCE: HOSDatabase? = null

        fun getDatabase(context: Context): HOSDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HOSDatabase::class.java,
                    "amone_hos_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

// --- repository ---

class HOSRepository(private val dao: HOSDao) {
    val checkIns: Flow<List<CheckInEntity>> = dao.getAllCheckIns()
    val breathSessions: Flow<List<BreathSessionEntity>> = dao.getAllBreathSessions()
    val cycleLogs: Flow<List<CycleLogEntity>> = dao.getAllCycleLogs()
    val chatMessages: Flow<List<ChatMessageEntity>> = dao.getAllChatMessages()

    suspend fun insertCheckIn(checkIn: CheckInEntity) = dao.insertCheckIn(checkIn)
    suspend fun deleteCheckIn(id: Int) = dao.deleteCheckIn(id)

    suspend fun insertBreathSession(session: BreathSessionEntity) = dao.insertBreathSession(session)

    suspend fun insertCycleLog(log: CycleLogEntity) = dao.insertCycleLog(log)

    suspend fun insertChatMessage(message: ChatMessageEntity) = dao.insertChatMessage(message)
    suspend fun clearChatHistory() = dao.clearChatHistory()
}
