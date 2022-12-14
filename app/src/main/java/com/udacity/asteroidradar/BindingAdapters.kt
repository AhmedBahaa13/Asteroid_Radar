package com.udacity.asteroidradar

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

@BindingAdapter("statusIcon")
fun bindAsteroidStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
        imageView.contentDescription = "It is potentially hazardous"
    } else {
        imageView.setImageResource(R.drawable.ic_status_normal)
        imageView.contentDescription = "It is normal"
    }
}

@BindingAdapter("asteroidStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.asteroid_hazardous)
        imageView.contentDescription = "It is hazardous"
    } else {
        imageView.setImageResource(R.drawable.asteroid_safe)
        imageView.contentDescription = "It is Safe"
    }

}

@BindingAdapter("astronomicalUnitText")
fun bindTextViewToAstronomicalUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.astronomical_unit_format), number)
    textView.contentDescription = "Astronomical is ${textView.text}"

}

@BindingAdapter("kmUnitText")
fun bindTextViewToKmUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_unit_format), number)
    textView.contentDescription = "Estimated Diameter is ${textView.text}"

}

@BindingAdapter("velocityText")
fun bindTextViewToDisplayVelocity(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_s_unit_format), number)
    textView.contentDescription = "Velocity is ${textView.text}"

}

@BindingAdapter("setData")
fun bindRecyclerData(recyclerView: RecyclerView,list:List<Asteroid>?){
    val adapter = recyclerView.adapter as AsteroidAdapter
    list?.let {
        adapter.submitList(it)
    }
    Log.d("MainViewModel", "bindRecyclerData: $list")
    recyclerView.smoothScrollToPosition(0)
}

@BindingAdapter("setMainImage")
fun bindMainImage(imageView: ImageView,image: MainImage?){
    image?.let {
        Picasso.with(imageView.context).load(image.url).into(imageView)
        imageView.contentDescription = image.title
    }
}

@BindingAdapter("showProgressBar")
fun bindProgressBar(progressBar: ProgressBar,showProgressBar:Boolean){
    if (showProgressBar){
        progressBar.visibility = View.VISIBLE
    }else{
        progressBar.visibility = View.GONE
    }
}
