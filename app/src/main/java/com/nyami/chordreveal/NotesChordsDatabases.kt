package com.nyami.chordreveal
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Database
import androidx.room.Delete
import androidx.room.RoomDatabase

// tables
@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val note: String,
    val string: String,
    val fret: Int,
    val key: String,
    val intervalFromRoot: Int,
    val stepToNextNoteInKey: Float,
    val stepToPrevNoteInKey: Float
)

@Entity(tableName = "chords")
data class Chord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val notes: List<Note>,          // ordered lowest to highest note.
    val chord: String,
    val chordMajorOrMinor: String,  // Either a "major" or a "minor" chord
    val chordType: String,          // "dyad", "triad", "tetrad"
    val dissonance: String,
    val notesInChord: String,       // ordered lowest to highest note.
    val key: String,
    val rootNoteIntervalFromKey: String
)

// Direct Access Objects
@Dao
interface ChordRevealerDao {
    // Inserts
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(vararg note: Note)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChord(vararg chord: Chord)

    // Queries
    @Query("SELECT * FROM notes")
    suspend fun getAllNotes(): List<Note>
    @Query("SELECT * FROM chords")
    suspend fun getAllChords(): List<Chord>

    // Deletions
    @Delete
    suspend fun deleteNote(vararg note: Note)
    @Delete
    suspend fun deleteChord(vararg chord: Chord)
}

// database
@Database(entities = [Note::class, Chord::class], version = 1)
abstract class ChordRevealDatabase : RoomDatabase() {
    abstract fun chordRevealerDao(): ChordRevealerDao
}