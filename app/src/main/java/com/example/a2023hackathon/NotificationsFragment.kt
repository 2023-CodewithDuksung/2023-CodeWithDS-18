package com.example.a2023hackathon

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.a2023hackathon.databinding.FragmentNotificationsBinding

class NotificationsFragment : Fragment() {

    lateinit var binding: FragmentNotificationsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false)

        binding.btnMove.setOnClickListener{
            var bundle : Bundle = Bundle()
            bundle.putString("fromFrag", "수강과목프래그먼트")
            val transaction: FragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            val mylecturefragment: Fragment = MyLectureFragment()
            mylecturefragment.arguments = bundle
            transaction.replace(R.id.main_layout, mylecturefragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        if(MyApplication.checkAuth()){
            binding.CertifyEmailView.text = "${MyApplication.email}"
        }

        binding.logoutButton.setOnClickListener {
            logout()
        }

        binding.completeTask.setOnClickListener{
            var bundle : Bundle = Bundle()
            bundle.putString("fromFrag", "수강과목프래그먼트")
            val transaction: FragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            val mytasksfragment: Fragment = MyTasksFragment()
            mytasksfragment.arguments = bundle
            transaction.replace(R.id.main_layout, mytasksfragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        binding.makers.setOnClickListener {
            val intent = Intent(requireContext(), IntroActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    private fun logout() {
        val intent = Intent(requireContext(), AuthActivity::class.java)
        startActivity(intent)
    }

}