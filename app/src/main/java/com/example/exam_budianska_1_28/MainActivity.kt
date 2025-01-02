package com.example.exam_budianska_1_28

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.exam_budianska_1_28.databinding.ActivityMainBinding
import org.json.JSONObject
import java.io.File
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // Запуск галереї для вибору фото
    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            binding.photoView.setImageURI(uri)
        } else {
            Toast.makeText(this, "Фото не було вибрано", Toast.LENGTH_SHORT).show()
        }
    }

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

        // Обробка кліку кнопки "Зберегти"
        binding.buttonSave.setOnClickListener {
            saveDataToFile()
        }

        // Обробка кліку кнопки "Завантажити фото"
        binding.buttonUploadPhoto.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        // Обробка кліку кнопки для пошуку сайту
        binding.buttonSearchAutoRia.setOnClickListener {
            val website = binding.inputWebsite.text.toString()
            if (website.isNotBlank()) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(website))
                startActivity(intent)
            } else {
                Toast.makeText(this, "Будь ласка, введіть посилання на сайт", Toast.LENGTH_SHORT).show()
            }
        }

        // Налаштування вибору дати
        binding.inputYear.setOnClickListener {
            showDatePickerDialog()
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

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, _, _ ->
            binding.inputYear.setText("$selectedYear")
        }, year, month, day)

        datePickerDialog.show()
    }
}

