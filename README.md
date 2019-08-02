# [ :biohazard: WIP :biohazard: ] Support Authentication Library

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/e5b695e0e61144639591a341f3d42d6d)](https://www.codacy.com/app/AniTrend/support-auth?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=AniTrend/support-auth&amp;utm_campaign=Badge_Grade) &nbsp; [![Build Status](https://travis-ci.org/AniTrend/support-auth.svg?branch=master)](https://travis-ci.org/AniTrend/support-auth)

A easy to use social authentication android library for (Facebook, Google, Twitter) forked from [Simple Auth](https://github.com/jaychang0917/SimpleAuth) to add on or improve life-cycle awareness with [Lifecycle Arch Componets](https://developer.android.com/topic/libraries/architecture/lifecycle) & [Couroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) tweaked to also be life cycle aware as demonstrated with [structured concurrency, lifecycle and coroutine parent-child hierarchy](https://github.com/Kotlin/kotlinx.coroutines/blob/master/ui/coroutines-guide-ui.md#structured-concurrency-lifecycle-and-coroutine-parent-child-hierarchy)

## Usage

Configure the application that imports any of the following sub modules

```
android.defaultConfig.manifestPlaceholders = [
        facebookAppId        : "your facebook app id",
        googleWebClientId    : "your google web client id",
        twitterConsumerKey   : "your twitter consumer key",
        twitterConsumerSecret: "your twitter consumer secret"
]
```


### Callback

Create an interface callback to receive the authentication callback

```kotlin
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
```

### Facebook Authentication

```kotlin
    FacebookAuth.connectToProvider(this, authCallback)
```

### Twitter Authentication

```kotlin
    TwitterAuth.connectToProvider(this, authCallback)
```

### Google Authentication

```kotlin
    GoogleAuth.connectToProvider(this, authCallback)
```

## License

```
Copyright 2018 AniTrend

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```
