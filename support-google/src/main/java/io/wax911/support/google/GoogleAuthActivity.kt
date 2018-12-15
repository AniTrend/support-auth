package io.wax911.support.google

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import com.google.android.gms.auth.GoogleAuthUtil
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Scope
import io.wax911.support.core.extension.getMetaValue
import io.wax911.support.core.model.AuthenticationMeta
import io.wax911.support.core.model.AuthStorage
import io.wax911.support.core.model.SocialUser
import io.wax911.support.core.utils.PreferenceUtils
import io.wax911.support.core.view.DialogFactory
import io.wax911.support.core.CoreAuthActivity
import kotlinx.coroutines.*
import java.util.*


class GoogleAuthActivity : CoreAuthActivity(), GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private var googleApiClient: GoogleApiClient? = null
    private var retrySignIn: Boolean = false

    private val scopes by lazy {
        val scopes = ArrayList<Scope>()
        authenticationMeta?.also {
            for (str in it.scopes)
                scopes.add(Scope(str))
        }
        scopes
    }

    private val accessTokenScope by lazy {
        var scopes = "oauth2:id profile email"
        if (authenticationMeta?.scopes?.isNotEmpty() == true)
            scopes = "oauth2:" + TextUtils.join(" ", authenticationMeta!!.scopes)
        scopes
    }

    private interface AccessTokenListener {
        fun onTokenReady(accessToken: String)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val clientId = getMetaValue(R.string.io_wax911_support_googleWebClientId)

        val gsoBuilder = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestId().requestProfile()
            .requestEmail().requestIdToken(clientId)

        setupScopes(gsoBuilder)

        googleApiClient = GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gsoBuilder.build())
                .build()
    }

    override val authenticationMeta: AuthenticationMeta? by lazy {
        AuthStorage.instance.googleAuthenticationMeta
    }

    private fun startSignInFlows() {
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient)
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun setupScopes(builder: GoogleSignInOptions.Builder) {
        val scopes = scopes
        if (scopes.size == 1) {
            builder.requestScopes(scopes[0])
        } else if (scopes.size > 1) {
            val restScopesArray: Array<Scope?> = scopes.toTypedArray()
            builder.requestScopes(scopes[0], *restScopesArray)
        }
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

        val signInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data)

        if (!isGoogleDisconnectRequested(this) && !isGoogleRevokeRequested(this) || retrySignIn) {
            retrySignIn = false
            handleSignInResult(signInResult)
        }
    }

    private fun handleSignInResult(result: GoogleSignInResult?) {
        if (result == null) {
            onCancellation()
            return
        }

        if (result.isSuccess && result.signInAccount != null) {
            val googleSignInAccount = result.signInAccount
            val user = SocialUser().apply {
                userId = googleSignInAccount?.id
                accessToken = googleSignInAccount?.idToken
                profilePictureUrl = googleSignInAccount?.photoUrl.toString()
                email = googleSignInAccount?.email
                fullName = googleSignInAccount?.displayName
            }

            getAccessToken(googleSignInAccount, object : AccessTokenListener {
                override fun onTokenReady(accessToken: String) {
                    user.accessToken = accessToken
                    onSocialSuccess(user)
                }
            })
        } else {
            val errorMsg = result.status.statusMessage
            if (errorMsg == null) {
                onCancellation()
            } else {
                val error = Throwable(result.status.statusMessage)
                onExceptionThrown(error)
            }
        }
    }

    private fun getAccessToken(account: GoogleSignInAccount?, listener: AccessTokenListener) {
        val loadingDialog = DialogFactory
                .createLoadingDialog(this)
                .apply { show() }

        GlobalScope.async(context = coroutineContext) {
            if (account?.account == null) {
                dismissLoadingDialog(loadingDialog)
                withContext(Dispatchers.Main) {
                    onExceptionThrown(RuntimeException("Account is null"))
                }
            } else {
                dismissLoadingDialog(loadingDialog)
                setGoogleDisconnectRequested(this@GoogleAuthActivity, false)
                setGoogleRevokeRequested(this@GoogleAuthActivity, false)
                val token = GoogleAuthUtil.getToken(applicationContext, account.account, accessTokenScope)
                withContext(Dispatchers.Main) { listener.onTokenReady(token) }
            }
        }.invokeOnCompletion { cause: Throwable? ->
            runBlocking {
                cause?.also {
                    it.printStackTrace()
                    onExceptionThrown(cause)
                }
                dismissLoadingDialog(loadingDialog)
            }
        }
    }

    private suspend fun dismissLoadingDialog(progressDialog: ProgressDialog) = withContext(Dispatchers.Main) {
        progressDialog.dismiss()
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
