package com.simbaone.lifehacks.presentation.home

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.simbaone.SimbaAds
import com.simbaone.lifehacks.R
import com.simbaone.lifehacks.base.BaseFragmentVB
import com.simbaone.lifehacks.constants.Extras
import com.simbaone.lifehacks.databinding.FragmentHomeBinding
import com.simbaone.lifehacks.extensions.collectWhenStarted
import com.simbaone.lifehacks.utils.SpacingItemDecorator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class HomeFragment : BaseFragmentVB<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val viewModel by viewModels<HomeViewModel>()

    private val mAdapter by lazy {
        CategoriesAdapter {
            viewModel.onItemClick(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpClicks()
        setUpRecyclerViewAdapter()
        addObservers()
    }

    private fun setUpClicks() {
        binding.apply {
            ivPrivacyPolicy.setOnClickListener {
                launchBrowser(getString(R.string.privacy_policy_link))
            }
        }
    }

    fun launchBrowser(link: String?) {
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
        } catch (anfe: ActivityNotFoundException) {
            anfe.printStackTrace()
        }
    }

    private fun setUpRecyclerViewAdapter() {
        binding.rvCategories.apply {
            layoutManager = GridLayoutManager(requireContext(), 2).apply {
                reverseLayout = true
            }

            val x = (resources.displayMetrics.density * 4).toInt() //converting dp to pixels
            addItemDecoration(SpacingItemDecorator(x)) //setting space between items in RecyclerView

            adapter = mAdapter
        }
    }

    private fun addObservers() {
        collectWhenStarted {
            viewModel.uiState.collectLatest { state ->
                mAdapter.submitList(state.categories)
                binding.apply {
                    tvTitle.text = state.title
                }
            }
        }

        collectWhenStarted {
            viewModel.channel.collectLatest { event ->
                when (event) {
                    is HomeViewModel.NavigationEvents.NavigateToLifeHacks -> {
                        /*SimbaAds.showInterstitialAd(
                            activity = requireActivity(),
                            onAdDismissed = {

                            },
                            onAdFailedToShow = {

                            }
                        )*/
                        findNavController().navigate(
                            R.id.carouselFragment,
                            Bundle().apply { putParcelable(Extras.CATEGORY, event.category) }
                        )
                    }
                }
            }
        }
    }

}