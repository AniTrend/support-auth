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
GNU GENERAL PUBLIC LICENSE Version 3

Copyright (C) 2018 AniTrend

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.
```
