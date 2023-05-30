package com.example.internship.person.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.internship.databinding.FragmentAddPersonBinding
import com.example.internship.person.data.Person
import com.example.internship.person.presentation.PersonViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddPersonFragment : Fragment() {
    private var _binding: FragmentAddPersonBinding? = null
    private val binding: FragmentAddPersonBinding get() = _binding!!

    private val viewModel: PersonViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddPersonBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
    }

    private fun observeData() {
        binding.btnSave.setOnClickListener {
            viewModel.addPerson(
                isNetwork = true,
                person = Person(
                    name = binding.editTextTextPersonName.editableText.toString().trim(),
                    surName = binding.editTextTextPersonSurName.editableText.toString().trim(),
                    email = binding.editTextTextEmailAddress.editableText.toString().trim(),
                    grade = "Unknown grade",
                )
            )
        }
        viewModel.showErrorInvalidData.observe(viewLifecycleOwner){
            Toast.makeText(requireContext(), "Invalid Data", Toast.LENGTH_SHORT).show()
        }
        viewModel.addPerson.observe(viewLifecycleOwner){
            findNavController().popBackStack()
            Toast.makeText(requireContext(), "Added Person", Toast.LENGTH_SHORT).show()
        }
        viewModel.showErrorInternetConnection.observe(viewLifecycleOwner){
            Toast.makeText(requireContext(), "No internet connection", Toast.LENGTH_SHORT).show()
        }
        viewModel.networkError.observe(viewLifecycleOwner){
            Toast.makeText(requireContext(), "Some think went wrong", Toast.LENGTH_SHORT).show()
        }
        viewModel.showProgress.observe(viewLifecycleOwner){
            binding.progressBar2.visibility = View.VISIBLE
        }
        viewModel.hideProgress.observe(viewLifecycleOwner){
            binding.progressBar2.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}