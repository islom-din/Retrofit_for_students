package islom.din.retrofit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.lang.Exception
import java.net.ConnectException
import java.net.SocketTimeoutException

class MainViewModel : ViewModel() {

    private val retrofit = RetrofitClient()

    // coffee
    private val _coffeeLiveData = MutableLiveData<Coffee?>(null)
    val coffeeLiveData: LiveData<Coffee?> = _coffeeLiveData

    // progress
    private val _progressLiveData = MutableLiveData<Boolean>(false)
    val progressLiveData: LiveData<Boolean> = _progressLiveData

    // error
    private val _errorLiveData = MutableLiveData<String?>(null)
    val errorLiveData: LiveData<String?> = _errorLiveData

    fun getCoffee() {
        viewModelScope.launch {
            requestCoffee()
        }
    }

    private suspend fun requestCoffee() = withContext(Dispatchers.IO) {
        _progressLiveData.postValue(true)
        try {
            val response: Response<Coffee> = retrofit.getCoffeeApi().getCoffee2()
            if (response.isSuccessful) {
                val coffee = response.body()
                _coffeeLiveData.postValue(coffee)
                _errorLiveData.postValue(null)
            } else {
                _coffeeLiveData.postValue(null)
                _errorLiveData.postValue("Что-то не так...")
            }
        } catch (e: ConnectException) {
            _coffeeLiveData.postValue(null)
            _errorLiveData.postValue("Нет сети!")
        } catch (e: SocketTimeoutException) {
            _coffeeLiveData.postValue(null)
            _errorLiveData.postValue("Превышено время ожидания!")
        } catch (e: Exception) {
            _coffeeLiveData.postValue(null)
            _errorLiveData.postValue("Что-то не так!")
        }
        _progressLiveData.postValue(false)
    }
}