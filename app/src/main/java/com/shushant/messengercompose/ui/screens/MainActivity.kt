package com.shushant.messengercompose.ui.screens

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.shushant.messengercompose.extensions.AssetParamType
import com.shushant.messengercompose.extensions.MyOpenDocumentContract
import com.shushant.messengercompose.extensions.idUser
import com.shushant.messengercompose.model.UsersData
import com.shushant.messengercompose.ui.screens.chat.ChatDetailScreen
import com.shushant.messengercompose.ui.theme.MessengerComposeTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    val viewModel: MainViewModel by viewModels()

    private val openDocument = registerForActivityResult(MyOpenDocumentContract()) { uri ->
        if (uri != null) {
            onImageSelected(uri, idUser)
        }
    }

    @ExperimentalMaterial3Api
    @ExperimentalFoundationApi
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MessengerComposeTheme {
                MainScreen(viewModel, openDocument)
            }
        }
    }

    private fun onImageSelected(uri: Uri, idUser: String) {
        viewModel.sendImage(uri, this, idUser)
    }
}

@ExperimentalMaterial3Api
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun MainScreen(
    viewModel: MainViewModel,
    openDocument: ActivityResultLauncher<Array<String>>,
) {
    val focusManager = LocalFocusManager.current
    focusManager.clearFocus()
    val navController = rememberNavController()
    Navigation(navController = navController, viewModel, openDocument)
}


@ExperimentalMaterial3Api
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun Navigation(
    navController: NavHostController,
    viewModel: MainViewModel,
    openDocument: ActivityResultLauncher<Array<String>>,
) {
    val context = LocalContext.current
    NavHost(
        navController,
        startDestination = NavigationItem.Home.route,
    ) {
        composable(NavigationItem.ChatDetail.route, arguments = listOf(
            navArgument("data") {
                type = AssetParamType()
            }
        )) {
            val device = it.arguments?.getParcelable<UsersData>("data")
            idUser = device?.uid ?: ""
            ChatDetailScreen(viewModel = hiltViewModel(), device, onVideoCallAudioCall = { data ->
                navController.navigate("calling/{${data.first}}/{${data.second}}/{${data.third}}")
            }) {
                navController.navigateUp()
            }
        }

        composable(NavigationItem.Calling.route) {
            CallingScreen(
                it.arguments?.getString("meeting_id"),
                it.arguments?.getString("isVideoCall").toBoolean(),
                it.arguments?.getString("isJoin").toBoolean()
            ){
                navController.navigateUp()
            }
        }
        composable(NavigationItem.Home.route) {
            HomePageScreen(navController = navController, viewModel = viewModel) {
                val user = FirebaseAuth.getInstance().currentUser
                if (user != null) {
                    Firebase.database.reference.child("Users").child(user.uid).child("isOnline")
                        .setValue(false).continueWith {
                            Firebase.database.reference.child("Users").child(user.uid)
                                .child("currentTime").setValue(System.currentTimeMillis())
                        }.continueWith {
                            FirebaseAuth.getInstance().signOut()
                            val intent = Intent(context, LoginActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            context.startActivity(intent)
                            (context as Activity).finish()
                        }
                }

            }
        }
    }
}
