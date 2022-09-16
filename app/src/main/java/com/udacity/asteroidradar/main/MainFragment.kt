package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.AsteroidAdapter
import com.udacity.asteroidradar.DataWorker
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.repository.AsteroidsRepository

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private lateinit var adapter: AsteroidAdapter

    private val viewModel: MainViewModel by lazy {
        val factory = MainViewModelFactory(requireActivity().application)
        ViewModelProvider(this,factory).get(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        setUpRecyclerAdapter()
        setObservers()
        setHasOptionsMenu(true)
        return binding.root
    }

    private fun setUpRecyclerAdapter(){
        adapter = AsteroidAdapter(AsteroidAdapter.AsteroidClickListener {
            findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
        })

        binding.asteroidRecycler.adapter = adapter
    }

    private fun setObservers() {
        viewModel.asteroids.observe(viewLifecycleOwner){ asteroids ->
            if (!asteroids.isNullOrEmpty()) {
                adapter.submitList(asteroids)
                binding.asteroidRecycler.smoothScrollToPosition(0)
                binding.statusLoadingWheel.visibility = View.GONE
            }
        }

        AsteroidsRepository.isFinished.observe(viewLifecycleOwner){
            if (it) viewModel.getTodayAsteroids()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.today_asteroids -> viewModel.getTodayAsteroids()
            R.id.view_week_asteroid -> viewModel.getWeekAsteroids()
            R.id.saved_asteroids -> viewModel.getSavedAsteroids()
        }
        return true
    }
}
