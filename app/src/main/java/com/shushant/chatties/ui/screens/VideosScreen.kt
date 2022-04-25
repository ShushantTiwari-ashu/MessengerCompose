package com.shushant.chatties.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.VerticalPager
import com.google.accompanist.pager.rememberPagerState
import com.shushant.chatties.model.VideoItem
import timber.log.Timber

@ExperimentalPagerApi
@Composable
fun VideosScreen(viewModel: VideosViewModel = hiltViewModel()) {

    val videos by viewModel.videos.observeAsState(listOf())

    val pagerState = rememberPagerState(videos.size)

    LaunchedEffect(pagerState) {
        // Collect from the a snapshotFlow reading the currentPage
        snapshotFlow { pagerState.currentPage }.collect { page ->
            Timber.e(page.toString())
        }
    }

    VerticalPager(
        state = pagerState,
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 56.dp)
    ) { page ->
        // Our page content
        val movie = videos[page]
        val isSelected = pagerState.currentPage == page
        PagerItem(movie, isSelected)
    }
}

@Composable
fun PagerItem(movie: VideoItem, selected: Boolean) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(4.dp))
    ) {
        VideoPlayer(movie.mediaUrl, selected,context)
    }
}
