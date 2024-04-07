package com.example.taskmanager.activities.ui.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.taskmanager.activities.ui.viewmodel.NoteViewModel
import com.example.taskmanager.databinding.ActivityTestingBinding
import com.google.firebase.auth.FirebaseAuth

class TestingActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityTestingBinding
    private val noteViewModel: NoteViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        noteViewModel.randomNote()

        noteViewModel.notaModel.observe(this, Observer { it ->
            binding.txtTitulo.text = it.title
            binding.txtDescription.text = it.description
            binding.txtDate.text = it.createdAt.toString()
        })

    }
}