package com.example.guiadeestudos

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat


class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra("discipline") ?: "Disciplina"
        val message = intent.getStringExtra("content") ?: "Conteúdo"

        if (checkNotificationPermission(context)) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channelId = "study_reminder_channel"

            // Criar o canal de notificação (necessário para Android 8.0 e superior)
            createNotificationChannel(notificationManager, channelId)

            // Criar a intenção para abrir a PendingStudiesActivity ao clicar na notificação
            val notificationIntent = Intent(context, PendingStudiesActivity::class.java)

            // Aqui você usa FLAG_IMMUTABLE para garantir que o PendingIntent não seja modificado
            val pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)

            // Criar a notificação
            val notification = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.check) // Ícone da notificação
                .setContentTitle(title) // Título da notificação
                .setContentText(message) // Texto da notificação
                .setPriority(NotificationCompat.PRIORITY_HIGH) // Prioridade alta para notificações importantes
                .setContentIntent(pendingIntent) // Definir a ação ao clicar na notificação
                .setAutoCancel(true) // Cancelar a notificação ao ser clicada
                .build()

            // Exibir a notificação
            notificationManager.notify(0, notification)
        }
    }

    private fun createNotificationChannel(notificationManager: NotificationManager, channelId: String) {
        // Criar o canal de notificação apenas se ele ainda não existir
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "Study Reminders"
            val channelDescription = "Notificações para lembretes de estudos"
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH).apply {
                description = channelDescription
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun checkNotificationPermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
        } else {
            true // Para versões anteriores ao Android 13, a permissão é concedida por padrão
        }
    }
}
