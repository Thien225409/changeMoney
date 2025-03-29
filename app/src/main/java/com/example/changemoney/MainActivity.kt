package com.example.changemoney

import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private val currencyList = listOf(
        Triple("United States - Dollar", R.drawable.dollar,25575.00),
        Triple("United Kingdom - Pound", R.drawable.pound,33093.30),
        Triple("Russia - Ruble", R.drawable.rupee,301.07 ),
        Triple("European Union - Euro", R.drawable.euro,27549.00),
        Triple("Vietnam - Dong", R.drawable.vnd,1.00)
    )
    private val dataList = currencyList.map {it.first}
    private val currencyCodes = listOf("USD","GBP","RUB","EUR","VND")
    val imageList = currencyList.map {it.second}
    val rateList = currencyList.map {it.third}
    lateinit var rate : TextView
    lateinit var spiner_input : Spinner
    lateinit var spiner_output : Spinner
    lateinit var btn_change : Button
    lateinit var input_money : EditText
    lateinit var output_money : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        spiner_input = findViewById(R.id.spinner)
        spiner_output = findViewById(R.id.spinner3)
        btn_change = findViewById(R.id.button)
        input_money = findViewById(R.id.editText)
        output_money = findViewById(R.id.editText2)
        val image_input : ImageView = findViewById(R.id.imageView2)
        val image_output : ImageView = findViewById(R.id.imageView)
        rate = findViewById(R.id.textView)

        val arrAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, dataList)
        arrAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        btn_change.setOnClickListener{ convertMoney() }

        // Gọi phương thức setupSpinner để thiết lập spinner
        setupSpinner(spiner_input, image_input, arrAdapter, 0)
        setupSpinner(spiner_output, image_output, arrAdapter, 4)
    }

    private fun convertMoney() {
        val amountStr = input_money.text.toString().trim()
        if (amountStr.isEmpty()) {
            output_money.text = "0"
            Toast.makeText(this, "Vui lòng nhập số tiền cần đổi", Toast.LENGTH_SHORT).show()
            return
        }
        val amount = amountStr.toDoubleOrNull()
        if (amount == null || amount < 0) {
            output_money.text = "0"
            Toast.makeText(this, "Vui lòng nhập số tiền lớn hơn 0", Toast.LENGTH_SHORT).show()
            return
        }
        val exRate = updateExchangeRate()
        val output = input_money.text.toString().toDouble() * exRate
        output_money.text = "%.2f".format(output)
    }

    private fun setupSpinner(spinner: Spinner, image: ImageView, arrAdapter: ArrayAdapter<String>, defaultSelection: Int) {
        spinner.adapter = arrAdapter
        spinner.setSelection(defaultSelection)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                (view as? TextView)?.apply {
                    setTypeface(null, Typeface.BOLD)
                    textSize = 20F
                }
                image.setImageResource(imageList[position])
                updateExchangeRate()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

    }
    private fun updateExchangeRate() : Double{
        val inputPosition = spiner_input.selectedItemPosition
        val outputPosition = spiner_output.selectedItemPosition

        val inputRate = rateList[inputPosition]
        val outputRate = rateList[outputPosition]

        // Tính tỷ giá
        val exchangeRate = inputRate / outputRate

        val inputCode = currencyCodes[inputPosition]
        val outputCode = currencyCodes[outputPosition]

        val formattedRate = if (exchangeRate == 1.0) "1" else "%.2f".format(exchangeRate)

        rate.text = "1 $inputCode = $formattedRate $outputCode"
        return exchangeRate;
    }

}