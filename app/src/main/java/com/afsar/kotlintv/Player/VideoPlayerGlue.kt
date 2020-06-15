package com.afsar.kotlintv.Player

import android.content.Context
import android.util.Log
import androidx.leanback.media.PlaybackTransportControlGlue
import androidx.leanback.widget.Action
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.PlaybackControlsRow
import androidx.leanback.widget.PlaybackControlsRow.*
import java.util.concurrent.TimeUnit

/*
* Copyright (C) 2017 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
import com.google.android.exoplayer2.ext.leanback.LeanbackPlayerAdapter

/**
 * Manages customizing the actions in the [PlaybackControlsRow]. Adds and manages the
 * following actions to the primary and secondary controls:
 *
 *
 *  * [androidx.leanback.widget.PlaybackControlsRow.RepeatAction]
 *  * [androidx.leanback.widget.PlaybackControlsRow.ThumbsDownAction]
 *  * [androidx.leanback.widget.PlaybackControlsRow.ThumbsUpAction]
 *  * [androidx.leanback.widget.PlaybackControlsRow.SkipPreviousAction]
 *  * [androidx.leanback.widget.PlaybackControlsRow.SkipNextAction]
 *  * [androidx.leanback.widget.PlaybackControlsRow.FastForwardAction]
 *  * [androidx.leanback.widget.PlaybackControlsRow.RewindAction]
 *
 *
 *
 * Note that the superclass, [PlaybackTransportControlGlue], manages the playback controls
 * row.
 */
class VideoPlayerGlue(
    context: Context?,
    playerAdapter: LeanbackPlayerAdapter?,
    private val mActionListener: OnActionClickedListener
) : PlaybackTransportControlGlue<LeanbackPlayerAdapter?>(context, playerAdapter) {
    /**
     * Listens for when skip to next and previous actions have been dispatched.
     */
    interface OnActionClickedListener {
        /**
         * Skip to the previous item in the queue.
         */
        fun onPrevious()

        /**
         * Skip to the next item in the queue.
         */
        fun onNext()
    }

    private val mRepeatAction: RepeatAction? = null
    private val mThumbsUpAction: ThumbsUpAction? = null
    private val mThumbsDownAction: ThumbsDownAction? = null
    private val mSkipPreviousAction: SkipPreviousAction = SkipPreviousAction(context)
    private val mSkipNextAction: SkipNextAction = SkipNextAction(context)
    private val mFastForwardAction: FastForwardAction = FastForwardAction(context)
    private val mRewindAction: RewindAction = RewindAction(context)
    override fun onCreatePrimaryActions(adapter: ArrayObjectAdapter) {
        // Order matters, super.onCreatePrimaryActions() will create the play / pause action.
        // Will display as follows:
        // play/pause, previous, rewind, fast forward, next
        //   > /||      |<        <<        >>         >|
        super.onCreatePrimaryActions(adapter)
        adapter.add(mSkipPreviousAction)
        adapter.add(mRewindAction)
        adapter.add(mFastForwardAction)
        adapter.add(mSkipNextAction)
    }

    override fun onActionClicked(action: Action) {
        if (shouldDispatchAction(action)) {
            dispatchAction(action)
            return
        }
        // Super class handles play/pause and delegates to abstract methods next()/previous().
        super.onActionClicked(action)
    }

    // Should dispatch actions that the super class does not supply callbacks for.
    private fun shouldDispatchAction(action: Action): Boolean {
        return action === mRewindAction || action === mFastForwardAction || action === mThumbsDownAction || action === mThumbsUpAction || action === mRepeatAction
    }

    private fun dispatchAction(action: Action) {
        // Primary actions are handled manually.
        when {
            action === mRewindAction -> {
                rewind()
            }
            action === mFastForwardAction -> {
                fastForward()
            }
            action is MultiAction -> {
                action.nextIndex()
                // Notify adapter of action changes to handle secondary actions, such as, thumbs up/down
                // and repeat.
                notifyActionChanged(
                    action,
                    controlsRow.secondaryActionsAdapter as ArrayObjectAdapter
                )
            }
        }
    }

    private fun notifyActionChanged(
        action: MultiAction, adapter: ArrayObjectAdapter?
    ) {
        if (adapter != null) {
            val index = adapter.indexOf(action)
            if (index >= 0) {
                adapter.notifyArrayItemRangeChanged(index, 1)
            }
        }
    }

    override fun next() {
        Log.d("next", "called")
        mActionListener.onNext()
    }

    override fun previous() {
        Log.d("previous", "called")
        mActionListener.onPrevious()
    }

    /**
     * Skips backwards 10 seconds.
     */
    fun rewind() {
        var newPosition = currentPosition - TEN_SECONDS
        newPosition = if (newPosition < 0) 0 else newPosition
        playerAdapter!!.seekTo(newPosition)
    }

    /**
     * Skips forward 10 seconds.
     */
    fun fastForward() {
        if (duration > -1) {
            var newPosition =
                currentPosition + TEN_SECONDS
            newPosition = if (newPosition > duration) duration else newPosition
            playerAdapter!!.seekTo(newPosition)
        }
    }

    companion object {
        private val TEN_SECONDS = TimeUnit.SECONDS.toMillis(10)
    }

}