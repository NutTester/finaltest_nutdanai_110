package th.ac.rmutto.finaltest_nutdanai_110

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    var recyclerView: RecyclerView? = null
    private var display = ArrayList<Data>()
    private val data = ArrayList<Data>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)

        val root = inflater.inflate(R.layout.activity_main, container, false)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        recyclerView = root.findViewById(R.id.recyclerView)
        showDataList()

        return root

    }



        private fun showDataList() {
            val url: String = getString(R.string.root_url) + getString(R.string.product_url)
            val okHttpClient = OkHttpClient()
            val request: Request = Request.Builder().url(url).get().build()
            try {
                val response = okHttpClient.newCall(request).execute()
                if (response.isSuccessful) {
                    try {
                        val res = JSONArray(response.body!!.string())
                        if (res.length() > 0) {
                            for (i in 0 until res.length()) {
                                val item: JSONObject = res.getJSONObject(i)
                                data.add( Data(
                                    item.getString("productID"),
                                    item.getString("productName"),
                                    item.getString("price"),
                                    item.getString("quantity"),
                                    item.getString("imageFile")
                                )
                                )
                            }
                            display.addAll(data)
                            recyclerView!!.adapter = DataAdapter(display)
                        } else {
                            Toast.makeText(context, "ไม่สามารถแสดงข้อมูลได้",
                                Toast.LENGTH_LONG).show()
                        }
                    } catch (e: JSONException) { e.printStackTrace() }
                } else { response.code }
            } catch (e: IOException) { e.printStackTrace() }
        }

}