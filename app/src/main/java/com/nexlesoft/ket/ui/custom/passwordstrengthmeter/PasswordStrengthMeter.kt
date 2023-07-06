package com.nexlesoft.ket.ui.custom.passwordstrengthmeter

import android.content.Context
import android.os.Build
import android.text.Editable
import android.text.InputFilter.LengthFilter
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.nexlesoft.ket.R
import kotlin.math.roundToInt


class PasswordStrengthMeter : LinearLayout {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    private val TAG: String = "PasswordStrengthMeter"

    private var passwordStrengthCalculator: PasswordStrengthCalculator =
        PasswordStrengthCalculator()

    private val strengthLevels: List<PasswordStrengthLevel>
    private val strengthBarHeight: Int
    private val animationDuration: Int
    private val strengthLabelTextSize: Int

    private lateinit var textInputLayout: TextInputLayout
    private lateinit var textInputEditText: TextInputEditText
    private lateinit var strengthIndicatorView: StrengthIndicatorView
    private lateinit var strengthLabel: TextView
    private lateinit var textWatcher: TextWatcher

    init {
        val levelNames = resources.getStringArray(R.array.default_password_strength_levels)
        strengthLevels = listOf(
            PasswordStrengthLevel(levelNames[0], android.R.color.darker_gray),
            PasswordStrengthLevel(levelNames[1], android.R.color.holo_red_dark),
            PasswordStrengthLevel(levelNames[2], android.R.color.holo_orange_dark),
            PasswordStrengthLevel(levelNames[3], android.R.color.holo_blue_dark),
            PasswordStrengthLevel(levelNames[4], android.R.color.holo_green_dark),
        )
        strengthBarHeight = convertDpToPx(2)
        animationDuration = 300
        strengthLabelTextSize = convertDpToPx(12)

        initStrengthMeter()
    }

    fun isValidPassword(): Boolean {
        val password = textInputEditText.text.toString()
        return passwordStrengthCalculator.calculatePasswordSecurityLevel(password) > 0
    }

    fun isEmpty(): Boolean {
        return textInputEditText.text.toString().isEmpty()
    }

    fun getPasswordText(): String {
        return textInputEditText.text.toString()
    }

    private fun initStrengthMeter() {
        orientation = VERTICAL
        setVerticalGravity(Gravity.CENTER_VERTICAL)
        // Add password edit text
        textInputLayout = TextInputLayout(context)
        textInputLayout.boxBackgroundMode = TextInputLayout.BOX_BACKGROUND_NONE
        textInputLayout.isHintEnabled = true
        textInputLayout.setHintTextAppearance(R.style.labelTextStyle)
        textInputLayout.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
        addView(textInputLayout)
        //Add password EditText
        textInputEditText = TextInputEditText(context)
        textInputEditText.filters = arrayOf(LengthFilter(18))
        textInputEditText.inputType =
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        textInputEditText.setTextAppearance(R.style.editTextStyle)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            textInputEditText.textCursorDrawable = getDrawable(context, R.drawable.color_cursor)
        }
        textInputEditText.setBackgroundColor(resources.getColor(android.R.color.transparent))
        textInputEditText.setHint(R.string.password_label)
        textInputEditText.transformationMethod = AsteriskTransformationMethod()
        textInputLayout.addView(textInputEditText)

        // Add strength indicator
        strengthIndicatorView = StrengthIndicatorView(context)
        strengthIndicatorView.setAnimDuration(animationDuration)
        strengthIndicatorView.setAnimate(true)
        strengthIndicatorView.setPasswordStrengthLevels(strengthLevels)
        strengthIndicatorView.height = strengthBarHeight
        addView(strengthIndicatorView)
        // Add strength label
        strengthLabel = TextView(context)
        strengthLabel.gravity = Gravity.END
        strengthLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, strengthLabelTextSize.toFloat())
        addView(strengthLabel)
        strengthLabel.text = ""
//        strengthLabel.setTextColor(resources.getColor(R.color.blue))
        strengthLabel.id = View.generateViewId()

        // Set layout params
        setLayoutParams()
        // Add text watcher
        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                refresh()
            }
        }
        textInputEditText.addTextChangedListener(textWatcher)
        textInputEditText.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus && textInputEditText.text.isNullOrEmpty()) {
                strengthIndicatorView.resetIndicator()
            }
        }
    }

    private fun setLayoutParams() {
        strengthLabel.measure(0, 0)
        val strengthLabelParams = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        strengthLabelParams.topMargin = 0
        strengthLabelParams.bottomMargin = 0
        strengthLabelParams.marginEnd = 0
        strengthLabelParams.marginStart = 0
        strengthLabel.layoutParams = strengthLabelParams

        val indicatorParams =
            LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        indicatorParams.setMargins(0, 0, 0, convertDpToPx(4))
        strengthIndicatorView.layoutParams = indicatorParams
    }

    private fun refresh() {
        //Calculate strength level
        val input = textInputEditText.text.toString()
        val level: Int = passwordStrengthCalculator.calculatePasswordSecurityLevel(input)

        val displayIndicator = if (input.isNotEmpty()) View.VISIBLE else View.GONE
        // Refresh strength indicator
        strengthIndicatorView.setSecurityLevel(level)

        // Refresh strength label
        strengthLabel.visibility = displayIndicator
        strengthLabel.text = strengthLevels[level].displayName
        strengthLabel.setTextColor(resources.getColor(strengthLevels[level].indicatorColor))

    }

    private fun convertDpToPx(dp: Int): Int {
        val displayMetrics = context!!.resources.displayMetrics
        return (dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
    }
}