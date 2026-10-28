package com.bernilla.denunciasapp.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bernilla.denunciasapp.databinding.ActivityRegistrarBinding
import com.bernilla.denunciasapp.ui.viewmodel.DenunciaViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Pantalla para registrar una nueva denuncia.
 * Garantiza enviar la fecha en formato ISO "yyyy-MM-dd'T'HH:mm:ss" compatible con LocalDateTime de Spring Boot.
 */
class RegistrarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrarBinding
    private val viewModel: DenunciaViewModel by viewModels()

    private val tiposDenuncia = arrayOf(
        "Robo de equipo",
        "Asalto a mano armada",
        "Vandalismo / Daño a la propiedad",
        "Hurto simple",
        "Violencia / Extorsión",
        "Otros incidentes"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Registrar Denuncia"

        // Genera la fecha actual en formato ISO completo "yyyy-MM-dd'T'HH:mm:ss"
        val fechaIso = generarFechaActualIso()
        binding.etFecha.setText(fechaIso)

        setupTipoDropdown()
        setupListeners()
        setupObservers()
    }

    /**
     * Genera la fecha y hora actual con el formato esperado por LocalDateTime en Spring Boot.
     * Ejemplo: "2026-10-28T15:30:00"
     */
    private fun generarFechaActualIso(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        return sdf.format(Date())
    }

    /**
     * Asegura que cualquier fecha ingresada en formato "yyyy-MM-dd" o con espacio sea convertida al formato ISO "yyyy-MM-dd'T'00:00:00" esperado por LocalDateTime en Spring Boot.
     */
    private fun formatearAISO(fechaIngresada: String): String {
        val textoLimpio = fechaIngresada.trim()
        return when {
            textoLimpio.isEmpty() -> generarFechaActualIso()
            textoLimpio.contains("T") -> textoLimpio
            textoLimpio.contains(" ") -> textoLimpio.replace(" ", "T")
            textoLimpio.matches(Regex("^\\d{4}-\\d{2}-\\d{2}$")) -> "${textoLimpio}T00:00:00"
            textoLimpio.length == 10 -> "${textoLimpio}T00:00:00"
            else -> generarFechaActualIso()
        }
    }

    private fun setupTipoDropdown() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, tiposDenuncia)
        binding.actvTipo.setAdapter(adapter)
        binding.actvTipo.setText(tiposDenuncia[0], false)
    }

    private fun setupListeners() {
        binding.btnRegistrar.setOnClickListener {
            if (validarFormulario()) {
                val dni = binding.etDni.text.toString().trim()
                val tipoSeleccionado = binding.actvTipo.text.toString().trim()
                val descripcionIngresada = binding.etTitulo.text.toString().trim()
                val direccion = binding.etDireccion.text.toString().trim()
                val referencia = binding.etReferencia.text.toString().trim().ifEmpty { "Sin referencia" }
                val usuario = binding.etUsuario.text.toString().trim().ifEmpty { "mbernilla" }

                // Convierte la fecha ingresada al formato ISO LocalDateTime exigido por Spring Boot ("2026-10-28T00:00:00")
                val fechaRaw = binding.etFecha.text.toString().trim()
                val fechaIsoFormateada = formatearAISO(fechaRaw)

                val tituloFinal = if (descripcionIngresada.isNotEmpty()) {
                    "$tipoSeleccionado - $descripcionIngresada"
                } else {
                    tipoSeleccionado
                }

                viewModel.registrarDenuncia(
                    dni = dni,
                    tipo = tipoSeleccionado,
                    descripcion = descripcionIngresada,
                    direccion = direccion,
                    referencia = referencia,
                    fecha = fechaIsoFormateada,
                    usuario = usuario,
                    estado = 1,
                    titulo = tituloFinal,
                )
            }
        }
    }

    private fun validarFormulario(): Boolean {
        var esValido = true

        val dni = binding.etDni.text.toString().trim()
        val direccion = binding.etDireccion.text.toString().trim()
        val tipo = binding.actvTipo.text.toString().trim()

        if (dni.isEmpty() || dni.length != 8) {
            binding.tilDni.error = "DNI debe tener 8 dígitos numéricos"
            esValido = false
        } else {
            binding.tilDni.error = null
        }

        if (tipo.isEmpty()) {
            binding.tilTipo.error = "Debe seleccionar un tipo de denuncia"
            esValido = false
        } else {
            binding.tilTipo.error = null
        }

        if (direccion.isEmpty()) {
            binding.tilDireccion.error = "La dirección es obligatoria"
            esValido = false
        } else {
            binding.tilDireccion.error = null
        }

        return esValido
    }

    private fun setupObservers() {
        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.btnRegistrar.isEnabled = !isLoading
        }

        viewModel.operacionExitosa.observe(this) { exitosa ->
            if (exitosa) {
                Toast.makeText(this, "¡Denuncia registrada con éxito!", Toast.LENGTH_LONG).show()
                finish()
            }
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
