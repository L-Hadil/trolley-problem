package com.example.trolleyproblem

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.aldebaran.qi.sdk.QiContext
import com.aldebaran.qi.sdk.QiSDK
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks

class MainActivity : AppCompatActivity(), RobotLifecycleCallbacks {

    companion object {
        var qiContext: QiContext? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        QiSDK.register(this, this)

        findViewById<Button>(R.id.buttonWithEmotion).setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, TrolleyProblemWithEmotionFragment())
                .addToBackStack(null)
                .commit()

            findViewById<View>(R.id.menuLayout).visibility = View.GONE
            findViewById<View>(R.id.fragment_container).visibility = View.VISIBLE
        }

        findViewById<Button>(R.id.buttonWithoutEmotion).setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, TrolleyProblemWithoutEmotionFragment())
                .addToBackStack(null)
                .commit()

            findViewById<View>(R.id.menuLayout).visibility = View.GONE
            findViewById<View>(R.id.fragment_container).visibility = View.VISIBLE
        }
    }

    override fun onRobotFocusGained(context: QiContext) {
        qiContext = context
    }

    override fun onRobotFocusLost() {
        qiContext = null
    }

    override fun onRobotFocusRefused(reason: String?) {}

    override fun onDestroy() {
        QiSDK.unregister(this)
        super.onDestroy()
    }

    override fun onBackPressed() {
        val fragmentManager = supportFragmentManager
        if (fragmentManager.backStackEntryCount > 0) {
            fragmentManager.popBackStack()
            findViewById<View>(R.id.fragment_container).visibility = View.GONE
            findViewById<View>(R.id.menuLayout).visibility = View.VISIBLE
        } else {
            super.onBackPressed()
        }
    }
}
