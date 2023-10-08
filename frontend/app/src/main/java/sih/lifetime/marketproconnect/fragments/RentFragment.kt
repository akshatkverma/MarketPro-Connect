package sih.lifetime.marketproconnect.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import sih.lifetime.marketproconnect.R
import sih.lifetime.marketproconnect.activities.LoginActivity
import sih.lifetime.marketproconnect.databinding.FragmentRentBinding

class RentFragment : Fragment() {

    private var _binding: FragmentRentBinding?=null
    private val binding get()=_binding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentRentBinding.inflate(inflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding?.logoutButton?.setOnClickListener {
            val sharedPref = activity?.getSharedPreferences("activities.LoginActivity",Context.MODE_PRIVATE)
            sharedPref?.edit()?.clear()?.apply()
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
        }
    }


}