package com.bernilla.denunciasapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bernilla.denunciasapp.R
import com.bernilla.denunciasapp.databinding.ActivityBuscarBinding
import com.bernilla.denunciasapp.ui.adapter.DenunciaAdapter
import com.bernilla.denunciasapp.ui.viewmodel.DenunciaViewModel

/**
 * Pantalla de Búsqueda de Denuncias por DNI.
 */
class BuscarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBuscarBinding
    private val viewModel: DenunciaViewModel by viewModels()
    private lateinit var adapter: DenunciaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuscarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.title_buscar)

        setupRecyclerView()
        setupListeners()
        setupObservers()
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

        binding.rvResultados.layoutManager = LinearLayoutManager(this)
        binding.rvResultados.adapter = adapter
    }

    private fun setupListeners() {
        binding.btnBuscar.setOnClickListener {
            ejecutarBusqueda()
        }
    }

    private fun ejecutarBusqueda() {
        val dni = binding.etBuscarDni.text.toString().trim()

        if (dni.isEmpty() || dni.length != 8) {
            binding.tilBuscarDni.error = "DNI debe tener 8 dígitos"
            return
        }

        binding.tilBuscarDni.error = null
        adapter.limpiar()

        viewModel.listarDenuncias()
    }

    private fun setupObservers() {
        viewModel.denuncias.observe(this) { listaCompleta ->
            val dniIngresado = binding.etBuscarDni.text.toString().trim()
            val filtradas = if (dniIngresado.isNotEmpty()) {
                listaCompleta.filter { it.dni == dniIngresado }
            } else {
                listaCompleta
            }

            adapter.setLista(filtradas)

            if (filtradas.isEmpty()) {
                binding.tvSinResultados.visibility = View.VISIBLE
                binding.rvResultados.visibility = View.GONE
            } else {
                binding.tvSinResultados.visibility = View.GONE
                binding.rvResultados.visibility = View.VISIBLE
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.btnBuscar.isEnabled = !isLoading
        }

        viewModel.errorMessage.observe(this) { mensaje ->
            mensaje?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
                viewModel.clearErrorMessage()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
