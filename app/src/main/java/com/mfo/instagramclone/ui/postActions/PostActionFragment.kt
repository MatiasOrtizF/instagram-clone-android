package com.mfo.instagramclone.ui.postActions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.mfo.instagramclone.databinding.FragmentPostActionBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostActionFragment : Fragment() {
    private var _binding: FragmentPostActionBinding? = null
    private val binding get() = _binding!!

    private val args: PostActionFragmentArgs by navArgs()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        println(args.label)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =  FragmentPostActionBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}