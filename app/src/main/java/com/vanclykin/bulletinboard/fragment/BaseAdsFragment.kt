package com.vanclykin.bulletinboard.fragment

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.vanclykin.bulletinboard.R
import com.vanclykin.bulletinboard.databinding.ImageListFragBinding

open class BaseAdsFragment: Fragment(), InterAdsClose {
    lateinit var adView: AdView
    lateinit var binding: ImageListFragBinding
    var interAd: InterstitialAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadInterAds()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadBannerAds()
    }

    override fun onResume() {
        super.onResume()
        adView.resume()
    }

    override fun onPause() {
        super.onPause()
        adView.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        adView.destroy()
    }

    private fun loadBannerAds(){
        MobileAds.initialize(activity as Activity)
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }

    private fun loadInterAds(){

        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(context as Activity, getString(R.string.ad_id_interstitial), adRequest, object : InterstitialAdLoadCallback(){

            override fun onAdLoaded(ad: InterstitialAd) {
                interAd = ad
            }
        })
    }

    fun showInterAds(){
        if (interAd != null){
            interAd?.fullScreenContentCallback = object: FullScreenContentCallback(){

                override fun onAdDismissedFullScreenContent() {

                    onClose()
                }

                override fun onAdFailedToShowFullScreenContent(p0: AdError) {

                    onClose()
                }
            }

            interAd?.show(activity as Activity)

        } else {

            onClose()
        }
    }

    override fun onClose() {}

}