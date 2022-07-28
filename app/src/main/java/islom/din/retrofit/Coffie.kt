package islom.din.retrofit

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

//1) Создали модель!
data class Coffee(
    val id: Int,
    val uid: String,
    @SerializedName("blend_name")
    val blendName: String,
    val origin: String,
    val variety: String,
    val notes: String,
    val intensifier: String
)

//2) Объявить интерфейс
interface CoffeeApi {
    @GET("coffee/random_coffee")
    fun getCoffee() : Call<Coffee>
}

//3) Сам ретрофит
class RetrofitClient {
    val retrofit = Retrofit
        .Builder()
        .baseUrl("https://random-data-api.com/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getCoffeeApi(): CoffeeApi {
        return retrofit.create(CoffeeApi::class.java)
    }
}


