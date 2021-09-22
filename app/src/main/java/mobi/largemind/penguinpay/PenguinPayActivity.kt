package mobi.largemind.penguinpay

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import mobi.largemind.penguinpay.databinding.ActivityPenguinPayBinding
import mobi.largemind.penguinpay.model.countries
import mobi.largemind.penguinpay.viewmodel.PengiunPayViewModel

@AndroidEntryPoint
class PenguinPayActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPenguinPayBinding
    private val viewModel: PengiunPayViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPenguinPayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.countrySpinner.adapter = CountriesAdapter(this, countries)
    }
}