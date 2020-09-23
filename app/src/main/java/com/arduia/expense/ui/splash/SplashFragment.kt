package com.arduia.expense.ui.splash

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.arduia.core.extension.px
import com.arduia.expense.R
import com.arduia.mvvm.EventObserver
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment: Fragment(){

    private val viewModel by viewModels<SplashViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{
        return createView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycle.addObserver(viewModel)
        setupViewModel()
    }

    private fun setupViewModel(){
        viewModel.firstTimeEvent.observe(viewLifecycleOwner, EventObserver{
            findNavController().popBackStack()
            findNavController().navigate(R.id.dest_language)
        })

        viewModel.normalUserEvent.observe(viewLifecycleOwner, EventObserver{
            findNavController().popBackStack()
            findNavController().navigate(R.id.dest_home)
        })
    }

    private fun createView(): View {

        //Require View Components
        val frameLayout = FrameLayout(requireContext())
        val imageView = ImageView(requireContext())
        val progressBar = ProgressBar(requireContext())

        //Configure
        with(frameLayout){
            background = ContextCompat.getDrawable(requireContext(), R.color.blue_light_500)
            layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
        }

        with(imageView){
            layoutParams = FrameLayout.LayoutParams(px(120), px(120)).apply {
                gravity = Gravity.CENTER
            }
            setImageResource(R.drawable.ic_launcher_foreground)
            setColorFilter(android.R.color.white)
        }

        with(progressBar){
            layoutParams = FrameLayout.LayoutParams(px(30), px(30)).apply {
                gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
                bottomMargin = resources.getDimension(R.dimen.grid_3).toInt()
            }
            id = View.generateViewId()
        }

        //add to Desire Views
        frameLayout.addView(imageView)

        return frameLayout
    }
}
