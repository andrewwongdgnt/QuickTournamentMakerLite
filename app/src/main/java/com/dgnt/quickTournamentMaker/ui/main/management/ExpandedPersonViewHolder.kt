package com.dgnt.quickTournamentMaker.ui.main.management

import android.widget.Checkable
import android.widget.CheckedTextView
import com.dgnt.quickTournamentMaker.databinding.PersonItemBinding
import com.dgnt.quickTournamentMaker.model.management.Person
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder


class ExpandedPersonViewHolder(private val binding: PersonItemBinding, private val setDrawable: (CheckedTextView, Boolean) -> Unit, private val selectedPersons: Set<Person>, private val clickListener: (Checkable, Person) -> Unit) : ChildViewHolder(binding.root) {
    fun bind(person: Person, selectable: Boolean) {

        binding.checkableListItemCtv.text = person.name
        setDrawable(binding.checkableListItemCtv, selectable)

        binding.checkableListItemCtv.isChecked = if (!selectable) false else selectedPersons.contains(person)

        itemView.setOnClickListener {
            clickListener(binding.checkableListItemCtv, person)
        }

    }

}