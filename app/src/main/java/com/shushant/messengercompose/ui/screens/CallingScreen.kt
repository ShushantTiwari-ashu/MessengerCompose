package com.shushant.messengercompose.ui.screens

import android.Manifest
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.isGone
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.shushant.messengercompose.videocall.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.webrtc.*
import timber.log.Timber

private const val CAMERA_PERMISSION = Manifest.permission.CAMERA
private const val AUDIO_PERMISSION = Manifest.permission.RECORD_AUDIO

@OptIn(ExperimentalPermissionsApi::class, ExperimentalCoroutinesApi::class)
@Composable
fun CallingScreen(
    meetingID: String?,
    isVideoCall: Boolean?,
    isJoin: Boolean?,
    onBackPressed:()->Unit) {
    val context = LocalContext.current
    val audioManager = remember {
        mutableStateOf(lazy { RTCAudioManager.create(context) })
    }
    audioManager.value.value.selectAudioDevice(RTCAudioManager.AudioDevice.SPEAKER_PHONE)
    var isJoins = false

    val isMute = remember {
        mutableStateOf(false)
    }

    val isVideoPaused = remember {
        mutableStateOf(false)
    }

    val inSpeakerMode = remember {
        mutableStateOf(true)
    }
    var rtcClient: RTCClient? = null
    var signallingClient: SignalingClient? = null

    val remoteView = remember {
        mutableStateOf(SurfaceViewRenderer(context))
    }
    val localView = remember {
        mutableStateOf(SurfaceViewRenderer(context))
    }

    val endCallClick = remember {
        mutableStateOf(false)
    }

    val multiplePermissionsState = rememberMultiplePermissionsState(
        listOf(
            CAMERA_PERMISSION,
            AUDIO_PERMISSION,
        )
    )
    if (multiplePermissionsState.allPermissionsGranted && rtcClient == null) {
        rtcClient = RTCClient(
            context = context,
            object : PeerConnectionObserver() {
                override fun onIceCandidate(p0: IceCandidate?) {
                    super.onIceCandidate(p0)
                    if (isJoin != null) {
                        signallingClient?.sendIceCandidate(p0, isJoin)
                    }
                    rtcClient?.addIceCandidate(p0)
                }

                override fun onAddStream(p0: MediaStream?) {
                    super.onAddStream(p0)
                    Timber.e("onAddStream: $p0")
                    p0?.videoTracks?.get(0)?.addSink(remoteView.value)
                }

                override fun onIceConnectionChange(p0: PeerConnection.IceConnectionState?) {
                    Timber.e("onIceConnectionChange: $p0")
                }

                override fun onIceConnectionReceivingChange(p0: Boolean) {
                    Timber.e("onIceConnectionReceivingChange: $p0")
                }

                override fun onConnectionChange(newState: PeerConnection.PeerConnectionState?) {
                    Timber.e("onConnectionChange: $newState")
                }

                override fun onDataChannel(p0: DataChannel?) {
                    Timber.e("onDataChannel: $p0")
                }

                override fun onStandardizedIceConnectionChange(newState: PeerConnection.IceConnectionState?) {
                    Timber.e("onStandardizedIceConnectionChange: $newState")
                }

                override fun onAddTrack(p0: RtpReceiver?, p1: Array<out MediaStream>?) {
                    Timber.e("onAddTrack: $p0 \n $p1")
                }

                override fun onTrack(transceiver: RtpTransceiver?) {
                    Timber.e("onTrack: $transceiver")
                }
            }
        )
        Surface(color = MaterialTheme.colors.background) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
            ) {
                Box(
                    contentAlignment = Alignment.TopStart,
                    modifier = Modifier.fillMaxSize()
                ) {
                    AndroidView(factory = {
                        remoteView.value
                    }, modifier = Modifier.fillMaxSize())
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "",
                        tint = Color.White, modifier = Modifier
                            .padding(10.dp)
                            .wrapContentSize().clickable {
                                if (meetingID != null) {
                                    rtcClient.endCall(meetingID)
                                }
                                remoteView.value.isGone = false
                                Constants.isCallEnded = true
                                if (signallingClient != null) {
                                    signallingClient?.destroy()
                                }
                                onBackPressed.invoke()
                            }
                    )
                    Box(modifier = Modifier.align(Alignment.BottomStart)) {
                        Column(
                            verticalArrangement = Arrangement.SpaceAround,
                            horizontalAlignment = Alignment.Start,
                        ) {
                            AndroidView(
                                factory = {
                                    localView.value
                                },
                                modifier = Modifier
                                    .size(120.dp, 150.dp)
                                    .shadow(4.dp, shape = RoundedCornerShape(8.dp))
                                    .background(Color.Gray)
                                    .padding(4.dp)
                            )

                            Box(
                                modifier = Modifier
                                    .background(
                                        color = Color.Transparent,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .fillMaxWidth()
                                    .shadow(elevation = 2.dp)
                                    .padding(top = 4.dp)
                                    .border(BorderStroke(2.dp, color = Color(0XFF018786)))
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .padding(10.dp)
                                        .fillMaxWidth()
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.KeyboardVoice,
                                        contentDescription = "",
                                        Modifier
                                            .background(color = Color.White, shape = CircleShape)
                                            .padding(4.dp)
                                            .clickable {
                                                isMute.value = !isMute.value
                                                rtcClient.enableAudio(isMute.value)
                                            }
                                    )
                                    Icon(
                                        imageVector = Icons.Default.Videocam,
                                        contentDescription = "",
                                        Modifier
                                            .background(color = Color.White, shape = CircleShape)
                                            .padding(4.dp)
                                            .clickable {
                                                isVideoPaused.value = !isVideoPaused.value
                                                rtcClient.enableVideo(isVideoPaused.value)
                                            }
                                    )
                                    Icon(
                                        imageVector = Icons.Default.CallEnd,
                                        contentDescription = "",
                                        Modifier
                                            .background(color = Color.Red, shape = CircleShape)
                                            .padding(4.dp)
                                            .clickable {
                                                if (meetingID != null) {
                                                    rtcClient.endCall(meetingID)
                                                }
                                                remoteView.value.isGone = false
                                                Constants.isCallEnded = true
                                                signallingClient?.destroy()
                                                onBackPressed.invoke()
                                            }
                                    )
                                    Icon(
                                        imageVector = Icons.Default.Cameraswitch,
                                        contentDescription = "",
                                        Modifier
                                            .background(color = Color.White, shape = CircleShape)
                                            .padding(4.dp)
                                            .clickable {
                                                rtcClient.switchCamera()
                                            }
                                    )
                                    Icon(
                                        imageVector = Icons.Default.Speaker,
                                        contentDescription = "",
                                        Modifier
                                            .background(color = Color.White, shape = CircleShape)
                                            .padding(4.dp)
                                            .clickable {
                                                if (inSpeakerMode.value) {
                                                    inSpeakerMode.value = false
                                                    //audio_output_button.setImageResource(R.drawable.ic_baseline_hearing_24)
                                                    audioManager.value.value.setDefaultAudioDevice(
                                                        RTCAudioManager.AudioDevice.EARPIECE
                                                    )
                                                } else {
                                                    inSpeakerMode.value = true
                                                    //audio_output_button.setImageResource(R.drawable.ic_baseline_speaker_up_24)
                                                    audioManager.value.value.setDefaultAudioDevice(
                                                        RTCAudioManager.AudioDevice.SPEAKER_PHONE
                                                    )
                                                }
                                            }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        rtcClient.initSurfaceView(remoteView.value)
        rtcClient.initSurfaceView(localView.value)
        rtcClient.startLocalVideoCapture(localView.value)
        signallingClient = SignalingClient(
            meetingID ?: "",
            createSignallingClientListener(rtcClient, meetingID ?: "", endCallClick)
        )
        if (!isJoin!!)
            rtcClient.call(sdpObserver, meetingID ?: "")

    } else {
        Column {
            Text(
                getTextToShowGivenPermissions(
                    multiplePermissionsState.revokedPermissions,
                    multiplePermissionsState.shouldShowRationale
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { multiplePermissionsState.launchMultiplePermissionRequest() }) {
                Text("Request permissions")
            }
        }
    }

}

private fun createSignallingClientListener(
    rtcClient: RTCClient,
    meetingID: String,
    endCallClick: MutableState<Boolean>
) = object : SignalingClientListener {
    override fun onConnectionEstablished() {
        endCallClick.value = true
    }

    override fun onOfferReceived(description: SessionDescription) {
        rtcClient.onRemoteSessionReceived(description)
        Constants.isInitiatedNow = false
        rtcClient.answer(sdpObserver, meetingID)
        //remote_view_loading.isGone = true
    }

    override fun onAnswerReceived(description: SessionDescription) {
        rtcClient.onRemoteSessionReceived(description)
        Constants.isInitiatedNow = false
        //remote_view_loading.isGone = true
    }

    override fun onIceCandidateReceived(iceCandidate: IceCandidate) {
        rtcClient.addIceCandidate(iceCandidate)
    }

    override fun onCallEnded() {
        if (!Constants.isCallEnded) {
            Constants.isCallEnded = true
            rtcClient.endCall(meetingID)
            /*  finish()
              startActivity(Intent(this@RTCActivity, MainActivity::class.java))*/
        }
    }
}


@OptIn(ExperimentalPermissionsApi::class)
private fun getTextToShowGivenPermissions(
    permissions: List<PermissionState>,
    shouldShowRationale: Boolean
): String {
    val revokedPermissionsSize = permissions.size
    if (revokedPermissionsSize == 0) return ""

    val textToShow = StringBuilder().apply {
        append("The ")
    }

    for (i in permissions.indices) {
        textToShow.append(permissions[i].permission)
        when {
            revokedPermissionsSize > 1 && i == revokedPermissionsSize - 2 -> {
                textToShow.append(", and ")
            }
            i == revokedPermissionsSize - 1 -> {
                textToShow.append(" ")
            }
            else -> {
                textToShow.append(", ")
            }
        }
    }
    textToShow.append(if (revokedPermissionsSize == 1) "permission is" else "permissions are")
    textToShow.append(
        if (shouldShowRationale) {
            " important. Please grant all of them for the app to function properly."
        } else {
            " denied. The app cannot function without them."
        }
    )
    return textToShow.toString()
}

private val sdpObserver = object : AppSdpObserver() {
    override fun onCreateSuccess(p0: SessionDescription?) {
        super.onCreateSuccess(p0)

    }
}

