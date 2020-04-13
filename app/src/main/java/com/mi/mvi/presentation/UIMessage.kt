package com.mi.mvi.presentation

data class UIMessage(
    val message: String,
    val uiMessageType: UIMessageType
)

sealed class UIMessageType {
    class Toast : UIMessageType()
    class Dialog : UIMessageType()
    class AreYouSureDialog(
        val callBack: AreYouSureCallBack
    ) : UIMessageType()

    class None() : UIMessageType()
}