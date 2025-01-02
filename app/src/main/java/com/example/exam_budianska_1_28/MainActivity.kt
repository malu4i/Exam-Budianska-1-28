import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.exam_budianska_1_28.databinding.ActivityMainBinding
import org.json.JSONObject
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Ініціалізація ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Завантаження даних із файлу при запуску
        loadData()?.let { data ->
            binding.inputBrand.setText(data.optString("brand"))
            binding.inputYear.setText(data.optString("year"))
            binding.inputEngine.setText(data.optString("engine"))
            binding.inputEmail.setText(data.optString("email"))
            binding.inputWebsite.setText(data.optString("website"))
        }

        // Обробник кнопки "Зберегти"
        binding.buttonSave.setOnClickListener {
            saveDataToFile()
        }

        // Обробник кнопки "Відкрити сайт"
        binding.buttonOpenWebsite.setOnClickListener {
            val websiteUrl = binding.inputWebsite.text.toString()
            if (websiteUrl.isNotEmpty() && android.util.Patterns.WEB_URL.matcher(websiteUrl).matches()) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl))
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Не вдалося відкрити браузер", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Введіть коректне посилання на сайт!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        saveDataToFile() // Автозбереження при переході у фоновий режим
    }

    private fun saveDataToFile() {
        val data = JSONObject().apply {
            put("brand", binding.inputBrand.text.toString())
            put("year", binding.inputYear.text.toString())
            put("engine", binding.inputEngine.text.toString())
            put("email", binding.inputEmail.text.toString())
            put("website", binding.inputWebsite.text.toString())
        }

        val file = File(filesDir, "data.json")
        file.writeText(data.toString())
        // Викликаємо довге Toast-повідомлення
        Toast.makeText(this, "Дані успішно збережено!", Toast.LENGTH_LONG).show()
    }

    private fun loadData(): JSONObject? {
        val file = File(filesDir, "data.json")
        return if (file.exists()) {
            val content = file.readText()
            JSONObject(content)
        } else {
            null
        }
    }
}





