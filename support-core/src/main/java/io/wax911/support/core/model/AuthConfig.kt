package io.wax911.support.core.model

data class AuthConfig(
  val facebookAppId: String = "",
  val googleWebClientId: String = "",
  val twitterConsumerKey: String = "",
  val twitterConsumerSecret: String = "",
  val instagramClientId: String = "",
  val instagramClientSecret: String = "",
  val instagramRedirectUrl: String = ""
)