package islom.din.retrofit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private val retrofit = RetrofitClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val call = retrofit.getCoffeeApi().getCoffee()
        val button = findViewById<Button>(R.id.button)

        button.setOnClickListener {
            call.enqueue(object : Callback<Coffee> {
                override fun onResponse(call: Call<Coffee>, response: Response<Coffee>) {
                    response.errorBody()
                    if(response.isSuccessful) {
                        Log.d("data_tag", "OK!")
                        // Ответ успешно пришел, он находится внутри объекта call
                        val coffee: Coffee? = response.body()
                        if(coffee != null) {
                            // данные точно есть
                            Log.d("data_tag", "${coffee.id}")
                            Log.d("data_tag", "${coffee.blendName}")
                            Log.d("data_tag", "${coffee.intensifier}")
                            Log.d("data_tag", "${coffee.notes}")
                            Log.d("data_tag", "${coffee.origin}")
                        }
                    }
                }

                override fun onFailure(call: Call<Coffee>, t: Throwable) {
                    Log.d("request_error", "onFailure: ${t.message}")
                }
            })
        }
    }
}