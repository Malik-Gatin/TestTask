package com.didjeridu_dev.testtask.utils

class MaskUtils {
    companion object {
        fun fromMaskToRequest(inputString:String): String{
            return inputString.replace(Regex("[^0-9]"), "")
        }

        fun applyMask(text: String, mask: String, firstXIndex:Int): String {
            val filteredText = if(text.length > firstXIndex) text.filterIndexed { index, c -> index > firstXIndex && c.isDigit() } else text
            val formattedText = buildString {
                var index = 0
                for (i in mask.indices) {
                    if (index >= filteredText.length) break
                    if (mask[i] == 'Х') {
                        append(filteredText[index])
                        index++
                    } else {
                        append(mask[i])
                    }
                }
            }
            return formattedText
        }
    }
}