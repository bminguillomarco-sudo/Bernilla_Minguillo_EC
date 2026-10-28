package com.bernilla.denunciasapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bernilla.denunciasapp.data.model.Denuncia
import com.bernilla.denunciasapp.databinding.ItemDenunciaBinding

/**
 * Adaptador para mostrar la lista de denuncias en un RecyclerView.
 */
class DenunciaAdapter(
    private var lista: MutableList<Denuncia> = mutableListOf(),
    private val onItemClick: (Denuncia) -> Unit
) : RecyclerView.Adapter<DenunciaAdapter.DenunciaViewHolder>() {

    inner class DenunciaViewHolder(private val binding: ItemDenunciaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(denuncia: Denuncia) {
            binding.tvTipo.text = denuncia.titulo
            binding.tvDni.text = "DNI: ${denuncia.dni} - Usuario: ${denuncia.usuario}"
            binding.tvDireccion.text = "Dirección: ${denuncia.direccion} (${denuncia.referencia})"
            binding.tvFecha.text = "Fecha: ${denuncia.fecha}"

            binding.tvEstadoBadge.text = " REGISTRADA "

            binding.root.setOnClickListener {
                onItemClick(denuncia)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DenunciaViewHolder {
        val binding = ItemDenunciaBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return DenunciaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DenunciaViewHolder, position: Int) {
        holder.bind(lista[position])
    }

    override fun getItemCount(): Int = lista.size

    fun setLista(nuevaLista: List<Denuncia>) {
        lista.clear()
        lista.addAll(nuevaLista)
        notifyDataSetChanged()
    }

    fun limpiar() {
        lista.clear()
        notifyDataSetChanged()
    }
}
