package com.dgnt.quickTournamentMaker.ui.main.management

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.data.QTMDatabase
import com.dgnt.quickTournamentMaker.data.management.GroupRepository
import com.dgnt.quickTournamentMaker.data.management.PersonRepository
import com.dgnt.quickTournamentMaker.databinding.ManagementFragmentBinding
import com.dgnt.quickTournamentMaker.model.management.Person

class ManagementFragment : Fragment() {
    companion object {
        fun newInstance() = ManagementFragment()
    }

    private lateinit var binding: ManagementFragmentBinding
    private lateinit var viewModel: ManagementViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.management_fragment, container, false)
        return binding.root;
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (context==null){
            return
        }
        val db = QTMDatabase.getInstance(context!!)
        val personDAO = db.personDAO
        val personRepository = PersonRepository(personDAO)
        val groupDAO = db.groupDAO
        val groupRepository = GroupRepository(groupDAO)
        val factory = ManagementViewModelFactory(personRepository, groupRepository)
        viewModel = ViewModelProvider(this, factory).get(ManagementViewModel::class.java)
        binding.vm =viewModel
        binding.lifecycleOwner = this

        viewModel.navigateToPersonDetails.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { v ->// Only proceed if the event has never been handled
                //TODO
                val editing = false

                PersonEditorDialogFragment.newInstance(editing, if (editing) getString(R.string.editing, v) else getString(R.string.adding)).show(activity?.supportFragmentManager!!, PersonEditorDialogFragment.TAG)

            }
        })
    }




}