package com.example.meetup.util

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class AutoMask {

    companion object {
        private fun removeMask(textToUnmask: String): String {
            return textToUnmask.replace(".", "").replace("-", "")
                .replace("(", "").replace(")", "")
                .replace("/", "").replace(" ", "")
                .replace("*", "")
        }

        fun mask(mask: String, editText: EditText): TextWatcher {
            return object : TextWatcher {
                var isUpdating: Boolean = false
                var oldString: String = ""
                override fun beforeTextChanged(
                    charSequence: CharSequence,
                    i: Int,
                    i1: Int,
                    i2: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    val str: String = removeMask(s.toString())
                    var inputWithMask = ""
                    if (count == 0)//is deleting
                        isUpdating = true
                    if (isUpdating) {
                        oldString = str
                        isUpdating = false
                        return
                    }
                    var i = 0
                    for (m: Char in mask.toCharArray()) {
                        if (m != '#' && str.length > oldString.length) {
                            inputWithMask += m
                            continue
                        }
                        try {
                            inputWithMask += str[i]
                        } catch (e: Exception) {
                            break
                        }
                        i++
                    }
                    isUpdating = true
                    editText.setText(inputWithMask)
                    editText.setSelection(inputWithMask.length)
                }

                override fun afterTextChanged(editable: Editable) {}
            }
        }
    }
}
