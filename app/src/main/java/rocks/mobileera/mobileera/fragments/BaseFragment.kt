package rocks.mobileera.mobileera.fragments

import android.content.Intent
import android.net.Uri
import android.support.v4.app.Fragment
import android.view.MenuItem
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.Theme
import rocks.mobileera.mobileera.R

abstract class BaseFragment : Fragment() {

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.navigation_info -> {
                activity?.let {context ->
                    val dialog = MaterialDialog.Builder(context)
                            .title(R.string.information)
                            .iconRes(R.drawable.ic_info_blue)
                            .itemsCallback { _, _, index, _ ->

                            }
                            .onPositive { dialog, _ ->
                                dialog.dismiss()
                            }
                            .positiveText(R.string.filter_ok)
                            .autoDismiss(false)
                            .positiveColorRes(R.color.colorPrimary)
                            .customView(R.layout.dialogue_info, true)
                            .theme(Theme.LIGHT)
                            .show()

                    dialog.customView?.findViewById<View>(R.id.sponsorsTextView)?.setOnClickListener {
                        openUrl("https://2017.mobileera.rocks/sponsors/")
                    }

                    dialog.customView?.findViewById<View>(R.id.codTextView)?.setOnClickListener {
                        openUrl("https://2017.mobileera.rocks/cod/")
                    }

                    dialog.customView?.findViewById<View>(R.id.mobileEraTeamTextView)?.setOnClickListener {
                        openUrl("https://2017.mobileera.rocks/team/")
                    }

                    dialog.customView?.findViewById<View>(R.id.contactUsTextView)?.setOnClickListener {
                        sendEmail("contact@mobileera.rocks")
                    }

                    dialog.customView?.findViewById<View>(R.id.credentialsTextView)?.setOnClickListener {
                        sendEmail("loginov.k@gmail.com")
                    }
                }
            }
        }

        return super.onOptionsItemSelected(item)

    }

    private fun sendEmail(email: String) {
        val emailIntent = Intent(Intent.ACTION_SENDTO)
        emailIntent.type = "message/rfc822"
        emailIntent.data = Uri.parse("mailto:$email")
        emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(emailIntent)
        } catch (e: Exception) {
            print(e)
        }
    }

    private fun openUrl(link: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        try {
            startActivity(browserIntent)
        } catch (e: Exception) {
            print(e)
        }
    }


}