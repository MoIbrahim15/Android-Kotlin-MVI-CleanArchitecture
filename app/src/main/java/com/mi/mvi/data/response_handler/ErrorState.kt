package com.mi.mvi.data.response_handler


sealed class ErrorState {
    class TOAST() : ErrorState()
    class SNACKBAR() : ErrorState()
    class DIALOG() : ErrorState()
    class NONE() : ErrorState()
}

