package com.shushant.chatties.extensions

import android.content.res.Resources
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import java.util.*
import kotlin.reflect.KClass
import android.content.Context
import android.content.res.Configuration
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Patterns
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.constraintlayout.compose.ConstraintLayout
import com.google.android.gms.tasks.Task
import com.shushant.chatties.R
import com.shushant.chatties.ui.theme.Typography
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resumeWithException

//Don't touch this. It's behind your reasoning!
@Suppress("UNCHECKED_CAST", "EXPERIMENTAL_API_USAGE")
suspend fun <T> Task<T>.await(): T {
    if (isComplete) {
        val e = exception
        return if (e == null) {
            if (isCanceled) throw CancellationException("Task $this was cancelled normally.")
            else result as T
        } else {
            throw e
        }
    }

    return suspendCancellableCoroutine { cont ->
        addOnCompleteListener {
            val e = exception
            if (e == null) {
                if (isCanceled) cont.cancel() else cont.resume(result as T) {}
            } else {
                cont.resumeWithException(e)
            }
        }
    }
}

fun Context.validPasswordOrThrow(password: String) {
    if (password.length < 6) throw Exception(getString(R.string.password_should_be_at_least_6_characters))
}

fun Context.validEmailOrThrow(email: String) {
    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        throw Exception(getString(R.string.not_a_valid_email))
}

//region
//note: this validates Egyptian numbers only.
//fun Context.validPhoneNumberOrThrow(phoneNumber: String) {
//    //starts with '+20', followed by 1, followed by 0 for 010 or 1 for 011 or 2 for 012 or 5 for 015, then the remaining 8 digits.
//    if (!Pattern.matches(
//            "\\+20[1][0125]\\d{8}",
//            phoneNumber
//        )
//    ) throw Exception(getString(R.string.not_a_valid_number))
//}
//endregion

@RequiresApi(Build.VERSION_CODES.M)
private fun ConnectivityManager.isConnected(): Boolean {
    val capabilities = getNetworkCapabilities(activeNetwork) ?: return false
    val wifiConnected = capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
    val mobileDataActive = capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
    val ethernetConnected = capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
    return wifiConnected || mobileDataActive || ethernetConnected
}

@RequiresApi(Build.VERSION_CODES.M)
fun Context.isConnected(): Boolean {
    return (this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).isConnected()
}

@RequiresApi(Build.VERSION_CODES.M)
fun Context.connectedOrThrow() {
    if (!isConnected()) throw Exception(getString(R.string.you_are_offline))
}

fun Context.isSystemDarkMode(): Boolean {
    return (resources.configuration.uiMode + Configuration.UI_MODE_NIGHT_MASK) == UI_MODE_NIGHT_YES
}

const val MESSAGES = "messages"
const val MESSAGE = "message"
const val SENT_BY = "sent_by"
const val SENT_ON = "sent_on"
const val IS_CURRENT_USER = "is_current_user"
const val MESSAGES_CHILD = "messages"
const val USERS = "Users"
const val ANONYMOUS = "anonymous"
private const val LOADING_IMAGE_URL = "https://www.google.com/images/spin-32.gif"
var idUser:String=""

fun <T : Any> mapToObject(map: Map<String, Any>, clazz: KClass<T>) : T {
    //Get default constructor
    val constructor = clazz.constructors.first()

    //Map constructor parameters to map values
    val args = constructor
        .parameters
        .map {
            it to map.get(it.name)
        }
        .toMap()

    //return object from constructor call
    return constructor.callBy(args)
}

fun getRandomString(length: Int): String {
    val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
    return (1..length)
        .map { allowedChars.random() }
        .joinToString("")
}

fun getRandomBoolean(): Boolean {
    val random = Random()
    return random.nextBoolean()
}


fun Float.dp(): Float = this * density + 0.5f

val density: Float
    get() = Resources.getSystem().displayMetrics.density

@Composable
fun SnackbarDemo(text: String) {
    val scaffoldState = rememberScaffoldState() // this contains the `SnackbarHostState`
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier,
        scaffoldState = scaffoldState // attaching `scaffoldState` to the `Scaffold`
    ) {
        Button(
            onClick = {
                coroutineScope.launch { // using the `coroutineScope` to `launch` showing the snackbar
                    // taking the `snackbarHostState` from the attached `scaffoldState`
                    val snackbarResult = scaffoldState.snackbarHostState.showSnackbar(
                        message = text,
                        actionLabel = "Do something."
                    )
                    when (snackbarResult) {
                        SnackbarResult.Dismissed -> Log.d("SnackbarDemo", "Dismissed")
                        SnackbarResult.ActionPerformed -> Log.d(
                            "SnackbarDemo",
                            "Snackbar's button clicked"
                        )
                    }
                }
            }
        ) {
            Text(text = "A button that shows a Snackbar")
        }
    }
}

