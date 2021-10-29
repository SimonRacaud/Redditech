package my.epi.redditech.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface

class ErrorMessage {
    companion object {
        fun show(context: Activity, message: String, onClickCallback: (() -> Unit)? = null) {
            context.runOnUiThread {
                if (!context.isFinishing) {
                    var listener: DialogInterface.OnClickListener? = null

                    if (onClickCallback != null) {
                        listener = object : DialogInterface.OnClickListener {
                            override fun onClick(dialog: DialogInterface?, which: Int) {
                                onClickCallback()
                            }
                        }
                    }
                    AlertDialog.Builder(context)
                        .setTitle("An error occurred")
                        .setMessage(message)
                        .setCancelable(false)
                        .setPositiveButton("ok", listener).show()
                }
            }
        }
    }
}