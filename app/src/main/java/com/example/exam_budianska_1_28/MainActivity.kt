import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.io.File

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val inputBrand = findViewById<EditText>(R.id.input_brand)
        val inputYear = findViewById<EditText>(R.id.input_year)
        val inputEngine = findViewById<EditText>(R.id.input_engine)
        val inputEmail = findViewById<EditText>(R.id.input_email)
        val inputWebsite = findViewById<EditText>(R.id.input_website)
        val saveButton = findViewById<Button>(R.id.button_save)

        // Завантаження даних із файлу при запуску
        loadData()?.let { data ->
            inputBrand.setText(data.optString("brand"))
            inputYear.setText(data.optString("year"))
            inputEngine.setText(data.optString("engine"))
            inputEmail.setText(data.optString("email"))
            inputWebsite.setText(data.optString("website"))
        }

        // Збереження даних у файл
        saveButton.setOnClickListener {
            val data = JSONObject().apply {
                put("brand", inputBrand.text.toString())
                put("year", inputYear.text.toString())
                put("engine", inputEngine.text.toString())
                put("email", inputEmail.text.toString())
                put("website", inputWebsite.text.toString())
            }
            saveDataToFile(data)
        }
    }

    private fun saveDataToFile(data: JSONObject) {
        val fileName = "data.json"
        val file = File(filesDir, fileName)
        file.writeText(data.toString())
        Toast.makeText(this, "Дані збережено", Toast.LENGTH_SHORT).show()
    }

    private fun loadData(): JSONObject? {
        val fileName = "data.json"
        val file = File(filesDir, fileName)
        return if (file.exists()) {
            val content = file.readText()
            JSONObject(content)
        } else {
            null
        }
    }
}


