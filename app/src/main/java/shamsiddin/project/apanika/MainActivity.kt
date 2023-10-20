package shamsiddin.project.apanika

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import shamsiddin.project.apanika.UI.HomeFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction().add(R.id.main, HomeFragment()).commit()
    }
}