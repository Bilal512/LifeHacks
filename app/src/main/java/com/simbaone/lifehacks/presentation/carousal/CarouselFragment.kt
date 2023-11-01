package com.simbaone.lifehacks.presentation.carousal

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.graphics.Path
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import androidx.core.animation.doOnStart
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.simbaone.lifehacks.R
import com.simbaone.lifehacks.base.BaseFragmentVB
import com.simbaone.lifehacks.browser.BrowserActivity
import com.simbaone.lifehacks.databinding.FragmentCarousalBinding
import com.simbaone.lifehacks.domain.repo.LogRepo
import com.simbaone.lifehacks.extensions.collectWhenStarted
import com.simbaone.lifehacks.extensions.setupCarousel
import com.simbaone.lifehacks.utils.ColorUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject


@AndroidEntryPoint
class CarouselFragment : BaseFragmentVB<FragmentCarousalBinding>(FragmentCarousalBinding::inflate) {

    private val viewModel by viewModels<CarousalViewModel>()

    @Inject
    lateinit var logRepo: LogRepo

    private val mAdapter by lazy { CarousalAdapter() }

    lateinit var mColorAnimation: ValueAnimator

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startGolaAnimation(binding.ivGolaYellow)
        startGolaAnimation(binding.ivGolaPurpule, startAngle = 200f)
//        startGolaAnimation(binding.ivGolaStar, startAngle = 100f)
        setUpClicks()
        setUpViewPager()
        addObservers()
    }

    private fun setUpClicks() {
        binding.apply {
            ivCredit.setOnClickListener { viewModel.onCreditButtonClick() }
        }
    }

    private fun startGolaAnimation(imageView: ImageView, startAngle: Float = 0f) {
        val totalScreenWidth = requireActivity().resources.displayMetrics.widthPixels.toFloat()
        val totalScreenHeight = requireActivity().resources.displayMetrics.heightPixels.toFloat()

        imageView.scaleX = 4f
        imageView.scaleY = 4f

//        var startAngle = 0f
        var endAngle = -359f
        var mStartDelay = 20L
        var mDuration = 40000L

        val largeBox = Rect()
        largeBox.top = -400
        largeBox.left = -400
        largeBox.right = (totalScreenWidth - 300).toInt()
        largeBox.bottom = (totalScreenHeight - 300).toInt()

        val path = Path().apply {
            arcTo(
                largeBox.left.toFloat(),
                largeBox.top.toFloat(),
                largeBox.right.toFloat(),
                largeBox.bottom.toFloat(),
                startAngle, endAngle,
                true
            )
        }

        val animator = ObjectAnimator.ofFloat(imageView, View.X, View.Y, path).apply {
            duration = mDuration
            repeatMode = ObjectAnimator.RESTART
            repeatCount = ObjectAnimator.INFINITE
            interpolator = LinearInterpolator()
            startDelay = mStartDelay
        }

        AnimatorSet().apply {
            play(animator)
            addListener(
                doOnStart {
                    logRepo.logError("Animation Started")
                }
            )
        }.start()
    }


    private fun setUpViewPager() {
        mColorAnimation = ValueAnimator.ofObject(
            ArgbEvaluator(),
            ColorUtils.generateRandomColors(),
            ColorUtils.generateRandomColors(),
            ColorUtils.generateRandomColors(),
            ColorUtils.generateRandomColors(),
            ColorUtils.generateRandomColors()
        )

        mColorAnimation.addUpdateListener(AnimatorUpdateListener { animator ->
            binding.rootLayout.setBackgroundColor(
                animator.animatedValue as Int
            )
        })

        binding.pager.apply {
            setupCarousel()
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                    mColorAnimation.currentPlayTime =
                        ((positionOffset + position) * 10000000000L).toLong()
                }
            })
            adapter = mAdapter
        }
    }

    private fun addObservers() {
        collectWhenStarted {
            viewModel.uiState.collectLatest { state ->
                if (state.lifeHacks.isNotEmpty()) {
                    mColorAnimation.duration = (state.lifeHacks.size - 1) * 10000000000L
                    mAdapter.submitList(state.lifeHacks)
                }

                binding.apply {
                    ivCredit.isVisible = state.showCreditButton
                    tvTitle.text = state.title
                }
            }
        }

        collectWhenStarted {
            viewModel.channel.collectLatest { event ->
                when(event) {
                    is CarousalViewModel.NavigationEvents.LaunchBrowser -> {
                        BrowserActivity.start(requireContext(), event.link, "Credit goes to")
                    }
                }
            }
        }
    }

}