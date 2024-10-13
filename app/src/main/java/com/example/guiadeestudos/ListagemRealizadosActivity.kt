package com.example.guiadeestudos

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

class StudyListActivity : ComponentActivity() {

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dbHelper = DatabaseHelper(this)

        // Obter a lista de estudos realizados
        val estudosRealizados = dbHelper.obterEstudosRealizados()

        // Exibir a interface usando Jetpack Compose
        setContent {
            StudyListScreen(
                dbHelper = dbHelper,
                estudosRealizados = estudosRealizados,
                onBackToCadastroClick = {
                    startActivity(Intent(this, AddStudyActivity::class.java))
                },
                onViewPendingStudiesClick = {
                    startActivity(Intent(this, PendingStudiesActivity::class.java))
                }
            )
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudyListScreen(
    dbHelper: DatabaseHelper,
    estudosRealizados: List<DatabaseHelper.Estudo>,
    onBackToCadastroClick: () -> Unit,
    onViewPendingStudiesClick: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var estudoToDelete by remember { mutableStateOf<DatabaseHelper.Estudo?>(null) }
    var estudoToRevert by remember { mutableStateOf<DatabaseHelper.Estudo?>(null) }
    var estudos by remember { mutableStateOf(estudosRealizados) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "Estudos Realizados",
                            fontSize = 44.sp,
                            fontFamily = FontFamily(Font(R.font.intro_script)), // Referência à fonte
                            color = Color(0xFF6C9BD2)
                        )
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Adicionando a imagem logo abaixo do título
                Image(
                    painter = painterResource(id = R.drawable.study2), // Nome da imagem que você adicionou
                    contentDescription = "Imagem de Estudos",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp) // Ajuste a altura conforme necessário
                        .padding(bottom = 16.dp) // Espaçamento inferior
                )

                // Botão para voltar para a página de cadastro
                Button(
                    onClick = onBackToCadastroClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C9BD2)) // Cor azul-claro
                ) {
                    Text("Voltar ao Cadastro", color = Color.White)
                }

                // Botão para ver estudos pendentes
                Button(
                    onClick = onViewPendingStudiesClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C9BD2)) // Cor azul-claro
                ) {
                    Text("Ver Estudos Pendentes", color = Color.White)
                }

                // Exibir a lista de estudos realizados
                LazyColumn(
                    modifier = Modifier.weight(1f) // Ocupa o espaço disponível, exceto o espaço da frase no final
                ) {
                    items(estudos) { estudo ->
                        // Retângulo envolvendo os estudos
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp)
                                .background(Color.White, shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp))
                                .border(2.dp, Color(0xFF6C9BD2), shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)) // Borda azul

                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(text = "Disciplina: ${estudo.disciplina}")
                                Text(text = "Conteúdo: ${estudo.conteudo}")
                                Text(text = "Data: ${estudo.dia}")
                                Text(text = "Horário: ${estudo.horario}")

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 8.dp),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    // Botão para excluir o estudo
                                    Button(
                                        onClick = {
                                            estudoToDelete = estudo
                                            showDialog = true
                                        },
                                        modifier = Modifier.padding(start = 8.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C9BD2)) // Cor azul-claro
                                    ) {
                                        Text("Excluir", color = Color.White)
                                    }

                                    // Botão para reverter o estudo para não realizado
                                    Button(
                                        onClick = {
                                            estudoToRevert = estudo
                                            dbHelper.marcarEstudoComoNaoRealizado(estudo.id.toInt()) // Converter Long para Int
                                            estudos = dbHelper.obterEstudosRealizados() // Atualiza a lista
                                            coroutineScope.launch {
                                                snackbarHostState.showSnackbar("Estudo retornado para Estudos Pendente!")
                                            }
                                        },
                                        modifier = Modifier.padding(start = 8.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C9BD2)) // Cor azul-claro
                                    ) {
                                        Text("Voltar para Estudos Pendentes", color = Color.White)
                                    }
                                }
                            }
                        }
                    }
                }

                // Adicionando a frase "Desenvolvido por Lucas Vasco de Araujo" no final
                Spacer(modifier = Modifier.height(16.dp)) // Espaçamento entre a lista e a frase
                Text(
                    text = "Desenvolvido por Lucas Vasco de Araujo",
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.Gray,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Light,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                // Diálogo de confirmação para exclusão
                if (showDialog && estudoToDelete != null) {
                    AlertDialog(
                        onDismissRequest = { showDialog = false },
                        title = { Text("Confirmar Exclusão") },
                        text = { Text("Tem certeza que deseja excluir esse estudo?") },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    estudoToDelete?.let {
                                        dbHelper.excluirEstudo(it.id)
                                        estudos = dbHelper.obterEstudosRealizados()
                                        coroutineScope.launch {
                                            snackbarHostState.showSnackbar("Estudo excluído com sucesso!")
                                        }
                                    }
                                    showDialog = false
                                    estudoToDelete = null
                                }
                            ) {
                                Text("Sim")
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = {
                                    showDialog = false
                                    estudoToDelete = null
                                }
                            ) {
                                Text("Não")
                            }
                        }
                    )
                }
            }
        }
    )
}
