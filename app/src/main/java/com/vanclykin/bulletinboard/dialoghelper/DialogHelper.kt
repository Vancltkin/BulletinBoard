package com.vanclykin.bulletinboard.dialoghelper

import android.app.AlertDialog
import com.vanclykin.bulletinboard.MainActivity
import com.vanclykin.bulletinboard.R
import com.vanclykin.bulletinboard.accounthelper.AccountHelper
import com.vanclykin.bulletinboard.databinding.SignDialogBinding

class DialogHelper(activity: MainActivity) {
    private val activity = activity
    private val accHelper = AccountHelper(activity)

    fun createSignDialog(index: Int) {                                                                          //пояснительную бригаду
        val builder = AlertDialog.Builder(activity)
        val binding = SignDialogBinding.inflate(activity.layoutInflater)
        builder.setView(binding.root)
        builder.show()

        if (index == DialogConst.SIGN_UP_STATE) {
            binding.tvSignTitle.text = activity.resources.getString(R.string.ac_sign_up)
            binding.btSignUpIn.text = activity.resources.getString(R.string.sign_up_action)
        } else {
            binding.tvSignTitle.text = activity.resources.getString(R.string.ac_sign_in)
            binding.btSignUpIn.text = activity.resources.getString(R.string.sign_in_action)
        }
        binding.btSignUpIn.setOnClickListener {
            if (index == DialogConst.SIGN_UP_STATE) {
                accHelper.signUpWithEmail(
                    binding.edSignEmail.text.toString(),
                    binding.edSignPassword.text.toString()
                )
            } else {

            }
        }
    }
}