package rocks.mobileera.mobileera

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity


class SplashActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

}
