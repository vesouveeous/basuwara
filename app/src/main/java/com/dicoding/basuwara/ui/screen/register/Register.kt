package com.dicoding.basuwara.ui.screen.register


import android.util.Patterns
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dicoding.basuwara.R
import com.dicoding.basuwara.data.model.AlertDialogModel
import com.dicoding.basuwara.data.model.UserModel
import com.dicoding.basuwara.ui.components.registerComponents.RegisterEmail
import com.dicoding.basuwara.ui.components.registerComponents.RegisterName
import com.dicoding.basuwara.ui.components.registerComponents.RegisterPassword
import com.dicoding.basuwara.ui.components.registerComponents.RegisterPasswordConfirm
import com.dicoding.basuwara.ui.components.registerComponents.RegisterPhone
import com.dicoding.basuwara.ui.components.registerComponents.RegisterSignupButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.Dispatcher

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Register(
    viewModel: RegisterViewModel = hiltViewModel(),
    onSignInClick: () -> Unit,
) {
    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    var isRegistrationSuccess by remember { mutableStateOf(false) }
    var isAlertDialogShown by remember { mutableStateOf(false) }
    var isValidEmail by remember(email) {
        mutableStateOf(Patterns.EMAIL_ADDRESS.matcher(email).matches())
    }
    var alertDialogState by remember { mutableStateOf(AlertDialogModel("", "", "", {}, {})) }

    val localContext = LocalContext.current
    var state = viewModel.registerState.collectAsState(initial = null)

    fun showAlertDialog(alertDialogData: AlertDialogModel) {
        alertDialogState = alertDialogData
        isAlertDialogShown = true
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(
                color = Color.Transparent,
            )
    ) {
        if (isRegistrationSuccess) {
            Snackbar(
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                Text(
                    text = "Account successfully registered!"
                )
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.Center),
        ) {
            AnimatedVisibility(
                isAlertDialogShown
            ) {
                AlertDialog(
                    onDismissRequest = { isAlertDialogShown = false },
                    title = {
                        Text(
                            text = alertDialogState.title,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    text = {
                        Text(
                            text = alertDialogState.message,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    confirmButton = {
                        TextButton(onClick = { isAlertDialogShown = false }) {
                            Text("OK", fontWeight = FontWeight.SemiBold)
                        }
                    },
                    modifier = Modifier
                        .padding(16.dp)
                        .testTag("empty_field_alert"),
                    shape = RoundedCornerShape(16.dp),
                    containerColor = colorScheme.surface,
                    titleContentColor = colorScheme.onSurface,
                    textContentColor = colorScheme.onSurface
                )
            }
            Image(
                painter = painterResource(id = R.drawable.register),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .height(180.dp)
                    .fillMaxWidth(),

                )
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),

                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                //.........................Spacer
                Spacer(modifier = Modifier.height(30.dp))

                //.........................Text: title
                Text(
                    text = "Sign Up",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 130.dp)
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.headlineSmall,
                    color = colorScheme.primary,
                )
                Spacer(modifier = Modifier.height(8.dp))
                RegisterName(
                    name = name,
                    modifier = Modifier.testTag("register_name"),
                    onChange = {
                        name = it
                    }
                )

                Spacer(modifier = Modifier.padding(3.dp))

                RegisterPhone(
                    phone = phone,
                    modifier = Modifier.testTag("register_phone"),
                    onChange = {
                        phone = it
                    }
                )
                Spacer(modifier = Modifier.padding(3.dp))

                RegisterEmail(
                    email = email,
                    isValid = isValidEmail,
                    modifier = Modifier.testTag("register_email"),
                    onChange = {
                        email = it
                    }
                )

                Spacer(modifier = Modifier.padding(3.dp))

                RegisterPassword(
                    password = password,
                    modifier = Modifier.testTag("register_password"),
                    onChange = {
                        password = it
                    }
                )

                Spacer(modifier = Modifier.padding(3.dp))

                RegisterPasswordConfirm(
                    confirmPassword = confirmPassword,
                    modifier = Modifier.testTag("register_confirm_password"),
                    onChange = {
                        confirmPassword = it
                    }
                )


                val gradientColor = listOf(Color(0xFF484BF1), Color(0xFF673AB7))
                val cornerRadius = 16.dp


                Spacer(modifier = Modifier.padding(10.dp))

                RegisterSignupButton(
                    gradientColors = gradientColor,
                    cornerRadius = cornerRadius,
                    text = "Sign Up",
                    onClick = {
                        if (password != confirmPassword) {
                            showAlertDialog(
                                AlertDialogModel(
                                    "Attention",
                                    "Password and confirm password do not match",
                                    "OK",
                                    {},
                                    {})
                            )
                            return@RegisterSignupButton
                        } else if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
                            showAlertDialog(
                                AlertDialogModel(
                                    title = "Attention",
                                    message = "Please fill all the fields",
                                    confirmButtonText = "OK",
                                    onConfirmClick = { },
                                    onDismissClick = { }
                                )
                            )
                        } else if (!isValidEmail) {
                            showAlertDialog(
                                AlertDialogModel(
                                    title = "Attention",
                                    message = "Email is not valid",
                                    confirmButtonText = "OK",
                                    onConfirmClick = { },
                                    onDismissClick = { }
                                )
                            )
                        } else if (password.length <6) {
                            showAlertDialog(
                                AlertDialogModel(
                                    title = "Attention",
                                    message = "Password must be at least 6 characters",
                                    confirmButtonText = "OK",
                                    onConfirmClick = { },
                                    onDismissClick = { }
                                )
                            )
                        }
                        else {
                            val user = UserModel(
                                name = name,
                                email = email,
                                password = password,
                                phone = phone
                            )
                            viewModel.registerUser(user)
                        }
                    },
                    modifier = Modifier.testTag("register_button")

                )

                Spacer(modifier = Modifier.padding(10.dp))
                TextButton(onClick = { onSignInClick() }) {
                    Text(
                        text = "Sign In",
                        letterSpacing = 1.sp,
                        style = MaterialTheme.typography.labelLarge
                    )
                }

                LaunchedEffect(key1 = state.value?.isError) {
                    if (!state.value?.isError.isNullOrEmpty()) {
                        showAlertDialog(
                            AlertDialogModel(
                                title = "Attention",
                                message = "The email address is already in use by another account.",
                                confirmButtonText = "OK",
                                onConfirmClick = { },
                                onDismissClick = { }
                            )
                        )
                    }
                }
                LaunchedEffect(key1 = state.value?.isSuccess) {
                    if (state.value?.isSuccess == true) {
                        isRegistrationSuccess = true
                        delay(2000)
                        isRegistrationSuccess = false
                        delay(500)
                        onSignInClick()
                    }
                }

            }

        }
        if (state.value?.isLoading == true) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center).testTag("loading_indicator")
            )
        }
    }
}