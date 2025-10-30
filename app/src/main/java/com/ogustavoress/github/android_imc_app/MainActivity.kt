package com.ogustavoress.github.android_imc_app

import java.util.Locale
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ogustavoress.github.android_imc_app.ui.theme.AndroidimcappTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidimcappTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ImcScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun ImcScreen(modifier: Modifier = Modifier) {

    val peso = remember { mutableStateOf("") }
    val altura = remember { mutableStateOf("") }
    val imc = remember { mutableDoubleStateOf(0.0) }
    val statusImc = remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp), // aproxima o header da câmera
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // HEADER
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colorResource(id = R.color.vermelho_fiap))
                    .padding(vertical = 24.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.bmi),
                    contentDescription = "logo",
                    modifier = Modifier.size(80.dp)
                )
                Text(
                    text = "Calculadora IMC",
                    fontSize = 22.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // FORMULÁRIO
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xfff9f6f6)),
                elevation = CardDefaults.cardElevation(6.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Seus dados",
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.black),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Campo PESO
                    Text(
                        text = "Seu peso (kg)",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp),
                        fontSize = 12.sp,
                        color = colorResource(id = R.color.black)
                    )
                    OutlinedTextField(
                        value = peso.value,
                        onValueChange = { peso.value = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = colorResource(id = R.color.vermelho_fiap),
                            focusedBorderColor = colorResource(id = R.color.vermelho_fiap)
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Campo ALTURA
                    Text(
                        text = "Sua altura (m)",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp),
                        fontSize = 12.sp,
                        color = colorResource(id = R.color.black)
                    )
                    OutlinedTextField(
                        value = altura.value,
                        onValueChange = { altura.value = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = colorResource(id = R.color.vermelho_fiap),
                            focusedBorderColor = colorResource(id = R.color.vermelho_fiap)
                        )
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // BOTÃO CALCULAR
                    Button(
                        onClick = {
                            val pesoValor = peso.value.replace(",", ".").toDoubleOrNull()
                            val alturaValor = altura.value.replace(",", ".").toDoubleOrNull()

                            if (pesoValor != null && alturaValor != null && alturaValor > 0) {
                                imc.doubleValue = pesoValor / (alturaValor * alturaValor)
                                statusImc.value = determinarClassificacaoIMC(imc.doubleValue)
                            } else {
                                imc.doubleValue = 0.0
                                statusImc.value = "Dados inválidos"
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(id = R.color.vermelho_fiap)
                        )
                    ) {
                        Text(
                            text = "CALCULAR",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            // RESULTADO
            Spacer(modifier = Modifier.height(32.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xff329F6B)),
                elevation = CardDefaults.cardElevation(6.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Resultado",
                            color = Color.White,
                            fontSize = 14.sp
                        )
                        Text(
                            text = statusImc.value,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 18.sp
                        )
                    }
                    Text(
                        text = String.format(Locale.getDefault(), "%.1f", imc.doubleValue),
                        modifier = Modifier.fillMaxWidth(),
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 32.sp,
                        textAlign = TextAlign.End
                    )
                }
            }
        }
    }
}
