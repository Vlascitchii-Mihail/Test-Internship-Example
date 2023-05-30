package com.example.internship.person.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.internship.person.data.Person

class PersonDiff: DiffUtil.ItemCallback<Person>() {
    override fun areItemsTheSame(oldItem: Person, newItem: Person): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Person, newItem: Person): Boolean {
        return oldItem == newItem
    }
}