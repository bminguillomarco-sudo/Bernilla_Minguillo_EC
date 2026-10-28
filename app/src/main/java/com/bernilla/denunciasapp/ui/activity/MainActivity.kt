package com.bernilla.denunciasapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bernilla.denunciasapp.databinding.ActivityMainBinding
import com.bernilla.denunciasapp.ui.adapter.DenunciaAdapter
import com.bernilla.denunciasapp.ui.viewmodel.DenunciaViewModel

/**
 * Pantalla Principal para listar denuncias consumiendo la API REST en Render.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: DenunciaViewModel by viewModels()
    private lateinit var adapter: DenunciaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupListeners()
        setupObservers()

        viewModel.listarDenuncias()
    }

    override fun onResume() {
        super.onResume()
        viewModel.listarDenuncias()
    }

    private fun setupRecyclerView() {
        adapter = DenunciaAdapter { denuncia ->
            val intent = Intent(this, DetalleActivity::class.java).apply {
                putExtra("DENUNCIA_ID", denuncia.id ?: -1)
                putExtra("DENUNCIA_TITULO", denuncia.titulo)
                putExtra("DENUNCIA_DNI", denuncia.dni)
                putExtra("DENUNCIA_DIRECCION", denuncia.direccion)
                putExtra("DENUNCIA_REFERENCIA", denuncia.referencia)
                putExtra("DENUNCIA_FECHA", denuncia.fecha)
                putExtra("DENUNCIA_USUARIO", denuncia.usuario)
            }
            startActivity(intent)
        }

        binding.rvDenuncias.layoutManager = LinearLayoutManager(this)
        binding.rvDenuncias.adapter = adapter
    }

    private fun setupListeners() {
        binding.fabAgregar.setOnClickListener {
            startActivity(Intent(this, RegistrarActivity::class.java))
        }
    }

    private fun setupObservers() {
        viewModel.denuncias.observe(this) { lista ->
            adapter.setLista(lista)

            if (lista.isEmpty()) {
                binding.tvSinDenuncias.visibility = View.VISIBLE
                binding.rvDenuncias.visibility = View.GONE
            } else {
                binding.tvSinDenuncias.visibility = View.GONE
                binding.rvDenuncias.visibility = View.VISIBLE
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.errorMessage.observe(this) { mensaje ->
            mensaje?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
                viewModel.clearErrorMessage()
            }
        }
    }
}
