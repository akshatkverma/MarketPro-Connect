package sih.lifetime.marketproconnect.activities

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.android.volley.DefaultRetryPolicy
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import sih.lifetime.marketproconnect.R
import sih.lifetime.marketproconnect.databinding.ActivityLoginBinding
import sih.lifetime.marketproconnect.models.User

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var volleyRequestQueue: RequestQueue
    private var password : String? = null
    private var user : User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        volleyRequestQueue = Volley.newRequestQueue(this)

        switchToSignUpPage()
        switchToSignInPage()

        binding.layoutRegisterFile.buttonRegister.setOnClickListener {
            if(checkPasswordsMatch()) {
                user = User(binding.layoutRegisterFile.registerName.text.toString(), binding.layoutRegisterFile.registerEmail.text.toString())
                password = binding.layoutRegisterFile.registerPassword.text.toString()

                it.visibility = View.GONE
                binding.layoutRegisterFile.registerProgressBar.visibility = View.VISIBLE

                sentOTPtoEmail()
            }
            else {
                binding.layoutRegisterFile.registerPassword.error = "Password does not match!"
                binding.layoutRegisterFile.registerConfirmPassword.error = "Password does not match!"
            }
        }

        binding.layoutEnterOtp.submitOtpButton.setOnClickListener {
            val inputNumber1: EditText= binding.layoutEnterOtp.box1
            val inputNumber2: EditText= binding.layoutEnterOtp.box2
            val inputNumber3: EditText= binding.layoutEnterOtp.box3
            val inputNumber4: EditText= binding.layoutEnterOtp.box4
            val inputNumber5: EditText= binding.layoutEnterOtp.box5
            val inputNumber6: EditText= binding.layoutEnterOtp.box6

            if(inputNumber1.text.toString().trim().isNotEmpty() && inputNumber2.text.toString().trim().isNotEmpty() && inputNumber3.text.toString().trim().isNotEmpty() && inputNumber4.text.toString().trim().isNotEmpty() && inputNumber5.text.toString().trim().isNotEmpty() && inputNumber6.text.toString().trim().isNotEmpty()) {
                val otpByUser = inputNumber1.text.toString() +
                        inputNumber2.text.toString() +
                        inputNumber3.text.toString() +
                        inputNumber4.text.toString() +
                        inputNumber5.text.toString() +
                        inputNumber6.text.toString()

                it.visibility = View.GONE
                binding.layoutEnterOtp.otpVerifyProgressBar.visibility = View.VISIBLE
                verifyOtpAndLogin(otpByUser)
            }
            else
                Toast.makeText(this,"Please Enter the complete OTP!",Toast.LENGTH_SHORT).show()
        }
    }

    private fun verifyOtpAndLogin(otp : String) {
        val url = getString(R.string.BASE_URL) + "api/auth/register/"
        val jsonRequestObject = JSONObject()
        jsonRequestObject.put("verify_and_register", "true")
        jsonRequestObject.put("email", user?.email)
        jsonRequestObject.put("otp", otp)
        jsonRequestObject.put("name", user?.name)
        jsonRequestObject.put("password", password)
        jsonRequestObject.put("confirm_password", password)
        val jsonObjectRequest = object : JsonObjectRequest(
            Method.POST, url,jsonRequestObject,
            {
                response->
                Toast.makeText(applicationContext, "$response", Toast.LENGTH_SHORT).show()
            },
            {
                Toast.makeText(applicationContext, "$it ${it.networkResponse.statusCode}", Toast.LENGTH_SHORT).show()
                binding.layoutEnterOtp.submitOtpButton.visibility = View.VISIBLE
                binding.layoutEnterOtp.otpVerifyProgressBar.visibility = View.GONE
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                return headers
            }
        }
        jsonObjectRequest.setShouldCache(false)
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(0, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        volleyRequestQueue.add(jsonObjectRequest)
    }

    private fun sentOTPtoEmail() {
        val url = getString(R.string.BASE_URL) + "api/auth/register/"
        val jsonRequestObject = JSONObject()
        jsonRequestObject.put("send_otp", "true")
        jsonRequestObject.put("email", user?.email)
        val jsonObjectRequest = object : JsonObjectRequest(
            Method.POST, url,jsonRequestObject,
            { response->
                Toast.makeText(this, "$response", Toast.LENGTH_SHORT).show()
                if(response.has("status") && response["status"] == true)
                    switchToVerifyOTPPage()
                else {
                    Toast.makeText(applicationContext, "Some error occurred on our side!", Toast.LENGTH_SHORT).show()
                    binding.layoutRegisterFile.buttonRegister.visibility = View.VISIBLE
                    binding.layoutRegisterFile.registerProgressBar.visibility = View.GONE
                }
            },
            {
                Toast.makeText(applicationContext, "$it", Toast.LENGTH_SHORT).show()
                binding.layoutRegisterFile.buttonRegister.visibility = View.VISIBLE
                binding.layoutRegisterFile.registerProgressBar.visibility = View.GONE
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                return headers
            }
        }
        jsonObjectRequest.setShouldCache(false)
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(0, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        volleyRequestQueue.add(jsonObjectRequest)
    }

    override fun onBackPressed() {
        if(binding.layoutRegisterFile.layoutRegisterFileRootElement.visibility == View.VISIBLE) {
            binding.layoutRegisterFile.layoutRegisterFileRootElement.visibility = View.GONE
            binding.layoutLoginFile.layoutLoginFileRootElement.visibility = View.VISIBLE
        }
        else if(binding.layoutEnterOtp.layoutEnterOtpRootElement.visibility == View.VISIBLE) {
            binding.layoutEnterOtp.layoutEnterOtpRootElement.visibility = View.GONE
            binding.layoutRegisterFile.layoutRegisterFileRootElement.visibility = View.VISIBLE
        }
        else {
            return super.onBackPressed()
        }
    }

    private fun checkPasswordsMatch() : Boolean {
        return binding.layoutRegisterFile.registerPassword.text.toString() == binding.layoutRegisterFile.registerConfirmPassword.text.toString()
    }

    private fun switchToSignUpPage() {
        binding.layoutLoginFile.signUpTV.setOnClickListener {
            binding.layoutLoginFile.layoutLoginFileRootElement.visibility = View.GONE
            binding.layoutRegisterFile.layoutRegisterFileRootElement. visibility = View.VISIBLE
        }
    }

    private fun switchToSignInPage() {
        binding.layoutRegisterFile.signInTV.setOnClickListener {
            binding.layoutLoginFile.layoutLoginFileRootElement.visibility = View.VISIBLE
            binding.layoutRegisterFile.layoutRegisterFileRootElement. visibility = View.GONE
        }
    }

    private fun switchToVerifyOTPPage() {
        numberOTPMove()
        binding.layoutRegisterFile.layoutRegisterFileRootElement.visibility = View.GONE
        binding.layoutEnterOtp.layoutEnterOtpRootElement.visibility = View.VISIBLE
        binding.layoutEnterOtp.showEmail.text = user?.email
    }

    private fun numberOTPMove() {
        val inputNumber1: EditText= binding.layoutEnterOtp.box1
        val inputNumber2: EditText= binding.layoutEnterOtp.box2
        val inputNumber3: EditText= binding.layoutEnterOtp.box3
        val inputNumber4: EditText= binding.layoutEnterOtp.box4
        val inputNumber5: EditText= binding.layoutEnterOtp.box5
        val inputNumber6: EditText= binding.layoutEnterOtp.box6

        inputNumber1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().trim().isNotEmpty())
                    inputNumber2.requestFocus()
            }
        })

        inputNumber2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s.toString().trim().isNotEmpty())
                    inputNumber3.requestFocus()
            }
        })

        inputNumber3.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s.toString().trim().isNotEmpty())
                    inputNumber4.requestFocus()
            }
        })

        inputNumber4.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s.toString().trim().isNotEmpty())
                    inputNumber5.requestFocus()
            }
        })

        inputNumber5.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s.toString().trim().isNotEmpty())
                    inputNumber6.requestFocus()
            }
        })
    }
}