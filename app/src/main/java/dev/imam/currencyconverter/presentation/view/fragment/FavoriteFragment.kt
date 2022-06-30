package dev.imam.currencyconverter.presentation.view.fragment

import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.imam.currencyconverter.R
import dev.imam.currencyconverter.presentation.contract.HasCustomTitle
import dev.imam.currencyconverter.presentation.model.VideoContent
import dev.imam.currencyconverter.presentation.model.VideoContentService
import dev.imam.currencyconverter.presentation.view.adapter.VideoContentAdapter


class FavoriteFragment : Fragment(), HasCustomTitle {
    private lateinit var videoContentAdapter: VideoContentAdapter
    private val service: VideoContentService = VideoContentService()
    private lateinit var motionLayout:MotionLayout
    private lateinit var videoImageView:ImageView;
    private lateinit var videoTitleTextView:TextView;
    private lateinit var videoDescriptionTextView:TextView;
    private lateinit var smVideoTitleTextView:TextView;


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_favorite, container, false)
        setupViewHolder(view)
        motionLayout = view.findViewById(R.id.favouriteMotionLayout)

        videoImageView = view.findViewById(R.id.videoImageView)
        videoTitleTextView = view.findViewById(R.id.videoTitleTextView)
        videoDescriptionTextView = view.findViewById(R.id.videoDescriptionTextView)
        smVideoTitleTextView = view.findViewById(R.id.smVideoTitleTextView)

        view.findViewById<AppCompatImageButton>(R.id.closeImageButton).setOnClickListener {
            motionLayout.setTransition(R.id.thirdTransition)
            motionLayout.transitionToEnd(){
                motionLayout.setTransition(R.id.firstTransition)
            }
        }

        return view
    }

    private fun setupViewHolder(view: View) {
        videoContentAdapter = VideoContentAdapter(clickListener = {
            when(motionLayout.currentState){
                R.id.startState -> {
                    changeVideoDetail(it)
                    motionLayout.transitionToEnd(){
                        motionLayout.setTransition(R.id.secondTransition)
                    }
                }
                R.id.endState -> {
                    motionLayout.transitionToState(R.id.middleState)
                    changeVideoDetail(it)
                }
            }
        })
        videoContentAdapter.setItems(service.getVideoContentList())

        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        view.findViewById<RecyclerView>(R.id.videoRecyclerView).apply {
            this.adapter = videoContentAdapter
            this.layoutManager = layoutManager
            itemAnimator = DefaultItemAnimator()
        }
    }

    private fun changeVideoDetail(model: VideoContent){
        Glide.with(this)
            .asBitmap()
            .load(model.stringImageUrl)
            .error(R.drawable.video_image)
            .into(videoImageView)

        videoTitleTextView.text = model.title
        videoDescriptionTextView.text = model.description
        smVideoTitleTextView.text = model.description
    }

    override fun getTitleRes(): Int = R.string.favourite

    override fun getSelectedPageRes(): Int = R.id.page_2
}