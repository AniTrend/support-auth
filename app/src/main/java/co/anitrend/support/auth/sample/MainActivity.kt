package co.anitrend.support.auth.sample

import android.os.Bundle
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import co.anitrend.support.auth.core.callback.AuthCallback
import co.anitrend.support.auth.core.model.SocialUser
import co.anitrend.support.auth.facebook.FacebookAuth
import co.anitrend.support.auth.google.GoogleAuth
import co.anitrend.support.auth.sample.databinding.ActivityMainBinding
import co.anitrend.support.auth.twitter.TwitterAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val bottomDrawerBehavior: BottomSheetBehavior<FrameLayout> by lazy {
        BottomSheetBehavior.from(binding.bottomDrawer)
    }

    private val authCallback = object : AuthCallback {
        override fun onSuccess(socialUser: SocialUser?) {
            Toast.makeText(this@MainActivity, "Logged in successfully", Toast.LENGTH_SHORT).show()
        }

        override fun onError(error: Throwable?) {
            Toast.makeText(this@MainActivity, "Error occurred", Toast.LENGTH_SHORT).show()
        }

        override fun onCancel() {
            Toast.makeText(this@MainActivity, "Canceled", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bottomDrawerBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        binding.bottomAppBar.also { bar: BottomAppBar ->
            bar.setNavigationOnClickListener {
                bottomDrawerBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
            }
            bar.replaceMenu(R.menu.main_menu)
        }
        binding.mainContent.facebookLoginFab.setOnClickListener {
            FacebookAuth.connectToProvider(this, authCallback)
        }
        binding.mainContent.twitterLoginFab.setOnClickListener {
            TwitterAuth.connectToProvider(this, authCallback)
        }
        binding.mainContent.googleLoginFab.setOnClickListener {
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
