package com.dgnt.quickTournamentMaker.ui.main.management

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Checkable

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.data.QTMDatabase
import com.dgnt.quickTournamentMaker.data.management.GroupRepository
import com.dgnt.quickTournamentMaker.data.management.PersonRepository
import com.dgnt.quickTournamentMaker.databinding.ManagementFragmentBinding
import com.dgnt.quickTournamentMaker.model.management.Person
import kotlinx.android.synthetic.main.management_fragment.*

class ManagementFragment : Fragment() {
    companion object {
        fun newInstance() = ManagementFragment()
    }

    private val selectedPersons = mutableSetOf<String>()
    private lateinit var actionModeCallback: ManagementFragmentActionModeCallBack
    private lateinit var binding: ManagementFragmentBinding
    private lateinit var viewModel: ManagementViewModel
    private lateinit var personToGroupNameMap: Map<String, String>
    private lateinit var groupNames: List<String>;
    private var actionMode: ActionMode? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.management_fragment, container, false)
        return binding.root;
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.actions_management, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_editMode -> {
                actionMode = (activity as AppCompatActivity).startSupportActionMode(actionModeCallback)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (context == null) {
            return
        }

        add_fab.setOnClickListener { add() }

        actionModeCallback = ManagementFragmentActionModeCallBack(binding, selectedPersons)

        setHasOptionsMenu(true)
        val db = QTMDatabase.getInstance(context!!)
        val personRepository = PersonRepository.getInstance(db.personDAO)
        val groupRepository = GroupRepository.getInstance(db.groupDAO)
        val factory = ManagementViewModelFactory(personRepository, groupRepository)
        viewModel = ViewModelProvider(this, factory).get(ManagementViewModel::class.java)
        binding.vm = viewModel
        binding.lifecycleOwner = this

        viewModel.navigateToPersonDetails.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { triple ->// Only proceed if the event has never been handled
                val person = triple.first
                val groupName = triple.second
                val editing = triple.third

                PersonEditorDialogFragment.newInstance(editing, if (editing) getString(R.string.editing, person.name) else getString(R.string.adding), person, groupName, groupNames).show(activity?.supportFragmentManager!!, PersonEditorDialogFragment.TAG)

            }
        })

        binding.personRv.layoutManager = LinearLayoutManager(context)

        viewModel.personAndGroupLiveData.observe(viewLifecycleOwner, Observer { (persons, groups) ->

            Log.d("DGNTTAG", "person: $persons")
            Log.d("DGNTTAG", "group: $groups")

            groupNames = groups.map { it.name }

            personToGroupNameMap = persons.map { it.name to it.groupName }.toMap()
            val extraGroupExpandableGroupMap = groupNames.subtract(persons.map { it.groupName }.toSet()).map { GroupExpandableGroup(it, listOf()) }
            val groupExpandableGroupMap = persons.groupBy { it.groupName }.map { it.key to it.value.map { Person.fromEntity(it) } }.map { GroupExpandableGroup(it.first, it.second.sorted()) }

            val adapter = GroupExpandableRecyclerViewAdapter(actionModeCallback, selectedPersons, (groupExpandableGroupMap + extraGroupExpandableGroupMap).sorted()) { checkable: Checkable, person: Person -> personClicked(checkable, person) }
            binding.personRv.adapter = adapter

            add_fab.visibility = View.VISIBLE
        })

    }

    private fun add() {
        AddChoiceDialogFragment.newInstance(groupNames).show(activity?.supportFragmentManager!!, AddChoiceDialogFragment.TAG)

    }

    private fun personClicked(checkable: Checkable, person: Person) {

        if (actionModeCallback.multiSelect) {
            val isChecked = !checkable.isChecked
            if (isChecked)
                selectedPersons.add(person.name)
            else
                selectedPersons.remove(person.name)
            checkable.isChecked = isChecked

            val menu = actionMode?.menu
            menu?.findItem(R.id.action_delete)?.isVisible = selectedPersons.size > 0
            menu?.findItem(R.id.action_move)?.isVisible = selectedPersons.size > 0

            actionMode?.title = selectedPersons.size.toString()
        } else
            viewModel.editPerson(person, personToGroupNameMap[person.name] ?: "")
    }


}

class ManagementFragmentActionModeCallBack(private val binding: ManagementFragmentBinding, private val selectedPersons: MutableSet<String>) : ActionMode.Callback {
    var multiSelect = false

    override fun onCreateActionMode(actionMode: ActionMode, menu: Menu): Boolean {
        multiSelect = true
        binding.addFab.visibility = View.INVISIBLE
        actionMode.title = selectedPersons.size.toString()
        actionMode.menuInflater.inflate(R.menu.actions_management_contextual, menu)
        menu.findItem(R.id.action_delete)?.isVisible = selectedPersons.size > 0
        menu.findItem(R.id.action_move)?.isVisible = selectedPersons.size > 0
        binding.personRv.adapter?.notifyDataSetChanged()
        return true;
    }

    override fun onPrepareActionMode(actionMode: ActionMode, menu: Menu): Boolean {
        return false
    }

    override fun onActionItemClicked(actionMode: ActionMode, menuItem: MenuItem): Boolean {
        reset()
        actionMode.finish()
        return true
    }

    override fun onDestroyActionMode(actionMode: ActionMode) {
        reset()
    }

    private fun reset() {
        multiSelect = false
        binding.addFab.visibility = View.VISIBLE
        selectedPersons.clear()
        binding.personRv.adapter?.notifyDataSetChanged()
    }

}