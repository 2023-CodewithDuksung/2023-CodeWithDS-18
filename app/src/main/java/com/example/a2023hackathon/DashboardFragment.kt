package com.example.a2023hackathon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.a2023hackathon.databinding.FragmentDashboardBinding
import com.example.a2023hackathon.databinding.FragmentNotificationsBinding

class DashboardFragment : Fragment() {

    lateinit var binding: FragmentDashboardBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)

        return binding.root
    }
}