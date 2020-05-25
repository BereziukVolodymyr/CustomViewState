package com.volodymyr.drawingview

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerDialogBuilder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private val REQUEST_WRITE_PERMISSION = 100
    private var paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        isDither = true
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        strokeWidth = 30f
        setShadowLayer(15f, 0f, 0f, Color.BLACK)
    }
    private var path = Path()
    private var painter = Painter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        painter.paint = paint
        painter.path = path
        drawing_view.painter = painter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_WRITE_PERMISSION
            )
        } else {
            storeAsync()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_WRITE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            storeAsync()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_change_color -> {
                setupColorPicker()
                true
            }
            R.id.menu_clear -> {
                this.drawing_view.clean()
                true
            }
            R.id.menu_save -> {
                requestPermission()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun storeAsync() {
        GlobalScope.launch {
            val result = storeImage(this@MainActivity, drawing_view.cachedBitmap, "saved_image.png")
            runOnUiThread {
                handleResult(result)
            }
        }
    }

    private fun handleResult(result: Boolean) {
        if (result) {
            Toast.makeText(applicationContext, "Successfully saved", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(applicationContext, "Failed to store image", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupColorPicker() {
        ColorPickerDialogBuilder
            .with(this)
            .setTitle("Choose color")
            .initialColor(Color.WHITE)
            .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
            .density(12)
            .setOnColorSelectedListener { selectedColor ->
                paint.color = selectedColor
                paint.setShadowLayer(15f, 0f, 0f, selectedColor)
                painter.path = Path()
            }
            .setPositiveButton(
                "ok"
            ) { dialog, selectedColor, allColors ->

            }
            .setNegativeButton(
                "cancel"
            ) { dialog, which -> }
            .build()
            .show()
    }


}
