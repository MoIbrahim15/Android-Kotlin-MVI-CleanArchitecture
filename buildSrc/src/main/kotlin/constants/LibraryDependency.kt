object LibraryDependency {
    object Version {
        const val RECYCLER_LIB = "1.2.0-beta01"
        const val CARD_VIEW = "1.0.0"
        const val SWIPE_TO_REFERESH = "1.2.0-alpha01"
        const val CORE_KTX = "1.5.0-beta01"
        const val COLLECTION_KTX = "1.2.0-alpha01"
        const val APPCOMPAT_LIB = "1.3.0-beta01"
        const val MATERIAL_LIB = "1.3.0"
        const val CONSTRAINT = "2.1.0-alpha2"
        const val CROP = "2.8.0"
        const val KOIN = "2.2.2"
        const val ROOM = "2.3.0-beta02"
        const val RETROFIT = "2.9.0"
        const val RETROFIT_INTERCEPTOR = "4.9.1"
        const val COROUTINES = "1.4.2"
        const val LIFECYCLE = "2.3.0"
        const val MATERIAL_DIALOG = "3.3.0"
        const val GLIDE = "4.12.0"
        const val OKHTTP = "4.9.0"
        const val KIEL = "1.2.1"
    }

    const val KOTLIN_STD = "org.jetbrains.kotlin:kotlin-stdlib:${CoreVersion.KOTLIN}"
    const val CORE_KTX = "androidx.core:core-ktx:${Version.CORE_KTX}"
    const val COLLECTION_KTX = "androidx.collection:collection-ktx:${Version.COLLECTION_KTX}"
    const val OKHTTP = "com.squareup.okhttp3:okhttp:${Version.OKHTTP}"

    //UI
    const val APPCOMPAT = "androidx.appcompat:appcompat:${Version.APPCOMPAT_LIB}"
    const val MATERIAL = "com.google.android.material:material:${Version.MATERIAL_LIB}"
    const val RECYCYLER_VIEW = "androidx.recyclerview:recyclerview:${Version.RECYCLER_LIB}"
    const val CARD_VIEW = "androidx.cardview:cardview:${Version.CARD_VIEW}"
    const val SWIPE_TO_REFERESH = "androidx.swiperefreshlayout:swiperefreshlayout:${Version.SWIPE_TO_REFERESH}"
    const val CONSTRAINT = "androidx.constraintlayout:constraintlayout:${Version.CONSTRAINT}"
    const val NAVIGATION_FRAGMENT = "androidx.navigation:navigation-fragment-ktx:${CoreVersion.NAVIGATION}"
    const val NAVIGATION_UI = "androidx.navigation:navigation-ui-ktx:${CoreVersion.NAVIGATION}"
    const val NAVIGATION_RUNTIME = "androidx.navigation:navigation-runtime:${CoreVersion.NAVIGATION}"
    const val CROP = "com.theartofdev.edmodo:android-image-cropper:${Version.CROP}"
    const val MATERIAL_DIALOG = "com.afollestad.material-dialogs:core:${Version.MATERIAL_DIALOG}"
    const val GLIDE = "com.github.bumptech.glide:glide:${Version.GLIDE}"
    const val GLIDE_COMPILAR = "com.github.bumptech.glide:compiler:${Version.GLIDE}"

    //koin
    const val KOIN = "org.koin:koin-android:${Version.KOIN}"
    const val KOIN_VIEWMODEL = "org.koin:koin-androidx-viewmodel:${Version.KOIN}"
    const val KOIN_SCOPE = "org.koin:koin-androidx-scope:${Version.KOIN}"


    //CACHE
    const val ROOM_RUNTIME = "androidx.room:room-runtime:${Version.ROOM}"
    const val ROOM_COMPILER = "androidx.room:room-compiler:${Version.ROOM}"
    const val ROOM_KTX = "androidx.room:room-ktx:${Version.ROOM}"


    //Remote
    const val RETROFIT = "com.squareup.retrofit2:retrofit:${Version.RETROFIT}"
    const val RETROFIT_CONVERTER = "com.squareup.retrofit2:converter-gson:${Version.RETROFIT}"
    const val RETROFIT_INTERCEPTOR = "com.squareup.okhttp3:logging-interceptor:${Version.RETROFIT_INTERCEPTOR}"

    //coroutines
    const val COROUTINES_CORE =
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Version.COROUTINES}"
    const val COROUTINES_ANDROID = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Version.COROUTINES}"

    //lifeCycle
    const val LIVE_DATA_RUNTIME = "androidx.lifecycle:lifecycle-runtime:${Version.LIFECYCLE}"
    const val LIVE_DATA_COMPILER = "androidx.lifecycle:lifecycle-compiler:${Version.LIFECYCLE}"
    const val LIVE_DATA_KTX = "androidx.lifecycle:lifecycle-livedata-ktx:${Version.LIFECYCLE}"

    //kiel
    const val KIEL = "me.ibrahimyilmaz:kiel:${Version.KIEL}"

}