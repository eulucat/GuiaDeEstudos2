package com.example.guiadeestudos

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

class AddStudyActivity : ComponentActivity() {

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar o banco de dados
        dbHelper = DatabaseHelper(this)

        setContent {
            AdicionarEstudoScreen(
                onAdicionarEstudo = { disciplina, conteudo, dia, horario ->
                    adicionarEstudo(disciplina, conteudo, dia, horario)
                },
                onVerEstudosRealizados = {
                    val intent = Intent(this, StudyListActivity::class.java)
                    startActivity(intent)
                },
                onVerEstudosPendentes = {
                    val intent = Intent(this, PendingStudiesActivity::class.java)
                    startActivity(intent)
                }
            )
        }

    }

    @Composable
    fun AdicionarEstudoScreen(
        onAdicionarEstudo: (String, String, String, String) -> Unit,
        onVerEstudosRealizados: () -> Unit,
        onVerEstudosPendentes: () -> Unit
    ) {
        var disciplina by remember { mutableStateOf("") }
        var conteudo by remember { mutableStateOf("") }
        var dia by remember { mutableStateOf("") }
        var horario by remember { mutableStateOf("") }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF6C9BD2).copy(alpha = 0.5f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Cadastrar Estudo",
                    fontSize = 44.sp,
                    color = Color(0xFFFFFFFF),
                    style = TextStyle(
                        fontSize = 54.sp,
                        fontFamily = FontFamily(Font(R.font.intro_script))
                    ),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Image(
                    painter = painterResource(id = R.drawable.mulherazul),
                    contentDescription = "Imagem de Adicionar Estudo",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .padding(16.dp),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(16.dp))

                CampoTexto("Disciplina", disciplina) { disciplina = it }
                CampoTexto("Conteúdo", conteudo) { conteudo = it }
                CampoTexto("Dia (dd/MM/yyyy)", dia) { dia = it }
                CampoTexto("Horário (HH:mm)", horario) { horario = it }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        onAdicionarEstudo(disciplina, conteudo, dia, horario)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C9BD2)),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(text = "Adicionar")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onVerEstudosRealizados,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C9BD2)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Ver Estudos Realizados")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onVerEstudosPendentes,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C9BD2)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Ver Estudos Pendentes")
                }

                Spacer(modifier = Modifier.weight(1f))


                Text(
                    text = "Desenvolvido por Lucas Vasco de Araujo",
                    color = Color.White,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 16.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
    }


    @Composable
    fun CampoTexto(label: String, valor: String, onValorChange: (String) -> Unit) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .background(Color.White, shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp))
                .border(2.dp, Color(0xFF6C9BD2), shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)) // Borda azul
        ) {
            BasicTextField(
                value = valor,
                onValueChange = { onValorChange(it) },
                textStyle = TextStyle(fontSize = 18.sp),
                decorationBox = { innerTextField ->
                    if (valor.isEmpty()) {
                        Text(text = label, color = Color.Gray)
                    }
                    innerTextField()
                },
                modifier = Modifier.padding(16.dp)
            )
        }
    }

    private fun adicionarEstudo(disciplina: String, conteudo: String, dia: String, horario: String) {
        if (disciplina.isNotEmpty() && conteudo.isNotEmpty() && dia.isNotEmpty() && horario.isNotEmpty()) {
            if (!isDataValida(dia)) {
                Toast.makeText(this, "Data inválida! Use o formato dd/MM/yyyy.", Toast.LENGTH_SHORT).show()
                return
            }

            if (!isHorarioValido(horario)) {
                Toast.makeText(this, "Horário inválido! Use o formato HH:mm.", Toast.LENGTH_SHORT).show()
                return
            }

            // Adicionar estudo ao banco de dados
            val result = dbHelper.adicionarEstudo(disciplina, conteudo, dia, horario)

            if (result != -1L) {
                Toast.makeText(this, "Estudo adicionado com sucesso!", Toast.LENGTH_SHORT).show()
                // Agendar notificação
                scheduleNotification(disciplina, conteudo, dia, horario) // Corrigido para passar todos os 4 parâmetros

                // Redirecionar para a página de estudos pendentes após um pequeno atraso
                Handler(Looper.getMainLooper()).postDelayed({
                    val intent = Intent(this, PendingStudiesActivity::class.java)
                    startActivity(intent)
                    finish() // Finaliza a atividade atual, se desejado
                }, 2000) // 2000 ms = 2 segundos
            } else {
                Toast.makeText(this, "Erro ao adicionar estudo!", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun scheduleNotification(disciplina: String, conteudo: String, dia: String, horario: String) {
        val dateTime = parseDateTime(dia, horario)

        // Verifica se a data/hora é válida (futura)
        if (dateTime <= System.currentTimeMillis()) {
            Toast.makeText(this, "A data e horário devem ser futuros!", Toast.LENGTH_SHORT).show()
            return
        }

        // Cria o Intent para o Receiver
        val intent = Intent(this, NotificationReceiver::class.java).apply {
            putExtra("discipline", disciplina)
            putExtra("content", conteudo)
        }

        // Cria o PendingIntent com FLAG_IMMUTABLE
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        // Obtém o AlarmManager
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Verifica se o sistema permite agendamento de alarmes exatos
        if (alarmManager.canScheduleExactAlarms()) {
            // Agendar o alarme com o horário exato
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, dateTime, pendingIntent)
            Log.d("Notification", "Notificação agendada para: $dateTime")
            Toast.makeText(this, "Notificação agendada com sucesso!", Toast.LENGTH_SHORT).show()
        } else {
            Log.e("Notification", "Não tem permissão para agendar alarmes exatos")
            // Exibir mensagem ao usuário
            Toast.makeText(this, "Permissão para agendar alarmes exatos negada.", Toast.LENGTH_LONG).show()
            // Aqui você pode sugerir alternativas, como usar alarmes imprecisos
        }
    }

    private fun parseDateTime(dia: String, horario: String): Long {
        val dateTimeString = "$dia $horario"
        val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return formatter.parse(dateTimeString)?.time ?: 0
    }

    private fun isDataValida(data: String): Boolean {
        return try {
            val formatoData = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            formatoData.isLenient = false
            formatoData.parse(data) != null
        } catch (e: ParseException) {
            false
        }
    }

    private fun isHorarioValido(horario: String): Boolean {
        return try {
            val formatoHora = SimpleDateFormat("HH:mm", Locale.getDefault())
            formatoHora.isLenient = false
            formatoHora.parse(horario) != null
        } catch (e: ParseException) {
            false
        }

    }

}

