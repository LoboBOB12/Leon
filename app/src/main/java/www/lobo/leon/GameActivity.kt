package www.lobo.leon


import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Gravity
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import java.util.*

class GameActivity : AppCompatActivity() {
    private lateinit var ballImageView: ImageView
    private lateinit var scoreTextView: TextView
    private lateinit var timerTextView: TextView
    private lateinit var countDownTimer: CountDownTimer
    private var score: Int = 0
    private val gameDuration: Long = 60000 // Длительность игры в миллисекундах (в данном случае, 60 секунд)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        ballImageView = findViewById(R.id.ball)
        scoreTextView = findViewById(R.id.score)
        timerTextView = findViewById(R.id.timer)

        ballImageView.setOnClickListener {
            increaseScore()
        }

        startGame()
    }

    private fun startGame() {
        startBallAnimation()
        startTimer()
    }

    private var isAnimationRunning = false

    private fun startBallAnimation() {
        if (isAnimationRunning) {
            return
        }

        val frameLayout = findViewById<FrameLayout>(R.id.frameLayout)
        val ballImageView = ImageView(this)
        val ballDrawable = ContextCompat.getDrawable(this, R.drawable.ball)

        ballImageView.setImageDrawable(ballDrawable)

        val screenWidth = resources.displayMetrics.widthPixels
        val screenHeight = resources.displayMetrics.heightPixels

        val random = Random()

        val fromX = random.nextInt(screenWidth - ballImageView.width)
        val fromY = random.nextInt(screenHeight - ballImageView.height)

        val toX = random.nextInt(screenWidth - ballImageView.width)
        val toY = random.nextInt(screenHeight - ballImageView.height)

        val layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.gravity = Gravity.START or Gravity.TOP
        layoutParams.leftMargin = fromX
        layoutParams.topMargin = fromY

        ballImageView.layoutParams = layoutParams
        frameLayout.addView(ballImageView)

        val animation = TranslateAnimation(
            Animation.ABSOLUTE, 0f,
            Animation.ABSOLUTE, (toX - fromX).toFloat(),
            Animation.ABSOLUTE, 0f,
            Animation.ABSOLUTE, (toY - fromY).toFloat()
        )

        animation.duration = 1300
        animation.repeatCount = 0

        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                isAnimationRunning = true
            }

            override fun onAnimationEnd(animation: Animation?) {
                frameLayout.removeView(ballImageView)
                isAnimationRunning = false
                startBallAnimation()
            }

            override fun onAnimationRepeat(animation: Animation?) {
                // Do nothing
            }
        })

        ballImageView.startAnimation(animation)

        ballImageView.setOnClickListener {
            increaseScore()
        }
    }

    private fun startTimer() {
        countDownTimer = object : CountDownTimer(gameDuration, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished / 1000
                timerTextView.text = "Time: $seconds seconds"
            }

            override fun onFinish() {
                timerTextView.text = "Time's up!"
              finish()
            }
        }
        countDownTimer.start()
    }

    private fun increaseScore() {
        score++
        scoreTextView.text = "Score: $score"
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer.cancel()
    }
}