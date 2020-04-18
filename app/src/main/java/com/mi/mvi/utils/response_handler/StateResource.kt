package com.mi.mvi.utils.response_handler

import com.mi.mvi.presentation.AreYouSureCallBack

data class StateMessage(
    val message: String?,
    val uiComponentType: UIComponentType,
    val messageType : MessageType
)

sealed class UIComponentType {
    object TOAST : UIComponentType()
    object DIALOG : UIComponentType()
    class AreYouSureDialog(
        val callBack: AreYouSureCallBack
    ) : UIComponentType()

    object NONE : UIComponentType()
}

sealed class MessageType {
    object SUCCESS : MessageType()
    object ERROR : MessageType()
    object INFO : MessageType()
    object NONE : MessageType()
}
