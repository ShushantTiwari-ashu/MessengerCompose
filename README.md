# MessengerCompose
## Project characteristics and tech-stack

<img src="misc/image/application_anim.gif" width="336" align="right" hspace="20">

This project takes advantage of best practices, many popular libraries and tools in the Android ecosystem. Most of the libraries are in the stable version unless there is a good reason to use non-stable dependency.

* Tech-stack
    * [100% Kotlin](https://kotlinlang.org/) + [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) - perform background operations
    * [Retrofit](https://square.github.io/retrofit/) - networking
    * [Jetpack](https://developer.android.com/jetpack)
        * [Navigation](https://developer.android.com/guide/navigation?gclid=CjwKCAiAqIKNBhAIEiwAu_ZLDpdYw_HHKfeQcnLOi5ihj3jZdOdVBSj6M0KWlaw5592IS2GFiorHWhoCZsQQAvD_BwE&gclsrc=aw.ds) - in-app navigation
        * [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) - notify views about database change
        * [Lifecycle](https://developer.android.com/topic/libraries/architecture/lifecycle) - perform an action when lifecycle state changes
        * [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - store and manage UI-related data in a lifecycle conscious way
        * [Room](https://developer.android.com/jetpack/androidx/releases/room) - store offline cache
    * [HILT](https://developer.android.com/training/dependency-injection/hilt-android) - dependency injection
    * [CoilCompose](https://coil-kt.github.io/coil/compose/) - image loading library
    * [Lottie](http://airbnb.io/lottie) - animation library
    
* Modern Architecture
    * Clean Architecture (at feature module level)
    * Single activity architecture using [Navigation component](https://developer.android.com/guide/navigation?gclid=CjwKCAiAqIKNBhAIEiwAu_ZLDpdYw_HHKfeQcnLOi5ihj3jZdOdVBSj6M0KWlaw5592IS2GFiorHWhoCZsQQAvD_BwE&gclsrc=aw.ds)
    * MVVM + MVI (presentation layer)
    
* UI
    * [Build better apps faster with
Jetpack Compose](https://developer.android.com/jetpack/compose?gclid=CjwKCAiAqIKNBhAIEiwAu_ZLDoMaH7S9jATHeSOyOPS58B1D2mpfCkmQ8U__qs0kUAbvHsE_ySh-IhoCVicQAvD_BwE&gclsrc=aw.ds)
    * Reactive UI
[![Demo MessengerCompose](https://github.com/ShushantTiwari-ashu/MessengerCompose/blob/9dd9f07432e73e6ed80ece49fed614e26e65b501/ezgif.com-gif-maker%20(1).gif)](https://vimeo.com/650207593)
[![Demo MessengerCompose](https://github.com/ShushantTiwari-ashu/MessengerCompose/blob/0d25d2bcadd85b0792dea88ca38aefa387938a7b/ezgif.com-gif-maker%20(2).gif)](https://vimeo.com/650207593)

