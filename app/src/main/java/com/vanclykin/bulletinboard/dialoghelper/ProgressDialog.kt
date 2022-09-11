package com.vanclykin.bulletinboard.dialoghelper

import android.app.Activity
import android.app.AlertDialog
import com.vanclykin.bulletinboard.databinding.ProgressDialogLayoutBinding

object ProgressDialog {

    fun createProgressDialog(activity: Activity): AlertDialog {
        //ProgressBar
        val builder = AlertDialog.Builder(activity)
        val binding = ProgressDialogLayoutBinding.inflate(activity.layoutInflater)
        builder.setView(binding.root)
        val dialog = builder.create()

        dialog.setCancelable(false)
        dialog.show() //показать
        return dialog
    }
}