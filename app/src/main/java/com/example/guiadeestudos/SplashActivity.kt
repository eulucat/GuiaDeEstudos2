package com.example.guiadeestudos

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            setContentView(R.layout.splash_screen)

            // Defina um tempo para a tela de splash ser exibida
            Handler().postDelayed({
                // Inicie a MainActivity após o tempo definido
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish() // Finaliza a SplashActivity
            }, 3000) // 3000 milissegundos (3 segundos)
        }
    }
}
