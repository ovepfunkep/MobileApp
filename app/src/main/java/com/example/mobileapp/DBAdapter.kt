import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.mobileapp.Line


class DBHelper(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE $TABLE_NAME ($KEY_ID INTEGER PRIMARY KEY AUTOINCREMENT, $KEY_NAME TEXT NOT NULL, " +
                "$KEY_SURNAME TEXT NOT NULL, $KEY_DATEOFBIRTH TEXT NOT NULL, $KEY_PHONENUMBER TEXT NOT NULL)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun getLines(): List<Line> {
        val result = mutableListOf<Line>()
        val database = this.writableDatabase
        val cursor: Cursor = database.query(
            TABLE_NAME, null, null, null,
            null, null, null
        )
        if (cursor.moveToFirst()) {
            val idIndex: Int = cursor.getColumnIndex(KEY_ID)
            val nameIndex: Int = cursor.getColumnIndex(KEY_NAME)
            val surnameIndex: Int = cursor.getColumnIndex(KEY_SURNAME)
            val dateOfBirthIndex: Int = cursor.getColumnIndex(KEY_DATEOFBIRTH)
            val phoneNumberIndex: Int = cursor.getColumnIndex(KEY_PHONENUMBER)
            do {
                val line = Line(
                    cursor.getLong(idIndex),
                    cursor.getString(nameIndex),
                    cursor.getString(surnameIndex),
                    cursor.getString(dateOfBirthIndex),
                    cursor.getString(phoneNumberIndex)
                )
                result.add(line)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return result
    }

    fun addLine(name: String, surname: String, dateOfBirth: String, phoneNumber: String): Long {
        val database = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_NAME, name)
        contentValues.put(KEY_SURNAME, surname)
        contentValues.put(KEY_DATEOFBIRTH, dateOfBirth)
        contentValues.put(KEY_PHONENUMBER, phoneNumber)
        val id = database.insert(TABLE_NAME, null, contentValues)
        close()
        return id;
    }

    fun updateLine(id: Int, name: String, surname: String, dateOfBirth: String, phoneNumber: String) {
        val database = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_NAME, name)
        contentValues.put(KEY_SURNAME, surname)
        contentValues.put(KEY_DATEOFBIRTH, dateOfBirth)
        contentValues.put(KEY_PHONENUMBER, phoneNumber)
        database.update(TABLE_NAME, contentValues, "$KEY_ID = ?", arrayOf(id.toString()))
        close()
    }

    fun removeLine(id: Int) {
        val database = this.writableDatabase
        database.delete(TABLE_NAME, "$KEY_ID = ?", arrayOf(id.toString()))
        close()
    }

    fun removeAllLines() {
        val database = this.writableDatabase
        database.delete(TABLE_NAME, null, null)
        close()
    }

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "linesDb"
        const val TABLE_NAME = "lines"
        const val KEY_ID = "id"
        const val KEY_NAME = "name"
        const val KEY_SURNAME = "surname"
        const val KEY_DATEOFBIRTH = "dateOfBirth"
        const val KEY_PHONENUMBER = "phoneNumber"
    }
}