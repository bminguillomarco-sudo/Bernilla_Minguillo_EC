package com.bernilla.denunciasapp.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bernilla.denunciasapp.databinding.ActivityDetalleBinding

/**
 * Pantalla de Detalle de Denuncia.
 */
class DetalleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetalleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Detalle de Denuncia"

        val id = intent.getIntExtra("DENUNCIA_ID", -1)
        val titulo = intent.getStringExtra("DENUNCIA_TITULO") ?: ""
        val dni = intent.getStringExtra("DENUNCIA_DNI") ?: ""
        val direccion = intent.getStringExtra("DENUNCIA_DIRECCION") ?: ""
        val referencia = intent.getStringExtra("DENUNCIA_REFERENCIA") ?: ""
        val fecha = intent.getStringExtra("DENUNCIA_FECHA") ?: ""
        val usuario = intent.getStringExtra("DENUNCIA_USUARIO") ?: ""

        binding.tvId.text = "Denuncia #${if (id != -1) id else "N/A"}"
        binding.tvTipo.text = titulo
        binding.tvDni.text = "$dni (Usuario: $usuario)"
        binding.tvDireccion.text = "$direccion (Ref: $referencia)"
        binding.tvFecha.text = fecha
        binding.tvDescripcion.text = "Denuncia registrada por el usuario $usuario"

        binding.btnAtender.visibility = View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
