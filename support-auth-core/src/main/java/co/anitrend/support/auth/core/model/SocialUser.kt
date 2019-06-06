package co.anitrend.support.auth.core.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SocialUser(var userId: String? = null,
                      var accessToken: String? = null,
                      var secretToken: String? = null,
                      var profilePictureUrl: String? = null,
                      var username: String? = null,
                      var fullName: String? = null,
                      var email: String? = null,
                      var pageLink: String? = null) : Parcelable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        val that = other as SocialUser?

        return if (userId != null) userId == that!!.userId else that!!.userId == null
    }

    override fun hashCode(): Int {
        return if (userId != null) userId!!.hashCode() else 0
    }
}
