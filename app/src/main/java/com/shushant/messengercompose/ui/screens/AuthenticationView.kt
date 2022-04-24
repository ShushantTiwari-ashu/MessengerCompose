package com.shushant.messengercompose.ui.screens

import android.app.Activity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.shushant.messengercompose.R
import com.shushant.messengercompose.ui.composables.*
import com.shushant.messengercompose.ui.composables.SizedBox
import com.shushant.messengercompose.ui.theme.*
import com.shushant.messengercompose.utils.Utility.showMessage
import kotlinx.coroutines.launch

@ExperimentalAnimationApi
@Composable
fun AuthenticationView(
    formFill: () -> Unit,
    home: () -> Unit,
    authenticationViewModel: AuthenticationViewModel = viewModel()
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = SnackbarHostState()

    val loading: Boolean by authenticationViewModel.loading.observeAsState(initial = false)
    val showMessage: Boolean by authenticationViewModel.showMessage.observeAsState(initial = false)
    val otpSent: Boolean by authenticationViewModel.otpSent.observeAsState(initial = false)
    val message: String by authenticationViewModel.message.observeAsState(initial = "")
    val phoneNumber: String by authenticationViewModel.phoneNumber.observeAsState(initial = "")

    /**
     * Show snackbar
     */
    val showSnackbar = {
        coroutineScope.launch {
            when (snackbarHostState.showSnackbar(
                message = message,
            )) {
                SnackbarResult.Dismissed -> {
                    authenticationViewModel.snackbarDismissed()
                }
                SnackbarResult.ActionPerformed -> {
                    authenticationViewModel.snackbarDismissed()
                }
            }
        }
    }

    if (showMessage) {
        showMessage(message = message)
        showSnackbar()
    }

    MessengerComposeTheme(color = Color(0XFF310BE8)) {
        Surface(color = MaterialTheme.colors.background) {
            Scaffold(
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    Color(0XFF310BE8),
                                    Color(0XFF0027FE),
                                    Color(0XFF8200FF),
                                    Color(0XFFD703FF)
                                )
                            )
                        ),
                ) {
                    Placeholder(alpha = 0.3f, alignment = Alignment.BottomEnd)
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.Center)
                            .padding(horizontal = 15.dp, vertical = 30.dp)
                    ) {
                        Text(
                            stringResource(id = R.string.welcome_to)
                                    + " " +
                                    stringResource(id = R.string.app_name),
                            style = white20Bold
                        )
                        SizedBox(
                            height = 10
                        )
                        Text(
                            stringResource(id = R.string.insert_phone_number),
                            style = gray15
                        )
                        SizedBox(
                            sizeFloat = 0.1f
                        )
                        TextField(
                            value = phoneNumber,
                            textStyle = black20Bold,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFFFFFFF), RoundedCornerShape(16.dp)).padding(start = 8.dp, end = 8.dp),
                            singleLine = true,
                            onValueChange = { authenticationViewModel.onPhoneNumberChange(it) },
                            placeholder = {
                                Text(
                                    stringResource(id = R.string.phone_helper),
                                    style = gray14
                                )
                            },
                            leadingIcon = {
                                Row {
                                    CountryCodePicker(pickedCountry = {
                                        authenticationViewModel.country = it
                                    })
                                    SizedBox(width = 10)
                                }
                            },
                            colors = TextFieldDefaults.textFieldColors(
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent,
                                errorIndicatorColor = Color.Transparent,
                                placeholderColor = Color.White,
                                backgroundColor = Color.White
                            ),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    focusManager.clearFocus()
                                    if (context is Activity) {
                                        authenticationViewModel.sendOTPToPhoneNumber(context)
                                    }
                                }
                            )
                        )
                        OTPComposable(
                            home = home,
                            formFill = formFill
                        )
                        SizedBox(height = 50)
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(50),
                            onClick = {
                                focusManager.clearFocus()
                                if (context is Activity) {
                                    if (otpSent) {
                                        authenticationViewModel.verifyOTP(context, home, formFill)
                                    } else {
                                        authenticationViewModel.sendOTPToPhoneNumber(context)
                                    }
                                }
                            }
                        ) {
                            Text(
                                if (otpSent)
                                    stringResource(id = R.string.verify).uppercase()
                                else
                                    stringResource(id = R.string.continue_button).uppercase(),
                                style = white20Bold,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }
                    if (loading) {
                        CircularIndicatorMessage(
                            message = stringResource(id = R.string.please_wait)
                        )
                    }
                }
                Snackbar(snackbarHostState)
            }
        }
    }
}