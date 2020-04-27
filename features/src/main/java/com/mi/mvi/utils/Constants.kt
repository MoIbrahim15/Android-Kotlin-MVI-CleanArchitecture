package com.mi.mvi.utils

import com.mi.mvi.domain.Constants.Companion.INVALID_PAGE

class Constants {

    companion object {

        // -----------------------------SHARED_PREF-----------------------
        // Shared Preference Files:

        // Shared Preference Keys
        const val PREVIOUS_AUTH_USER: String = "com.mi.mvi.PREVIOUS_AUTH_USER"

        const val BLOG_FILTER: String = "com.mi.mvi.BLOG_FILTER"
        const val BLOG_ORDER: String = "com.mi.mvi.BLOG_ORDER"

        // -----------------------------UI-----------------------
        const val GALLERY_REQUEST_CODE = 201
        const val PERMISSION_REQUEST_READ_STORAGE: Int = 301



        fun isPaginationDone(errorResponse: String?): Boolean {
            // if error response = '{"detail":"Invalid page."}' then pagination is finished
            return errorResponse?.contains(INVALID_PAGE) ?: false
        }
    }
}
