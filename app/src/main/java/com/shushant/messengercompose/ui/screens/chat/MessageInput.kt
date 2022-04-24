package com.shushant.messengercompose.ui.screens.chat

import android.annotation.SuppressLint
import android.view.OnReceiveContentListener
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.widget.EditText
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.inputmethod.EditorInfoCompat
import androidx.core.view.inputmethod.InputConnectionCompat
import androidx.core.widget.doAfterTextChanged
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.shushant.messengercompose.R
import com.shushant.messengercompose.extensions.getAvatar
import com.shushant.messengercompose.extensions.writeToFileFromContentUri
import com.shushant.messengercompose.model.Message1
import com.shushant.messengercompose.model.UsersData
import java.io.File
import java.util.*

@Composable
fun MessageInput(
    messageInputViewModel: ChatDetailViewModel,
    device: UsersData?,
    onScroll: () -> Unit,
    onAnimationOpen: () -> Unit
) {
    var inputValue by remember { mutableStateOf("") } // 2
    val user = FirebaseAuth.getInstance().currentUser
    val context = LocalContext.current
    val userID = user?.uid
    val callback =
        InputConnectionCompat.OnCommitContentListener { inputContentInfo, _, _ ->
            try {
                inputContentInfo.requestPermission()
            } catch (e: Exception) {
                return@OnCommitContentListener false
            }
            val filename =
                "${System.currentTimeMillis()}.${inputContentInfo.contentUri.lastPathSegment}"
            val richContentFile = File(context.filesDir, filename)

            val fileWritten = writeToFileFromContentUri(
                richContentFile,
                inputContentInfo.contentUri,
                context = context
            )

            messageInputViewModel.uploadFile(richContentFile, onSuccess = { uri ->
                val message = Message1(
                    date = System.currentTimeMillis() + 500,
                    sentTo = device?.uid ?: "",
                    sentBy = FirebaseAuth.getInstance().currentUser?.uid
                        ?: "",
                    msgId = Random().nextInt().toLong(),
                    message = "Hit like",
                    isLeft = false,
                    chatImage = uri.toString(),
                    userName = device?.name ?: "Shushant tiwari",
                    status = "sent",
                    userImage = device?.name
                        ?: "Shushant tiwari".getAvatar()
                )
                messageInputViewModel.saveMessage(
                    message, onScroll, device?.uid, userID
                )
                inputValue = ""
            })
            true  // return true if succeeded
        }

    fun sendMessage(chatImage: String) { // 3
        val message = Message1(
            date = System.currentTimeMillis() + 500,
            sentTo = device?.uid ?: "",
            sentBy = userID ?: "",
            msgId = Random().nextInt().toLong(),
            message = inputValue,
            isLeft = false,
            chatImage = chatImage.ifEmpty {
                ""
            },
            userName = user?.displayName ?: "Shushant tiwari",
            status = "sent",
            userImage = user?.displayName ?: "Shushant tiwari".getAvatar()
        )
        messageInputViewModel.saveMessage(
            message, onScroll, device?.uid, userID
        )
        inputValue = ""
    }

    fun sendLike() { // 3
        val message = Message1(
            date = System.currentTimeMillis() + 500,
            sentTo = device?.uid ?: "",
            sentBy = userID ?: "",
            msgId = Random().nextInt().toLong(),
            message = "Hit like",
            isLeft = false,
            chatImage = "https://assets1.lottiefiles.com/packages/lf20_x99jHr.json",
            userName = user?.displayName ?: "Shushant tiwari",
            status = "sent",
            userImage = user?.displayName ?: "Shushant tiwari".getAvatar()
        )
        messageInputViewModel.saveMessage(
            message, onScroll, device?.uid, userID
        )
        inputValue = ""
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 20.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Image(
            painterResource(id = R.drawable.ic_collapse_input),
            contentDescription = "",
            modifier = Modifier
                .weight(0.1F)
                .align(Alignment.CenterVertically)
                .clickable { }
        )
        Box(
            modifier = Modifier
                .weight(0.8F)
                .align(Alignment.CenterVertically)
        ) {
            AndroidView(
                factory = { context ->
                    val editText = @SuppressLint("AppCompatCustomView")
                    object : EditText(context) {
                        override fun setOnReceiveContentListener(
                            mimeTypes: Array<out String>?,
                            listener: OnReceiveContentListener?
                        ) {
                            super.setOnReceiveContentListener(mimeTypes, listener)
                        }


                        override fun onCreateInputConnection(editorInfo: EditorInfo): InputConnection {
                            val ic: InputConnection = super.onCreateInputConnection(editorInfo)
                            EditorInfoCompat.setContentMimeTypes(
                                editorInfo,
                                arrayOf("image/gif", "image/png", "image/jpeg", "image/jpg")
                            )

                            return InputConnectionCompat.createWrapper(ic, editorInfo, callback)
                        }
                    }
                    editText
                }, modifier = Modifier
                    .padding(10.dp)
                    .defaultMinSize(minHeight = 20.dp)
                    .fillMaxWidth()
                    .background(color = Color.White, shape = RoundedCornerShape(18))
            ) {
                it.setTextColor(it.context.resources.getColor(R.color.black))
                it.setText(inputValue)
                it.doAfterTextChanged { text ->
                    inputValue = text.toString()
                    it.setSelection(it.length())
                }
                it.background = null
            }
            Image(painter = painterResource(id = R.drawable.ic_emoji),
                contentDescription = "",
                modifier = Modifier
                    .padding(end = 20.dp)
                    .align(
                        Alignment.CenterEnd
                    )
                    .clickable {
                        onAnimationOpen.invoke()
                    })
        }
        /*keyboardOptions = KeyboardOptions(imeAction = ImeAction.O),
        keyboardActions = KeyboardActions {
            sendMessage()
        }*/

        if (inputValue.isBlank()) {
            Icon( // 6
                painterResource(id = R.drawable.ic_like), tint = Color(0XFF0584FE),
                contentDescription = "Send", modifier = Modifier
                    .weight(0.1F)
                    .clickable {
                        sendLike()
                        //openDocument.launch(arrayOf("image/*"))
                    }
                    .align(Alignment.CenterVertically)
            )
        } else {
            Icon( // 6
                imageVector = Icons.Default.Send, tint = Color(0XFF0584FE),
                contentDescription = "Send", modifier = Modifier
                    .weight(0.1F)
                    .clickable {
                        sendMessage("")
                    }
                    .align(Alignment.CenterVertically)
            )
        }
    }
}

