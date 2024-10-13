package com.example.guiadeestudos

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.fontResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.guiadeestudos.ui.theme.GuiaDeEstudosTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material3.Icon
import androidx.compose.ui.res.painterResource

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GuiaDeEstudosTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(Modifier.padding(innerPadding))
                }
            }
        }
    }

    @Composable
    fun MainScreen(modifier: Modifier = Modifier) {
        val context = LocalContext.current

        Column(
            modifier = modifier
                .fillMaxSize()
                .background(Color(0xFF6C9BD2))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally // Centraliza os filhos horizontalmente
        ) {
            // Seção do cabeçalho
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(top = 16.dp) // Espaçamento superior do cabeçalho
            ) {
                // Ícone de livro
                Icon(
                    imageVector = Icons.Default.Book,
                    contentDescription = "Ícone de Livro",
                    tint = Color.White,
                    modifier = Modifier.size(40.dp) // Tamanho do ícone
                )

                Spacer(modifier = Modifier.width(8.dp)) // Espaço entre o ícone e o texto

                // Texto do cabeçalho
                Text(
                    text = "Guia de Estudos",
                    color = Color.White,
                    fontSize = 14.sp, // Tamanho da fonte
                    modifier = Modifier.padding(start = 4.dp) // Espaçamento à esquerda
                )
            }

            Spacer(modifier = Modifier.weight(1f)) // Empurra o conteúdo abaixo para centralizar o título

            // Texto centralizado
            Text(
                text = "Seja bem-vindo ao seu Guia de Estudos!",
                color = Color.White,
                fontSize = 40.sp, // Aumenta o tamanho da fonte
                fontFamily = FontFamily(Font(R.font.intro_script)),
                lineHeight = 48.sp, // Espaçamento entre linhas
                modifier = Modifier.fillMaxWidth(), // Garante que ocupe a largura total
                textAlign = androidx.compose.ui.text.style.TextAlign.Center // Centraliza o texto
            )

            // Adicionando a imagem abaixo do texto
            Spacer(modifier = Modifier.height(16.dp)) // Espaço entre o texto e a imagem
            Image(
                painter = painterResource(id = R.drawable.designer2), // Referência à imagem
                contentDescription = "Imagem de boas-vindas",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(340.dp) // Ajuste a altura conforme necessário
                    .padding(6.dp)
            )

            Spacer(modifier = Modifier.weight(1f)) // Empurra o conteúdo de baixo para garantir centralização

            // Coloca o botão na parte inferior
            Button(
                onClick = {
                    val intent = Intent(context, AddStudyActivity::class.java)
                    context.startActivity(intent)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFA3D4A7)) // Cor verde
            ) {
                Text(text = "Avançar")
            }

            Spacer(modifier = Modifier.weight(1f))

            // Frase final
            Text(
                text = "Desenvolvido por Lucas Vasco de Araujo",
                color = Color.White,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 16.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center // Centraliza o texto
            )
        }
    }
}
