package com.example.guiadeestudos

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.fontResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

class PendingStudiesActivity : ComponentActivity() {

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dbHelper = DatabaseHelper(this)
        val estudosPendentes = dbHelper.obterEstudosNaoRealizados()

        setContent {
            var estudos by remember { mutableStateOf(estudosPendentes) }

            PendingStudiesScreen(
                estudosPendentes = estudos,
                onMarcarComoRealizado = { estudo ->
                    dbHelper.marcarEstudoComoRealizado(estudo.id) // Atualiza a base de dados
                    estudos = dbHelper.obterEstudosNaoRealizados() // Atualiza a lista de estudos pendentes
                },
                onExcluirEstudo = { estudo ->
                    dbHelper.excluirEstudo(estudo.id) // Exclui o estudo do banco de dados
                    estudos = dbHelper.obterEstudosNaoRealizados() // Atualiza a lista de estudos pendentes
                },
                onBackToCadastroClick = {
                    val intent = Intent(this, AddStudyActivity::class.java)
                    startActivity(intent)
                },
                onViewRealizedStudiesClick = {
                    val intent = Intent(this, StudyListActivity::class.java)
                    startActivity(intent)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PendingStudiesScreen(
    estudosPendentes: List<DatabaseHelper.Estudo>,
    onMarcarComoRealizado: (DatabaseHelper.Estudo) -> Unit,
    onExcluirEstudo: (DatabaseHelper.Estudo) -> Unit,
    onBackToCadastroClick: () -> Unit,
    onViewRealizedStudiesClick: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var estudoSelecionado by remember { mutableStateOf<DatabaseHelper.Estudo?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "Estudos Pendentes",
                            fontSize = 44.sp,
                            fontFamily = FontFamily(Font(R.font.intro_script)), // Referência à fonte
                            color = Color(0xFF00CED1)
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
                    painter = painterResource(id = R.drawable.lastimage), // Nome da imagem que você adicionou
                    contentDescription = "Imagem de Estudos",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp) // Ajuste a altura conforme necessário
                        .padding(bottom = 16.dp) // Espaçamento inferior
                )

                // Botão para ver estudos realizados
                Button(
                    onClick = onViewRealizedStudiesClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00CED1)) // Alterado para azul claro
                ) {
                    Text("Ver Estudos Realizados", color = Color.White)
                }

                // Botão para voltar ao cadastro
                Button(
                    onClick = onBackToCadastroClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00CED1)) // Alterado para azul claro
                ) {
                    Text("Voltar ao Cadastro", color = Color.White)
                }

                // Exibir a lista de estudos pendentes
                LazyColumn(
                    modifier = Modifier
                        .weight(1f) // Esse modificador permite que a lista ocupe o espaço restante
                ) {
                    items(estudosPendentes) { estudo ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp)
                                .background(Color.White, shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp))
                                .border(2.dp, Color(0xFF00CED1), shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(text = "Disciplina: ${estudo.disciplina}")
                                Text(text = "Conteúdo: ${estudo.conteudo}")
                                Text(text = "Data: ${estudo.dia}")
                                Text(text = "Horário: ${estudo.horario}")

                                // Agrupando os botões lado a lado
                                Row(
                                    modifier = Modifier.padding(top = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween // Espaçamento entre os botões
                                ) {
                                    // Botão para marcar como realizado
                                    Button(
                                        onClick = {
                                            onMarcarComoRealizado(estudo)
                                            // Mostrar mensagem de "Estudado com sucesso!"
                                            coroutineScope.launch {
                                                snackbarHostState.showSnackbar("Estudado com sucesso!")
                                            }
                                        },
                                        modifier = Modifier.padding(end = 8.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00CED1)) // Alterado para azul claro
                                    ) {
                                        Text("Marcar como Realizado", color = Color.White)
                                    }

                                    // Botão para excluir o estudo com confirmação
                                    Button(
                                        onClick = {
                                            showDialog = true
                                            estudoSelecionado = estudo
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00CED1)) // Alterado para azul claro
                                    ) {
                                        Text("Excluir", color = Color.White)
                                    }
                                }
                            }
                        }
                    }
                }

                // Adicionando o texto de crédito no final da tela
                Text(
                    text = "Desenvolvido por Lucas Vasco de Araujo",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 14.dp),
                    color = Color.Gray,
                    fontWeight = FontWeight.Light,
                    fontSize = 14.sp
                )

                // Exibir diálogo de confirmação ao tentar excluir um estudo
                if (showDialog && estudoSelecionado != null) {
                    AlertDialog(
                        onDismissRequest = { showDialog = false },
                        title = { Text(text = "Confirmar Exclusão") },
                        text = { Text(text = "Tem certeza que deseja excluir esse estudo?") },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    estudoSelecionado?.let {
                                        onExcluirEstudo(it)
                                        // Mostrar a mensagem de "Estudo excluído com sucesso!"
                                        coroutineScope.launch {
                                            snackbarHostState.showSnackbar("Estudo excluído com sucesso!")
                                        }
                                    }
                                    showDialog = false
                                    estudoSelecionado = null
                                }
                            ) {
                                Text("Sim")
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = {
                                    showDialog = false
                                    estudoSelecionado = null
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
