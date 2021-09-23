package mobi.largemind.penguinpay

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import mobi.largemind.penguinpay.databinding.ActivityPenguinPayBinding
import mobi.largemind.penguinpay.model.countries
import mobi.largemind.penguinpay.viewmodel.Field
import mobi.largemind.penguinpay.viewmodel.PenguinPayViewModel
import mobi.largemind.penguinpay.viewmodel.SendUiState
import mobi.largemind.penguinpay.viewmodel.UiState

@AndroidEntryPoint
class PenguinPayActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPenguinPayBinding
    private val viewModel: PenguinPayViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPenguinPayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUi()
        observeEvents()
    }

    private fun setupUi() {
        binding.countrySpinner.adapter = CountriesAdapter(this, countries)

        binding.sendInput.doOnTextChanged { text, _, _, _ ->
            viewModel.sendAmount.value = text.toString()
        }

        binding.countrySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val adapter = binding.countrySpinner.adapter as CountriesAdapter
                adapter.getItem(position)?.let { country ->
                    viewModel.setCountry(country)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        binding.countrySpinner.setSelection(0)

        binding.send.setOnClickListener {
            viewModel.sendMoney(
                binding.phoneInput.text.toString(),
                binding.firstNameInput.text.toString(),
                binding.lastNameInput.text.toString(),
                binding.sendInput.text.toString()
            )
        }

        binding.sendInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                viewModel.sendMoney(
                    binding.phoneInput.text.toString(),
                    binding.firstNameInput.text.toString(),
                    binding.lastNameInput.text.toString(),
                    binding.sendInput.text.toString()
                )
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    private fun observeEvents() {
        viewModel.receiveAmount.observe(this) { amount ->
            binding.receiveInput.setText(amount)
        }

        viewModel.exchangeRate.observe(this) { rate ->
            if (rate.isNotEmpty()) {
                val text = getString(R.string.exchange_rate, rate)
                binding.exchangeRate.text = text
            }
        }

        lifecycleScope.launch {
            viewModel.uiState.collect { handleUiState(it) }
        }

        lifecycleScope.launch {
            viewModel.sendState.collect { handleSendState(it) }
        }
    }

    private fun handleSendState(state: SendUiState) {
        when (state) {
            SendUiState.Sending -> {
                binding.progress.isVisible = true
                binding.exchangeRate.isInvisible = true
                binding.send.isEnabled = false
            }
            SendUiState.Sent -> {
                binding.progress.isVisible = false
                binding.exchangeRate.isInvisible = false
                binding.send.isEnabled = true
                clearErrorStates(errorEnabled = false)
                Toast.makeText(this, "Money Sent!", Toast.LENGTH_LONG).show()
            }
            is SendUiState.Error -> {
                clearErrorStates()
               showFieldErrors(state.fields)
            }
            SendUiState.None -> {
                // NoOp
            }
        }
    }

    private fun clearErrorStates(errorEnabled: Boolean = true) {
        listOf(
            binding.phoneInputLayout,
            binding.phoneInputLayout,
            binding.firstNameInputLayout,
            binding.lastNameInputLayout,
            binding.sendInputLayout
        ).forEach {
            it.error = ""
            it.isErrorEnabled = errorEnabled
        }
    }

    private fun showFieldErrors(fields: List<Field>) {
        fields.forEach { field ->
            when (field) {
                Field.PhoneNumber -> showError(binding.phoneInputLayout)
                Field.InvalidPhoneNumber -> showError(binding.phoneInputLayout, "Invalid phone number")
                Field.FirstName -> showError(binding.firstNameInputLayout)
                Field.LastName -> showError(binding.lastNameInputLayout)
                Field.Amount -> showError(binding.sendInputLayout)
            }
        }
    }

    private fun showError(layout: TextInputLayout, errorMsg: String = "Missing field") {
        layout.error = errorMsg
    }

    private fun handleUiState(uiState: UiState) {
        when (uiState) {
            UiState.Loading -> {
                configureUi(loading = true)
            }
            UiState.Error -> {
                showFetchCurrencyError()
            }
            UiState.Loaded -> {
                configureUi(loading = false)
            }
        }
    }

    private fun showFetchCurrencyError() {
        binding.progress.isVisible = false
        binding.exchangeRate.isInvisible = false

        binding.exchangeRate.text = getString(R.string.error_loading)
        binding.exchangeRate.setOnClickListener {
            viewModel.loadCurrencies()
        }
    }

    private fun configureUi(loading: Boolean) {
        binding.countrySpinner.isEnabled = !loading
        binding.phoneInput.isEnabled = !loading
        binding.firstNameInput.isEnabled = !loading
        binding.lastNameInput.isEnabled = !loading
        binding.sendInput.isEnabled = !loading
        binding.send.isEnabled = !loading

        binding.progress.isVisible = loading
        binding.exchangeRate.isInvisible = loading

        binding.exchangeRate.setOnClickListener(null)
    }
}