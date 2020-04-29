package com.mi.mvi.utils

import com.mi.mvi.domain.Constants.Companion.INVALID_PAGE

class Constants {

    companion object {

        // Shared Preference Keys

        // -----------------------------UI-----------------------
        const val GALLERY_REQUEST_CODE = 201
        const val PERMISSION_REQUEST_READ_STORAGE: Int = 301

        fun isPaginationDone(errorResponse: String?): Boolean {
            // if error response = '{"detail":"Invalid page."}' then pagination is finished
            return errorResponse?.contains(INVALID_PAGE) ?: false
        }
    }
}
