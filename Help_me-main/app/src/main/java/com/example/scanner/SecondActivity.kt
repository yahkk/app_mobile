package com.example.scanner

import android.content.Intent

class SecondActivity : AppCompatActivity() {
    var resulttv: TextView? = null
    var codebar: TextView? = null
    var rcodebar: String? = null
    var tts: TextToSpeech? = null
    var bt: Button? = null
    @Override
    protected fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        val intent: Intent = getIntent()
        val message: String = intent.getStringExtra("TextView")
        resulttv = findViewById(R.id.result_tv)
        codebar = findViewById(R.id.textView)
        codebar.setText(message)
        rcodebar = message
        bt = findViewById(R.id.button)
        try {
            data
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }
    }

    @get:Throws(MalformedURLException::class)
    val data: Unit
        get() {
            val uri: Uri = Uri.parse("https://c1e9-196-200-133-171.ngrok.io/products")
                .buildUpon().build()
            val url = URL(uri.toString())
            DoTask().execute(url)
        }

    internal inner class DoTask : AsyncTask<URL?, Void?, String?>() {
        @Override
        protected fun doInBackground(vararg urls: URL?): String? {
            val url: URL? = urls[0]
            var data: String? = null
            try {
                data = NetworkUtils.makeHTTPRequest(url)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return data
        }

        @Override
        protected fun onPostExecute(s: String?) {
            try {
                parseJson(s)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }

        @Throws(JSONException::class)
        fun parseJson(data: String?) {
            var jsonObject: JSONObject? = null
            try {
                jsonObject = JSONObject(data)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            val cityArray: JSONArray = jsonObject.getJSONArray("products")
            for (i in 0 until cityArray.length()) {
                val produit: JSONObject = cityArray.getJSONObject(i)
                val produit_code: String = produit.get("codebar").toString()
                if (produit_code.equals(rcodebar)) {
                    val description: String = produit.get("description").toString()
                    val type: String = produit.get("type").toString()
                    val price: String = produit.get("price").toString()
                    val name: String = produit.get("name").toString()
                    resulttv.setText("this is $name\nit is a   $type \nit cost   $price dirhams  \nit is    $description")
                    tts = TextToSpeech(getApplicationContext(), object : OnInitListener() {
                        @Override
                        fun onInit(i: Int) {
                            if (i == TextToSpeech.SUCCESS) {
                                tts.setLanguage(Locale.UK)
                                tts.setSpeechRate(1.0f)
                                tts.speak(
                                    resulttv.getText().toString(),
                                    TextToSpeech.QUEUE_ADD,
                                    null
                                )
                                bt.setOnClickListener(object : OnClickListener() {
                                    @Override
                                    fun onClick(v: View?) {
                                        tts.speak(
                                            resulttv.getText().toString(),
                                            TextToSpeech.QUEUE_ADD,
                                            null
                                        )
                                    }
                                })
                            }
                        }
                    })
                    break
                } else {
                    resulttv.setText("we do not have any infomation about the product you have just scaned")
                    tts = TextToSpeech(getApplicationContext(), object : OnInitListener() {
                        @Override
                        fun onInit(i: Int) {
                            if (i == TextToSpeech.SUCCESS) {
                                tts.setLanguage(Locale.UK)
                                tts.setSpeechRate(1.0f)
                                tts.speak(
                                    resulttv.getText().toString(),
                                    TextToSpeech.QUEUE_ADD,
                                    null
                                )
                            }
                        }
                    })
                    bt.setOnClickListener(object : OnClickListener() {
                        @Override
                        fun onClick(v: View?) {
                            tts.speak(resulttv.getText().toString(), TextToSpeech.QUEUE_ADD, null)
                        }
                    })
                }
            }
        }
    }
}