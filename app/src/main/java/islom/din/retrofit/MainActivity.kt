package islom.din.retrofit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import islom.din.retrofit.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.net.ConnectException
import java.net.SocketTimeoutException

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        setupListeners()
        observeData()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setupListeners() {
        binding.button.setOnClickListener {
            viewModel.getCoffee()
        }
    }

    private fun observeData() {
        viewModel.coffeeLiveData.observe(this) { coffee ->
            coffee?.let { binding.button.text = coffee.blendName }
        }

        viewModel.progressLiveData.observe(this) { inProgress ->
//            if(inProgress) {
//                binding.button.isVisible = false
//                binding.progress.isVisible = true
//            } else {
//                binding.button.isVisible = true
//                binding.progress.isVisible = false
//            }
            binding.apply {
                button.isVisible = !inProgress
                progress.isVisible = inProgress
            }
        }

        viewModel.errorLiveData.observe(this) { errorMessage ->
            errorMessage?.let { message ->
                binding.button.text = message
            }
        }
    }

    private fun showMessage(message: String) {
        lifecycleScope.launch(Dispatchers.Main) {
            Toast.makeText(this@MainActivity, message, Toast.LENGTH_LONG).show()
        }
    }
}