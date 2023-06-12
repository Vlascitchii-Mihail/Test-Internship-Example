package com.example.internship.person.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
import com.example.internship.databinding.FragmentPersonBinding
import com.example.internship.person.adapter.PersonAdapter
import com.example.internship.person.presentation.PersonViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PersonFragment : Fragment() {

    private var _binding: FragmentPersonBinding? = null
    private val binding get() = _binding!!

    private val personAdapter = PersonAdapter(
        onClick = { person ->
            findNavController().navigate(
                PersonFragmentDirections.displayPersonFragment(person)
            )
        }
    )

    private val viewModel: PersonViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        binding.personList.addItemDecoration(
            DividerItemDecoration(
                requireActivity(), VERTICAL
            )
        )
        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(
                PersonFragmentDirections.addFragment()
            )
        }
        viewModel.getFullStaff(isNetwork = true)
    }

    private fun observeData(){
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.allPerson.collectLatest {
                    binding.personList.adapter = personAdapter
                    personAdapter.submitList(it)
                }
            }
        }
        viewModel.networkError.observe(viewLifecycleOwner){
            personAdapter.submitList(emptyList())
            Toast.makeText(requireContext(), "Some think went wrong", Toast.LENGTH_SHORT).show()
        }
        viewModel.showProgress.observe(viewLifecycleOwner){
            binding.progressBar.visibility = View.VISIBLE
        }
        viewModel.hideProgress.observe(viewLifecycleOwner){
            binding.progressBar.visibility = View.GONE
        }
        viewModel.showErrorInternetConnection.observe(viewLifecycleOwner){
            Toast.makeText(requireContext(), "No internet Connection", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}