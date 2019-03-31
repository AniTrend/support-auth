package io.wax911.support.google

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.tasks.Task
import io.wax911.support.core.CoreAuthActivity
import io.wax911.support.core.extension.getMetaValue
import io.wax911.support.core.model.AuthCache
import io.wax911.support.core.model.AuthenticationMeta
import io.wax911.support.core.model.SocialUser
import io.wax911.support.core.utils.PreferenceUtils
import kotlinx.coroutines.Runnable


class GoogleAuthActivity : CoreAuthActivity(), GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private var googleApiClient: GoogleApiClient? = null
    private var retrySignIn: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val clientId = getMetaValue(R.string.io_wax911_support_googleWebClientId)

        val gsoBuilder = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestId().requestProfile()
            .requestEmail().requestIdToken(clientId)


        googleApiClient = GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gsoBuilder.build())
                .build()
    }

    override val authenticationMeta: AuthenticationMeta? by lazy {
        AuthCache.instance.googleAuthenticationMeta
    }

    private fun startSignInFlows() {
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient)
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        val error = Throwable(connectionResult.errorMessage)
        onExceptionThrown(error)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode != RC_SIGN_IN) {
            onCancellation()
            return
        }

        val googleSignInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data)

        if (!isGoogleDisconnectRequested(this) && !isGoogleRevokeRequested(this) || retrySignIn) {
            retrySignIn = false
            handleSignInResult(googleSignInAccountTask)
        }
    }

    private fun handleSignInResult(googleSignInAccountTask: Task<GoogleSignInAccount>) {
        try {
            val googleSignInAccount = googleSignInAccountTask.getResult(ApiException::class.java)

            when (googleSignInAccount != null) {
                true -> onSocialSuccess(SocialUser().apply {
                    userId = googleSignInAccount.id
                    accessToken = googleSignInAccount.idToken
                    profilePictureUrl = googleSignInAccount.photoUrl.toString()
                    email = googleSignInAccount.email
                    fullName = googleSignInAccount.displayName
                })
                else -> {
                    Log.e(toString(), "handleSignInResult(googleSignInAccountTask: Task<GoogleSignInAccount>) | " +
                            "googleSignInAccountTask.getResult(ApiException::class.java) returned null")
                    onCancellation()
                }
            }
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(toString(), "signInResult:failed code=" + e.statusCode)
            onExceptionThrown(e)
        }
    }

    override fun onConnected(bundle: Bundle?) {
        val signIn = Runnable {
            retrySignIn = true
            startSignInFlows()
        }

        when {
            isGoogleDisconnectRequested(this) -> handleDisconnectRequest(signIn)
            isGoogleRevokeRequested(this) -> handleRevokeRequest(signIn)
            else -> startSignInFlows()
        }
    }

    override fun onConnectionSuspended(i: Int) {
        onExceptionThrown(Throwable("connection suspended."))
    }

    private fun handleDisconnectRequest(onSignOut: Runnable) {
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback {
            onSignOut.run()
            setGoogleDisconnectRequested(this@GoogleAuthActivity, false)
        }
    }

    private fun handleRevokeRequest(onRevoke: Runnable) {
        Auth.GoogleSignInApi.revokeAccess(googleApiClient).setResultCallback {
            onRevoke.run()
            setGoogleRevokeRequested(this@GoogleAuthActivity, false)
        }
    }

    companion object {

        private val KEY_IS_GOOGLE_DISCONNECT_REQUESTED = GoogleAuth::class.java.name + "KEY_IS_GOOGLE_DISCONNECT_REQUESTED"
        private val KEY_IS_GOOGLE_REVOKE_REQUESTED = GoogleAuth::class.java.name + "KEY_IS_GOOGLE_REVOKE_REQUESTED"
        private const val RC_SIGN_IN = 1000

        fun start(context: Context?) {
            val intent = Intent(context, GoogleAuthActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context?.startActivity(intent)
        }

        internal fun isGoogleDisconnectRequested(context: Context?): Boolean {
            return PreferenceUtils.getBoolean(context, KEY_IS_GOOGLE_DISCONNECT_REQUESTED)
        }

        internal fun setGoogleDisconnectRequested(context: Context?, isRequested: Boolean) {
            PreferenceUtils.saveBoolean(context, KEY_IS_GOOGLE_DISCONNECT_REQUESTED, isRequested)
        }

        internal fun isGoogleRevokeRequested(context: Context?): Boolean {
            return PreferenceUtils.getBoolean(context, KEY_IS_GOOGLE_REVOKE_REQUESTED)
        }

        internal fun setGoogleRevokeRequested(context: Context?, isRequested: Boolean) {
            PreferenceUtils.saveBoolean(context, KEY_IS_GOOGLE_REVOKE_REQUESTED, isRequested)
        }
    }
}
