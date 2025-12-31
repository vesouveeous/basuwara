package com.dicoding.basuwara.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CopyAll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.dicoding.basuwara.R
import com.dicoding.basuwara.ui.components.login.GradientButton
import com.dicoding.basuwara.ui.components.login.OutlinedEmailTextField
import com.dicoding.basuwara.ui.components.login.OutlinedPasswordTextField
import com.dicoding.basuwara.ui.screen.LoginPage.LoginViewModel
import com.dicoding.basuwara.ui.theme.Visibility
import com.dicoding.basuwara.ui.theme.VisibilityOff
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
fun Login(
    viewModel: LoginViewModel = hiltViewModel(),
    onCreateAccountClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    val clipboardManager = LocalClipboardManager.current
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current

    val scope = rememberCoroutineScope()
    val state = viewModel.loginState.collectAsState(initial = null)
    var isShowSnackBar by remember { mutableStateOf(false) }
    var snackBarMessage by remember { mutableStateOf("") }

    fun showSnackBar(
        text: String
    ) {
        isShowSnackBar = true
        snackBarMessage = text
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(
                color = Color.Transparent,
            )
    ) {


        Box(
            modifier = Modifier
                /*.background(
                    color = MaterialTheme.colorScheme.onPrimary,
                    shape = RoundedCornerShape(25.dp, 5.dp, 25.dp, 5.dp)
                )*/
                .align(Alignment.Center),
        ) {

            Image(
                painter = painterResource(id = R.drawable.user_sign_in),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth(),

                )
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                ,

                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                //.........................Spacer
                Spacer(modifier = Modifier.height(50.dp))

                //.........................Text: title
                Text(
                    text = "Sign In",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 130.dp)
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary,
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedEmailTextField(email){
                    email = it
                }

                Spacer(modifier = Modifier.padding(3.dp))
                OutlinedPasswordTextField(password) {
                    password = it
                }

                val gradientColor = listOf(Color(0xFF484BF1), Color(0xFF673AB7))
                val cornerRadius = 16.dp


                Spacer(modifier = Modifier.padding(10.dp))

                GradientButton(
                    gradientColors = gradientColor,
                    cornerRadius = cornerRadius,
                    nameButton = "Login",
                    onClick = {
                        if (email.isEmpty() || password.isEmpty()) {
                            showSnackBar("Error: Empty Input")
                            CoroutineScope(Dispatchers.Default).launch {
                                delay(3000)
                                isShowSnackBar = false
                            }
                        } else {
                            scope.launch {
                                viewModel.loginUser(email, password)
                            }
                        }
                    }
                )

                Spacer(modifier = Modifier.padding(10.dp))
                TextButton(onClick = { onCreateAccountClick() }) {
                    Text(
                        text = "Create An Account",
                        letterSpacing = 1.sp,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
                
                LaunchedEffect(key1 = state.value?.isSuccess) {
                    if (!state.value?.isSuccess.isNullOrEmpty()) {
                        viewModel.saveSession(state.value?.isSuccess!!)
                        delay(1000)
                        onLoginClick()
                    }
                }
                LaunchedEffect(key1 = state.value?.isError) {
                    if (!state.value?.isError.isNullOrEmpty()){
                        showSnackBar(state.value?.isError!!)
                        delay(3000)
                        isShowSnackBar = false
                    }
                }

            }

            if (state.value?.isLoading == true) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(70.dp)
                )
            }
        }
        if (isShowSnackBar) {
            Snackbar(
                action = {
                    Icon(
                        Icons.Default.CopyAll,
                        modifier = Modifier.clickable {
                            clipboardManager.setText(
                                AnnotatedString(snackBarMessage)
                            )
                            Toast
                                .makeText(context, "Copied!", Toast.LENGTH_SHORT)
                                .show()
                        },
                        contentDescription = "TODO()",
                        tint = Color.Black,
                    )
                },
                modifier = Modifier
                    .padding(8.dp)
                    .testTag("empty_input_error")
                    .align(Alignment.BottomCenter)
            ) {
                Text(
                    text = snackBarMessage,
                    color = Color.White,
                )
            }
        }
    }




}


