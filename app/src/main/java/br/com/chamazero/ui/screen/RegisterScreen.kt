package br.com.windfyr.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import br.com.windfyr.ui.component.AppButton
import br.com.windfyr.ui.component.AppLogo
import br.com.windfyr.ui.component.AppTextField
import br.com.windfyr.ui.theme.GreenBackground
import br.com.windfyr.ui.theme.GreenPrimary
import br.com.windfyr.ui.theme.GrayText
import br.com.windfyr.ui.viewmodel.AuthViewModel
import br.com.windfyr.util.Resource
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.Box

@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val name by viewModel.name.collectAsState()
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val confirmPassword by viewModel.confirmPassword.collectAsState()
    val registerState by viewModel.registerState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(registerState) {
        when (val state = registerState) {
            is Resource.Success -> {
                viewModel.resetRegisterState()
                onRegisterSuccess()
            }
            is Resource.Error -> {
                scope.launch { snackbarHostState.showSnackbar(state.message) }
                viewModel.resetRegisterState()
            }
            else -> {}
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GreenBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 24.dp)
        ) {
            IconButton(onClick = onNavigateToLogin) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Voltar"
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                AppLogo(size = 48.dp)
                Spacer(modifier = Modifier.size(16.dp))
                Column {
                    Text(
                        text = "Criar sua conta",
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Text(
                        text = "Comece a proteger seus terrenos",
                        style = MaterialTheme.typography.bodyMedium,
                        color = GrayText
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            AppTextField(
                label = "Nome completo",
                value = name,
                onValueChange = viewModel::onNameChange,
                placeholder = "Maria Silva",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )

            Spacer(modifier = Modifier.height(16.dp))

            AppTextField(
                label = "E-mail",
                value = email,
                onValueChange = viewModel::onEmailChange,
                placeholder = "voce@email.com",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            Spacer(modifier = Modifier.height(16.dp))

            AppTextField(
                label = "Senha",
                value = password,
                onValueChange = viewModel::onPasswordChange,
                placeholder = "••••••••",
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = null,
                            tint = GrayText
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            AppTextField(
                label = "Confirmar senha",
                value = confirmPassword,
                onValueChange = viewModel::onConfirmPasswordChange,
                placeholder = "••••••••",
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(
                            imageVector = if (confirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = null,
                            tint = GrayText
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(32.dp))

            AppButton(
                text = "Cadastrar",
                onClick = viewModel::register,
                isLoading = registerState is Resource.Loading
            )

            Spacer(modifier = Modifier.height(32.dp))

            val annotatedText = buildAnnotatedString {
                withStyle(SpanStyle(color = GrayText)) {
                    append("Já possui uma conta? ")
                }
                pushStringAnnotation(tag = "LOGIN", annotation = "login")
                withStyle(SpanStyle(color = GreenPrimary)) {
                    append("Entrar")
                }
                pop()
            }

            ClickableText(
                text = annotatedText,
                onClick = { offset ->
                    annotatedText.getStringAnnotations("LOGIN", offset, offset)
                        .firstOrNull()?.let { onNavigateToLogin() }
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}
