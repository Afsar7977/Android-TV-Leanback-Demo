@file:Suppress("SENSELESS_COMPARISON", "DEPRECATION")

package com.afsar.kotlintv.UI

import android.net.Uri
import android.os.Bundle
import androidx.leanback.app.VideoSupportFragment
import androidx.leanback.app.VideoSupportFragmentGlueHost
import com.afsar.kotlintv.Modal.VideosSource
import com.afsar.kotlintv.Player.Playlist
import com.afsar.kotlintv.Player.VideoPlayerGlue
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ext.leanback.LeanbackPlayerAdapter
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelection
import com.google.android.exoplayer2.trackselection.TrackSelector
import com.google.android.exoplayer2.upstream.BandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.google.android.exoplayer2.util.Util.SDK_INT


/** Handles video playback with media controls. */
class PlaybackVideoFragment : VideoSupportFragment() {

    private lateinit var mPlayerGlue: VideoPlayerGlue
    private lateinit var mPlayerAdapter: LeanbackPlayerAdapter
    private lateinit var mPlayer: SimpleExoPlayer
    private lateinit var mTrackSelector: TrackSelector
    private lateinit var playlistActionListener: PlaylistActionListener

    private lateinit var mVideo: VideosSource
    private lateinit var mPlaylist: Playlist

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mVideo = activity?.intent?.getSerializableExtra(DetailsActivity.MOVIE) as VideosSource
        mPlaylist = Playlist()
    }

    private fun initializePlayer() {
        val bandwidthMeter: BandwidthMeter = DefaultBandwidthMeter()
        val videoTrackSelectionFactory: TrackSelection.Factory =
            AdaptiveTrackSelection.Factory(bandwidthMeter)
        mTrackSelector = DefaultTrackSelector(videoTrackSelectionFactory)
        mPlayer = ExoPlayerFactory.newSimpleInstance(activity, mTrackSelector)
        mPlayerAdapter =
            LeanbackPlayerAdapter(activity, mPlayer,
                UPDATE_DELAY
            )
        playlistActionListener = PlaylistActionListener(mPlaylist)
        mPlayerGlue = VideoPlayerGlue(activity, mPlayerAdapter, playlistActionListener)
        mPlayerGlue.host = VideoSupportFragmentGlueHost(this)
        mPlayerGlue.playWhenPrepared()
        mPlaylist.add(videosSource)
        play(mVideo)
    }

    fun play(videosSource: VideosSource) {
        mPlayerGlue.title = videosSource.title
        mPlayerGlue.subtitle = videosSource.description
        prepareMediaforPlaying(Uri.parse(videosSource.sources.listIterator().next()))
        mPlayerGlue.play()
    }

    private fun prepareMediaforPlaying(uri: Uri) {
        val userAgent =
            Util.getUserAgent(activity, "VideoPlayerGlue")
        val mediaSource: MediaSource = ExtractorMediaSource(
            uri,
            DefaultDataSourceFactory(activity, userAgent),
            DefaultExtractorsFactory(),
            null,
            null
        )
        mPlayer.prepare(mediaSource)
    }

    override fun onPause() {
        super.onPause()
        when {
            mPlayerGlue.isPlaying -> mPlayerGlue.pause()
        }
        when {
            SDK_INT <= 23 -> {
                releasePlayer()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        when {
            SDK_INT > 23 -> {
                initializePlayer()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    override fun onResume() {
        super.onResume()
        when {
            SDK_INT <= 23 && mPlayer == null -> {
                initializePlayer()
            }
        }
    }

    private fun releasePlayer() {
        mPlayer.release()
        mPlayerGlue.pause()
    }

    inner class PlaylistActionListener(private val playlist: Playlist) :
        VideoPlayerGlue.OnActionClickedListener {

        override fun onPrevious() {
            play(playlist.previous()!!)
        }

        override fun onNext() {
            play(playlist.next()!!)
        }
    }

    companion object {
        private var UPDATE_DELAY = 16
        lateinit var videosSource: List<VideosSource>
    }
}