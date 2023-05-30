package com.example.internship.person.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.internship.databinding.ItemPersonBinding
import com.example.internship.person.data.Person

class PersonAdapter(
    private val onClick: (Person) -> Unit,
) : ListAdapter<Person, PersonViewHolder>(PersonDiff()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PersonViewHolder(
        ItemPersonBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
        onClick
    )

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}