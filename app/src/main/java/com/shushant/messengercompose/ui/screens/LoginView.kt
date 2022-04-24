package com.shushant.messengercompose.ui.screens

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.shushant.messengercompose.R
import com.shushant.messengercompose.model.UsersData
import com.shushant.messengercompose.network.NetworkState
import com.shushant.messengercompose.network.onLoading
import com.shushant.messengercompose.ui.theme.MessengerComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@Composable
fun LoginView(
    viewModel: LoginViewModel,
    loginActivity: LoginActivity,
    onLoginDOne: (UsersData?) -> Unit
) = MessengerComposeTheme {
    val networkState: NetworkState by viewModel.movieLoadingState
    val context = LocalContext.current

    var email by rememberSaveable { mutableStateOf("") }
    var alreadyHaveAnAccount by rememberSaveable { mutableStateOf(false) }
    var password by rememberSaveable { mutableStateOf("") }
    var fullname by rememberSaveable { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(Color.Transparent)
            .fillMaxSize()
            .clickable { focusManager.clearFocus() }
    ) {
        LoginFields(
            email,
            password,
            fullname,
            alreadyHaveAnAccount,
            onLoginClick = {
                viewModel.login(context, email, password, fullname, loginActivity) {
                    viewModel.getUserByID {
                        onLoginDOne(UsersData())
                    }
                }
            },
            onEmailChange = { email = it },
            onPasswordChange = { password = it },
            onfullnameChange = { fullname = it },
            onAlreadyClicked = { alreadyHaveAnAccount = it },
        )
    }
    networkState.onLoading {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
        ) {

            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
fun LoginFields(
    email: String,
    password: String,
    fullname: String,
    alreadyHaveAnAccount: Boolean,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onfullnameChange: (String) -> Unit,
    onAlreadyClicked: (Boolean) -> Unit,
    onLoginClick: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(25.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painterResource(id = R.drawable.logo),
            contentDescription = "",
            modifier = Modifier.size(80.dp)
        )

        if (alreadyHaveAnAccount) {
            Text("Login")
        } else {
            Text("Registration")
        }

        if (!alreadyHaveAnAccount) {
            OutlinedTextField(
                value = fullname,
                placeholder = { Text(text = "Ashu sharma") },
                label = { Text(text = "Fullname") },
                onValueChange = onfullnameChange,

                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
            )
        }

        OutlinedTextField(
            value = email,
            placeholder = { Text(text = "user@email.com") },
            label = { Text(text = "email") },
            onValueChange = onEmailChange,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
        )

        OutlinedTextField(
            value = password,
            placeholder = { Text(text = "password") },
            label = { Text(text = "password") },
            onValueChange = onPasswordChange,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
        )

        Button(onClick = {
            if (email.isNotBlank() && password.isNotBlank()) {
                onLoginClick(email)
                focusManager.clearFocus()
            } else {
                Toast.makeText(
                    context,
                    "Please enter email and password",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }) {
            if (alreadyHaveAnAccount) {
                Text("Login")
            } else {
                Text("Sign up")
            }
        }

        if (!alreadyHaveAnAccount) {
            Text(
                text = "Already have an account? Login",
                color = Color.Blue,
                modifier = Modifier.clickable {
                    onAlreadyClicked(true)
                })
        } else {
            Text(
                text = "Create an account? Signup",
                color = Color.Blue,
                modifier = Modifier.clickable {
                    onAlreadyClicked(false)
                })
        }
    }
}

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private var alreadyLaunched = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoginView(viewModel = hiltViewModel(), this) {
                if (it != null && !alreadyLaunched) {
                    alreadyLaunched=  alreadyLaunched.not()
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                }
            }
        }
    }
}