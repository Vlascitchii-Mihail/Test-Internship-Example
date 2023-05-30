package com.example.internship.person.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.internship.databinding.FragmentDisplayPersonBinding

class DisplayPersonFragment : Fragment() {
    private var _binding: FragmentDisplayPersonBinding? = null
    private val binding get() = _binding!!
    private val args: DisplayPersonFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDisplayPersonBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.txtName.text = args.model.name
        binding.txtSurname.text = args.model.surName
        binding.txtGrade.text = args.model.grade
        binding.txtEmail.text = args.model.email
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}