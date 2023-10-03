package sih.lifetime.marketproconnect.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import sih.lifetime.marketproconnect.R
import sih.lifetime.marketproconnect.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        switchToSignUpPage()
        switchToSignInPage()

        binding.layoutRegisterFile.buttonRegister.setOnClickListener {
            if(checkPasswordMatch()) {
                //
            }
            else {
                binding.layoutRegisterFile.registerPassword.error = "Password does not match!"
                binding.layoutRegisterFile.registerConfirmPassword.error = "Password does not match!"
            }
        }
    }

    private fun checkPasswordMatch() : Boolean {
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
}