@Composable
fun SearchComposable(state: MutableState<TextFieldValue>) {
    TextField(
        value = state.value,
        onValueChange = { value ->
            state.value = value
        },
        textStyle = TextStyle(fontSize = 17.sp),
        leadingIcon = { Icon(Icons.Filled.Search, null, tint = Color(0XFF8E8E93)) },
        trailingIcon = {
            if (state.value != TextFieldValue("")) {
                IconButton(
                    onClick = {
                        state.value =
                            TextFieldValue("") // Remove text from TextField when you press the 'X' icon
                    }
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "", tint = Color(0XFF8E8E93),
                        modifier = Modifier
                            .padding(15.dp)
                            .size(24.dp)
                    )
                }
            }
        },
        modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth()
            .background(Color(0xFFE7F1F1), RoundedCornerShape(16.dp)),
        placeholder = { Text(text = "Search") },
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            backgroundColor = Color.Transparent,
            cursorColor = Color.DarkGray
        )
    )
}

fun String.getAvatar() = "https://ui-avatars.com/api/?name=$this&background=102895&color=fff"
@Composable
fun Snackbar(snackbarHostState: SnackbarHostState) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val (snackbarHostRef) = createRefs()

        SnackbarHost(
            modifier = Modifier
                .constrainAs(snackbarHostRef) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                },
            hostState = snackbarHostState,
            snackbar = {
                Snackbar(
                    backgroundColor = Color.White,
                    action = {
                        Text(
                            text = "Okay",
                            modifier = Modifier
                                .padding(16.dp)
                                .clickable {
                                    snackbarHostState.currentSnackbarData?.dismiss()
                                },
                            style = Typography.button
                        )
                    },
                    modifier = Modifier.padding(15.dp)
                ) {
                    Text(
                        text =
                        if ((snackbarHostState.currentSnackbarData?.message ?: "").isEmpty())
                            "Something went wrong.."
                        else
                            snackbarHostState.currentSnackbarData!!.message,
                        style = Typography.subtitle1
                    )
                }
            },
        )
    }
}

@Composable
fun InputField(
    modifier: Modifier = Modifier,
    valueState: MutableState<String>,
    labelId: Int,
    enabled: Boolean,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    onAction: KeyboardActions = KeyboardActions.Default
) {
    OutlinedTextField(
        value = valueState.value,
        onValueChange = { valueState.value = it },
        label = { Text(text = stringResource(id = labelId)) },
        singleLine = true,
        textStyle = TextStyle(fontSize = 18.sp, color = MaterialTheme.colors.onBackground),
        modifier = modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        enabled = enabled,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
        keyboardActions = onAction
    )
}

@Composable
fun PasswordInput(
    modifier: Modifier = Modifier,
    passwordState: MutableState<String>,
    labelId: Int = R.string.password,
    enabled: Boolean = true,
    passwordVisibility: MutableState<Boolean>,
    imeAction: ImeAction = ImeAction.Done,
    onAction: KeyboardActions = KeyboardActions.Default
) {
    val visualTransformation =
        if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation()

    OutlinedTextField(
        value = passwordState.value,
        onValueChange = { passwordState.value = it },
        label = { Text(text = stringResource(id = labelId)) },
        singleLine = true,
        textStyle = TextStyle(fontSize = 18.sp, color = MaterialTheme.colors.onBackground),
        modifier = modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        enabled = enabled,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = imeAction
        ),
        visualTransformation = visualTransformation,
        trailingIcon = { PasswordVisibility(passwordVisibility = passwordVisibility) },
        keyboardActions = onAction
    )
}

@Composable
fun PasswordVisibility(passwordVisibility: MutableState<Boolean>) {
    val visible = passwordVisibility.value
    val description =
        stringResource(id = if (visible) R.string.hide_password else R.string.show_password)
    val icon =
        painterResource(id = if (visible) R.drawable.ic_visibility_on else R.drawable.ic_visibility_off)

    IconButton(onClick = { passwordVisibility.value = !visible }) {
        Icon(painter = icon, contentDescription = description)
    }
}

@Composable
fun EmailInput(
    modifier: Modifier = Modifier,
    emailState: MutableState<String>,
    labelId: Int = R.string.email,
    enabled: Boolean = true,
    imeAction: ImeAction = ImeAction.Next,
    onAction: KeyboardActions = KeyboardActions.Default
) {
    InputField(
        modifier = modifier,
        valueState = emailState,
        labelId = labelId,
        enabled = enabled,
        keyboardType = KeyboardType.Email,
        imeAction = imeAction,
        onAction = onAction
    )
}

@Composable
fun PhoneNumberInput(
    modifier: Modifier = Modifier,
    numberState: MutableState<String>,
    labelId: Int = R.string.phone_number,
    enabled: Boolean = true,
    imeAction: ImeAction = ImeAction.Done,
    onAction: KeyboardActions = KeyboardActions.Default
) {
    InputField(
        modifier = modifier,
        valueState = numberState,
        labelId = labelId,
        enabled = enabled,
        keyboardType = KeyboardType.Phone,
        imeAction = imeAction,
        onAction = onAction
    )
}

@Composable
fun Header(textId: Int) {
    Text(
        text = stringResource(id = textId),
        modifier = Modifier.padding(vertical = 50.dp, horizontal = 15.dp),
        fontSize = 26.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colors.onBackground
    )
}

@Composable
fun SubmitButton(
    textId: Int,
    loading: Boolean,
    validInputs: Boolean,
    onClick: () -> Unit
) {
    Button(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
        enabled = !loading && validInputs,
        onClick = onClick
    ) {
        if (loading) CircularProgressIndicator(modifier = Modifier.size(25.dp))
        else Text(text = stringResource(id = textId), modifier = Modifier.padding(5.dp))
    }
}