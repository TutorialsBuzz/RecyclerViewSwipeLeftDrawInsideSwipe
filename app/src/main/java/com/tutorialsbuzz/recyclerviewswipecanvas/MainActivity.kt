package com.tutorialsbuzz.recyclerviewswipecanvas

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tutorialsbuzz.recyclerview.CustomAdapter
import com.tutorialsbuzz.recyclerview.Model
import com.tutorialsbuzz.recylerviewswipetodelete.SwipeToDeleteCallback
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rcv.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        val model = readFromAsset();
        val adapter = CustomAdapter(model, this)
        rcv.adapter = adapter;

        rcv.addItemDecoration(SimpleDividerItemDecoration(this))

        val swipeToDeleteCallback =
            object : SwipeToDeleteCallback(this, 0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    adapter.undoView(viewHolder.adapterPosition)
                }
            }

        //configure left swipe
        swipeToDeleteCallback.leftBG = ContextCompat.getColor(this, R.color.leftSwipeBG)
        swipeToDeleteCallback.leftLabel = "Thumbs UP"
        swipeToDeleteCallback.leftIcon = AppCompatResources.getDrawable(this, R.drawable.ic_thumb_up_black_24dp)

        //configure right swipe
        swipeToDeleteCallback.rightBG = ContextCompat.getColor(this, R.color.rightSwipeBG)
        swipeToDeleteCallback.rightLabel = "Thumbs Down"
        swipeToDeleteCallback.rightIcon = AppCompatResources.getDrawable(this, R.drawable.ic_thumb_down_black_24dp)

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(rcv)
    }


    private fun readFromAsset(): List<Model> {

        val modeList = mutableListOf<Model>()

        val bufferReader = application.assets.open("android_version.json").bufferedReader()
        val json_string = bufferReader.use {
            it.readText()
        }

        val jsonArray = JSONArray(json_string);
        for (i in 0..jsonArray.length() - 1) {
            val jsonObject: JSONObject = jsonArray.getJSONObject(i)
            val model = Model(jsonObject.getString("name"), jsonObject.getString("version"))
            modeList.add(model)
        }
        return modeList
    }

}
