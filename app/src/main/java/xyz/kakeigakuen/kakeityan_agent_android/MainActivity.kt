package xyz.kakeigakuen.kakeityan_agent_android

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.trello.rxlifecycle.components.support.RxAppCompatActivity
import com.trello.rxlifecycle.kotlin.bindToLifecycle
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import xyz.kakeigakuen.kakeityan_agent_android.client.BookClient
import xyz.kakeigakuen.kakeityan_agent_android.generator.HttpGenerator
import com.github.mikephil.charting.utils.PercentFormatter
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.Entry
import xyz.kakeigakuen.kakeityan_agent_android.util.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : RxAppCompatActivity() {

    val RECEST_CODE = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val prefer = getSharedPreferences("user", Context.MODE_PRIVATE)
        val budget: TextView = findViewById(R.id.budget)
        budget.text = prefer.getInt("budget", 0).toString()
        val rest: TextView = findViewById(R.id.rest)
        rest.text = prefer.getInt("rest", 0).toString() + "円"
        val df: DateFormat = SimpleDateFormat("yyyy/MM/dd")
        val create_date = Date(System.currentTimeMillis())
        val date: TextView = findViewById(R.id.date)
        date.text = df.format(create_date)
        this.createPieChart()
    }

    fun bookPost(view: View) {
        val prefer = getSharedPreferences("user", Context.MODE_PRIVATE)
        val item: EditText = findViewById(R.id.item)
        val cost: EditText = findViewById(R.id.cost)
        val httpgenerator = HttpGenerator()
        val postclient = httpgenerator.retrofit.create(BookClient::class.java)

        if ((cost.text.toString() == "") || (item.text.toString() == "")) return

        Log.i("action", "book post start")
        postclient.post(cost.text.toString(), item.text.toString(), prefer.getString("token", "0"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .bindToLifecycle(view)
                .subscribe({
                    if ( ! it.error) {
                        val editor = prefer.edit()
                        editor.putInt("budget", it.budget)
                        editor.putInt("rest", it.rest)
                        editor.commit()
                        val budget: TextView = findViewById(R.id.budget)
                        budget.text = prefer.getInt("budget", 0).toString()
                        val rest: TextView = findViewById(R.id.rest)
                        rest.text = prefer.getInt("rest", 0).toString() + "円"
                        val item: EditText = findViewById(R.id.item)
                        val cost: EditText = findViewById(R.id.cost)
                        val send_itme = item.text.toString()
                        val send_cost = cost.text.toString()
                        item.setText("")
                        cost.setText("")
                        this.createPieChart()
                        val bookdialog = BookDialog()
                        bookdialog.show(this, send_itme, send_cost)
                    } else {
                        val hashmaptostring = HashMapToString()
                        val bookerror = BookError()
                        bookerror.show(this, hashmaptostring.ChangeToString(it.message))
                    }
                }, {
                    val networkerror = NetworkError()
                    networkerror.show(this)
                })
    }

    fun speechRecognizer (view: View) {
        try {
            intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "RecognizeSpeechEx")
            startActivityForResult(intent, RECEST_CODE)
        } catch (e: ActivityNotFoundException) {
            Log.e("action", e.toString())
        }
    }

    override fun onActivityResult (requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == RECEST_CODE && resultCode == Activity.RESULT_OK) {
            val item: EditText = findViewById(R.id.item)
            val cost: EditText = findViewById(R.id.cost)
            var result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            if (result.size != 0) {
                val parser = BookParser(result[0])
                item.setText(parser.item)
                cost.setText(parser.cost)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun logout (view: View) {
        val prefer = getSharedPreferences("user", Context.MODE_PRIVATE)
        val editor = prefer.edit()
        editor.putString("token", "0")
        editor.putInt("budget", 0)
        editor.putInt("rest", 0)
        editor.commit()
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }

    // 円グラフ表示用
    private fun createPieChart() {
        val pieChart = findViewById<View>(R.id.pie_chart) as PieChart

        pieChart.isDrawHoleEnabled = true // 真ん中に穴を空けるかどうか
        pieChart.holeRadius = 80f       // 真ん中の穴の大きさ(%指定)
        pieChart.setHoleColorTransparent(true)
        pieChart.transparentCircleRadius = 55f
        pieChart.rotationAngle = 90f          // 開始位置の調整
        pieChart.isRotationEnabled = false      // 回転可能かどうか
        pieChart.legend.isEnabled = false   //凡例
        pieChart.isHighlightEnabled = false //数値
        pieChart.setDescription("")
        pieChart.data = createPieChartData()

        // 更新
        pieChart.invalidate()
    }

    // pieChartのデータ設定
    private fun createPieChartData(): PieData {
        val yVals = ArrayList<Entry>()
        val xVals = ArrayList<String>()
        val colors = ArrayList<Int>()

        xVals.add("")
        xVals.add("")

        val prefer = getSharedPreferences("user", Context.MODE_PRIVATE)
        val rest = prefer.getInt("rest", 0).toFloat()
        var budget = prefer.getInt("budget", 0).toFloat()
        if (budget == 0f) budget = 100000f
        val use = budget - rest

        var rest_per = 0f;
        var use_per = 100f;
        if (rest > 0f) {
            rest_per = (rest/budget) * 100f
            use_per = (use/budget) * 100f
        }

        yVals.add(Entry(rest_per, 0))
        yVals.add(Entry(use_per, 1))

        val dataSet = PieDataSet(yVals, "")
        dataSet.sliceSpace = 5f
        dataSet.selectionShift = 1f

        // 色の設定
        colors.add(ColorTemplate.JOYFUL_COLORS[3])
        colors.add(ColorTemplate.LIBERTY_COLORS[1])
        dataSet.setColors(colors)
        dataSet.setDrawValues(true)

        val data = PieData(xVals, dataSet)
        data.setValueFormatter(PercentFormatter())

        // テキストの設定
        data.setValueTextSize(12f)
        data.setValueTextColor(Color.alpha(0))
        return data
    }
}
