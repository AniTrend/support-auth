package io.wax911.support.auth

import android.os.Bundle
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.navigation.NavigationView
import io.wax911.support.core.callback.AuthCallback
import io.wax911.support.core.model.SocialUser
import io.wax911.support.facebook.FacebookAuth
import io.wax911.support.google.GoogleAuth
import io.wax911.support.twitter.TwitterAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_content.*

class MainActivity : AppCompatActivity() {

    private val bottomDrawerBehavior: BottomSheetBehavior<FrameLayout> by lazy {
        BottomSheetBehavior.from(bottomDrawer)
    }

    private val authCallback = object : AuthCallback {
        override fun onSuccess(socialUser: SocialUser) {
            Toast.makeText(this@MainActivity, "Logged in successfully", Toast.LENGTH_SHORT).show()
        }

        override fun onError(error: Throwable) {
            Toast.makeText(this@MainActivity, "Error occurred", Toast.LENGTH_SHORT).show()
        }

        override fun onCancel() {
            Toast.makeText(this@MainActivity, "Canceled", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomDrawerBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        bottomAppBar.also { bar: BottomAppBar ->
            bar.setNavigationOnClickListener {
                bottomDrawerBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
            }
            bar.replaceMenu(R.menu.main_menu)
        }
        facebookLoginFab.setOnClickListener {
            FacebookAuth.connectToProvider(this, authCallback)
        }
        twitterLoginFab.setOnClickListener {
            TwitterAuth.connectToProvider(this, authCallback)
        }
        googleLoginFab.setOnClickListener {
            GoogleAuth.connectToProvider(this, authCallback)
        }
    }

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    override fun onBackPressed() {
        if (bottomDrawerBehavior.state != BottomSheetBehavior.STATE_COLLAPSED)
            bottomDrawerBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        super.onBackPressed()
    }
}
