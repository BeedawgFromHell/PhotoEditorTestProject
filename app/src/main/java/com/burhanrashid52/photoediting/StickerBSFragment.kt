package com.burhanrashid52.photoediting

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.bottomsheet.BottomSheetBehavior
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.burhanrashid52.photoediting.domain.StickerModel
import java.util.ArrayList

class StickerBSFragment : BottomSheetDialogFragment() {
    private var mStickerListener: StickerListener? = null
    private var mStickerAdapter: StickerAdapter? = null

    fun setStickerListener(stickerListener: StickerListener?) {
        mStickerListener = stickerListener
    }

    interface StickerListener {
        fun onStickerClick(bitmap: Bitmap?)
    }

    private val mBottomSheetBehaviorCallback: BottomSheetCallback = object : BottomSheetCallback() {
        override fun onStateChanged(bottomSheet: View, newState: Int) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss()
            }
        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) {}
    }

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        val contentView = View.inflate(context, R.layout.fragment_bottom_sticker_emoji_dialog, null)
        dialog.setContentView(contentView)
        val params = (contentView.parent as View).layoutParams as CoordinatorLayout.LayoutParams
        val behavior = params.behavior
        if (behavior != null && behavior is BottomSheetBehavior<*>) {
            behavior.setBottomSheetCallback(mBottomSheetBehaviorCallback)
        }
        (contentView.parent as View).setBackgroundColor(resources.getColor(android.R.color.transparent))
        val rvEmoji: RecyclerView = contentView.findViewById(R.id.rvEmoji)
        val gridLayoutManager = GridLayoutManager(activity, 3)
        rvEmoji.layoutManager = gridLayoutManager
        mStickerAdapter = StickerAdapter()
        rvEmoji.adapter = mStickerAdapter
        rvEmoji.setHasFixedSize(true)
        rvEmoji.setItemViewCacheSize(stickersModels.size)

        parseArgs()
    }

    private fun parseArgs() {
        val stickersModel =
            arguments?.getParcelableArrayList<StickerModel>(STICKERS_ARG) as ArrayList<StickerModel>
        stickersModels = stickersModel
    }

    inner class StickerAdapter : RecyclerView.Adapter<StickerAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.row_sticker, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            // Load sticker image from remote url
            Glide.with(requireContext())
                .asBitmap()
                .load(stickersModels[position])
                .into(holder.imgSticker)

            val sticker = stickersModels[position]
            if (sticker.sourceType == "url") {
                Glide.with(requireContext())
                    .asBitmap()
                    .load(sticker.address)
                    .into(holder.imgSticker)
            } else {
                val bitmap = BitmapFactory.decodeStream(context?.assets?.open("filters/${sticker.address}"))
                Glide.with(requireContext())
                    .asBitmap()
                    .load(bitmap)
                    .into(holder.imgSticker)
            }
        }

        override fun getItemCount(): Int {
            return stickersModels.size
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val imgSticker: ImageView = itemView.findViewById(R.id.imgSticker)

            init {
                itemView.setOnClickListener {
                    if (mStickerListener != null) {
                        val sticker = stickersModels[layoutPosition]

                        if(sticker.sourceType == "url") {
                            Glide.with(requireContext())
                                .asBitmap()
                                .load(sticker.address)
                                .into(object : CustomTarget<Bitmap?>(256, 256) {
                                    override fun onResourceReady(
                                        resource: Bitmap,
                                        transition: Transition<in Bitmap?>?
                                    ) {
                                        mStickerListener!!.onStickerClick(resource)
                                    }

                                    override fun onLoadCleared(placeholder: Drawable?) {}
                                })
                        } else {
                            val bitmap = BitmapFactory.decodeStream(context?.assets?.open("filters/${sticker.address}"))
                            Glide.with(requireContext())
                                .asBitmap()
                                .load(bitmap)
                                .into(object : CustomTarget<Bitmap?>(256, 256) {
                                    override fun onResourceReady(
                                        resource: Bitmap,
                                        transition: Transition<in Bitmap?>?
                                    ) {
                                        mStickerListener!!.onStickerClick(resource)
                                    }

                                    override fun onLoadCleared(placeholder: Drawable?) {}
                                })
                        }

                    }
                    dismiss()
                }
            }
        }
    }

    companion object {
        // Image Urls from flaticon(https://www.flaticon.com/stickers-pack/food-289)
//        private var stickerPathList = arrayListOf(
//                "https://cdn-icons-png.flaticon.com/256/4392/4392452.png",
//                "https://cdn-icons-png.flaticon.com/256/4392/4392455.png",
//                "https://cdn-icons-png.flaticon.com/256/4392/4392459.png",
//                "https://cdn-icons-png.flaticon.com/256/4392/4392462.png",
//                "https://cdn-icons-png.flaticon.com/256/4392/4392465.png",
//                "https://cdn-icons-png.flaticon.com/256/4392/4392467.png",
//                "https://cdn-icons-png.flaticon.com/256/4392/4392469.png",
//                "https://cdn-icons-png.flaticon.com/256/4392/4392471.png",
//                "https://cdn-icons-png.flaticon.com/256/4392/4392522.png",
//        )

        private var stickersModels = mutableListOf<StickerModel>()

        const val STICKERS_ARG = "stickers_arg"
    }
}