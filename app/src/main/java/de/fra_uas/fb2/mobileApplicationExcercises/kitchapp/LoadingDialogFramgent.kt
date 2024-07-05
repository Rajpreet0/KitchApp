package de.fra_uas.fb2.mobileApplicationExcercises.kitchapp


import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class LoadingDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.progress_dialog, null)
        builder.setView(view)
        builder.setCancelable(false)
        return builder.create()
    }
}