package com.udacity.asteroidradar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.databinding.AsteroidItemBinding

class AsteroidAdapter(val clickListener: AsteroidClickListener):ListAdapter<Asteroid,AsteroidAdapter.AsteroidViewHolder>(DiffCallBack) {

    companion object DiffCallBack :DiffUtil.ItemCallback<Asteroid>(){
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.id == newItem.id
        }

    }

   inner class AsteroidViewHolder(private var binding: AsteroidItemBinding) :RecyclerView.ViewHolder(binding.root) {
        fun bind(asteroid: Asteroid){
            binding.asteroid = asteroid
            binding.executePendingBindings()
            binding.root.setOnClickListener {
                clickListener.onClick(asteroid)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return AsteroidViewHolder(DataBindingUtil.inflate(inflater,R.layout.asteroid_item,parent,false))
    }

    override fun onBindViewHolder(holder: AsteroidViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class AsteroidClickListener(private val click:(Asteroid)->Unit){
        fun onClick(asteroid: Asteroid) = click(asteroid)
    }
}