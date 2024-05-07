package com.looper.android.support.preference

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.SeekBar
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.looper.android.support.R
import com.looper.android.support.util.UIUtils

open class ColorPickerPreference(context: Context, attrs: AttributeSet) : Preference(context, attrs),
    SeekBar.OnSeekBarChangeListener {
    private var currentColor: Int? = null
    private var redSeekBar: SeekBar? = null
    private var greenSeekBar: SeekBar? = null
    private var blueSeekBar: SeekBar? = null
    private var colorHexInput: EditText? = null
    private var colorPreview: View? = null
    private var circleView: View? = null

    init {
        layoutResource = R.layout.preference_color_picker
    }

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)

        circleView = holder.itemView.findViewById(R.id.circle)
        updateColorView()

        holder.itemView.setOnClickListener {
            showColorPickerDialog()
        }
    }

    override fun onGetDefaultValue(a: TypedArray, index: Int): Any {
        return Color.parseColor(a.getString(index))
    }

    override fun onSetInitialValue(defaultValue: Any?) {
        val color = if (defaultValue is Int) {
            getPersistedInt(defaultValue)
        } else {
            getPersistedInt(Color.WHITE)
        }
        currentColor = color
        persistInt(color)
    }

    private fun showColorPickerDialog() {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.dialog_color_picker, null)

        redSeekBar = view.findViewById(R.id.redSeekBar)
        greenSeekBar = view.findViewById(R.id.greenSeekBar)
        blueSeekBar = view.findViewById(R.id.blueSeekBar)
        colorHexInput = view.findViewById(R.id.colorHexInput)
        colorPreview = view.findViewById(R.id.colorPreview)

        setColorWithText(getPersistedInt(Color.WHITE), true)

        redSeekBar?.setOnSeekBarChangeListener(this)
        greenSeekBar?.setOnSeekBarChangeListener(this)
        blueSeekBar?.setOnSeekBarChangeListener(this)

        colorHexInput?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val hexColor = s.toString()
                if (hexColor.length == 9 || hexColor.length == 7) {
                    try {
                        val color = Color.parseColor(hexColor)
                        @Suppress("SameParameterValue")
                        setColorWithText(color, true)
                    } catch (e: IllegalArgumentException) {
                        UIUtils.showToast(
                            context,
                            context.getString(R.string.invalid_color),
                            UIUtils.TOAST_LENGTH_SHORT
                        )
                    }
                }
            }
        })

        MaterialAlertDialogBuilder(context)
            .setView(view)
            .setPositiveButton(R.string.okay) { _, _ ->
                val color = getColor()
                setColor(color)
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        setColorPreview(getColor())
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        val newColorString = colorToString(getColor())

        colorHexInput?.let {
            if (newColorString != it.text.toString()) {
                it.setText(newColorString)
            }
        }
    }

    private fun getColor(): Int {
        return Color.rgb(
            redSeekBar?.progress ?: 0,
            greenSeekBar?.progress ?: 0,
            blueSeekBar?.progress ?: 0
        )
    }

    private fun setColor(color: Int) {
        currentColor = color
        persistInt(color)
        updateColorView()
    }

    private fun setColorWithText(color: Int, textUpdate: Boolean = false) {
        redSeekBar?.progress = Color.red(color)
        greenSeekBar?.progress = Color.green(color)
        blueSeekBar?.progress = Color.blue(color)

        if (textUpdate) {
            val newColorString = colorToString(getColor())
            colorHexInput?.let {
                if (newColorString != it.text.toString()) {
                    it.setText(newColorString)
                }
            }
        }
        setColorPreview(color)
    }

    private fun setColorPreview(color: Int) {
        colorPreview!!.setBackgroundColor(color)
    }

    private fun updateColorView() {
        (if (currentColor is Int) currentColor else Color.WHITE)?.let {
            circleView!!.setBackgroundColor(it)
        }
    }

    private fun colorToString(color: Int): String {
        return String.format("#%08X", color)
    }
}