package com.example.internship.person.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.internship.databinding.ItemPersonBinding
import com.example.internship.person.data.Person

class PersonViewHolder(
    private val binding: ItemPersonBinding,
    private val onClick: (Person) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(model: Person) {
        binding.root.setOnClickListener { onClick.invoke(model) }
        binding.apply {
            txtName.text = model.surName
            txtSurname.text = model.name
        }
    }
}