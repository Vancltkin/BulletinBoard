package com.vanclykin.bulletinboard.dialoghelper

import android.app.AlertDialog
import android.view.View
import android.widget.Toast
import com.vanclykin.bulletinboard.MainActivity
import com.vanclykin.bulletinboard.R
import com.vanclykin.bulletinboard.accounthelper.AccountHelper
import com.vanclykin.bulletinboard.databinding.SignDialogBinding

class DialogHelper(private val activity: MainActivity) {
    val accHelper = AccountHelper(activity)

    fun createSignDialog(index: Int) {                                                                          //пояснительную бригаду
        val builder = AlertDialog.Builder(activity)
        val binding = SignDialogBinding.inflate(activity.layoutInflater)
        builder.setView(binding.root)
        setDialogState(index, binding)

        val dialog = builder.create()
        binding.btSignUpIn.setOnClickListener {
            setOnClickSingUpIn(index, binding, dialog)
        }
        binding.btForgetPass.setOnClickListener {
            setOnClickResetPassword(binding, dialog)
        }
        binding.btGoogleSignIn.setOnClickListener {
            accHelper.signInWithGoogle()
            dialog.dismiss()
        }
        dialog.show() //показать
    }

    private fun setOnClickResetPassword(binding: SignDialogBinding, dialog: AlertDialog?) {
        if (binding.edSignEmail.text.isNotEmpty()) {
            activity.mAuth.sendPasswordResetEmail(binding.edSignEmail.text.toString())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            activity,
                            R.string.email_reset_password_was_sent,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            dialog?.dismiss()
        } else {
            binding.tvSignTitle.text = activity.resources.getString(R.string.forget_password_title)
            binding.tvDialogMessage.visibility = View.VISIBLE
            binding.btForgetPass.text =  activity.resources.getString(R.string.forget_password_text)
            binding.btSignUpIn.visibility = View.GONE
            binding.btGoogleSignIn.visibility = View.GONE
            binding.edSignPassword.visibility = View.GONE
        }
    }

    private fun setOnClickSingUpIn(index: Int, binding: SignDialogBinding, dialog: AlertDialog?) {

        dialog?.dismiss() //закрытие диалога с вводом

        if (index == DialogConst.SIGN_UP_STATE) {
            accHelper.signUpWithEmail(
                binding.edSignEmail.text.toString(),
                binding.edSignPassword.text.toString()
            )
        } else {
            accHelper.signInWithEmail(
                binding.edSignEmail.text.toString(),
                binding.edSignPassword.text.toString()
            )
        }
    }

    private fun setDialogState(index: Int, binding: SignDialogBinding) {

        if (index == DialogConst.SIGN_UP_STATE) {
            binding.tvSignTitle.text = activity.resources.getString(R.string.ac_sign_up)
            binding.btSignUpIn.text = activity.resources.getString(R.string.sign_up_action)
        } else {
            binding.tvSignTitle.text = activity.resources.getString(R.string.ac_sign_in)
            binding.btSignUpIn.text = activity.resources.getString(R.string.sign_in_action)
            binding.btForgetPass.visibility = View.VISIBLE
        }

    }
}