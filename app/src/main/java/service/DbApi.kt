package service

import android.content.Context
import androidx.room.*
import androidx.room.OnConflictStrategy.IGNORE
import entity.Location
import entity.Pet
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import service.DbApi.Db.DbPet
import java.util.*

internal object DbApi : PetService {

    override fun pets() = db.petDao().pets().map(DbPet::toPet)!!

    override fun pet(id: Long) = db.petDao().pet(id).map(DbPet::toPet)

    override fun save(pet: Pet) = when (pet.isNew()) {
        true -> db.petDao().save(pet)
        else -> db.petDao().update(pet)
    }.map(DbPet::toPet)

    fun saveAll(vararg pets: Pet) = Unit

    lateinit var context: Context
    private val db: Db by lazy {
        Room.databaseBuilder(context, Db::class.java, "pets.db").build()
    }

    @Database(entities = [DbPet::class], version = 1)
    internal abstract class Db : RoomDatabase() {

        abstract fun petDao(): PetDao

        @Dao
        interface PetDao {

            @Query("select * from Pets")
            fun pets(): Observable<DbPet>

            @Query("select * from Pets where id = :id")
            fun pet(id: Long): Maybe<DbPet>

            @Insert(onConflict = IGNORE)
            fun save(pet: Pet): Single<DbPet>

            @Insert(onConflict = IGNORE)
            fun save(vararg pets: Pet): Single<Int>

            @Update(onConflict = IGNORE)
            fun update(pet: Pet): Single<DbPet>

            @Update(onConflict = IGNORE)
            fun update(vararg pets: Pet): Single<Int>

        }

        @Entity(tableName = "pets")
        internal data class DbPet(
            @PrimaryKey val id: Long,
            val name: String,
            val description: String,
            val imageUrl: String,
            val found: Boolean,
            val locationX: Double,
            val locationY: Double,
            val locationZ: Double,
            val locationDate: Date
        ) {
            fun toPet() = Pet(
                id,
                name,
                description,
                imageUrl,
                found,
                Location(locationDate, locationX, locationY, locationZ)
            )
        }
    }
}