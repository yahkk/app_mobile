package com.example.scanner

import android.content.Intent

class MainActivity : AppCompatActivity() {
    var btscan: Button? = null
    var product_id: String? = null
    @Override
    protected fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btscan = findViewById(R.id.bt_scan)
        btscan.setOnClickListener { view ->
            val intentIntegrator = IntentIntegrator(this@MainActivity)
            intentIntegrator.setPrompt("For flash use volume up key")
            intentIntegrator.setBeepEnabled(true)
            intentIntegrator.setOrientationLocked(true)
            intentIntegrator.setCaptureActivity(Capture::class.java)
            intentIntegrator.initiateScan()
        }
    }

    @Override
    protected fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val intentResult: IntentResult = IntentIntegrator.parseActivityResult(
            requestCode, resultCode, data
        )
        product_id = intentResult.getContents()
        val dis = product_id
        val intent = Intent(this@MainActivity, SecondActivity::class.java)
        intent.putExtra("TextView", dis)
        startActivity(intent)
    }
}