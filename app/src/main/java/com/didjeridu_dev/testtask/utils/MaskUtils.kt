package com.didjeridu_dev.testtask.utils

class MaskUtils {
    companion object {
        fun applyMask(input: String, mask: String): String {
            if (input.length > mask.length) {
                return input.substring(0, mask.length)
            }
            val result = StringBuilder()
            for (i in input.indices) {
                val maskChar = mask[i]
                if (maskChar == 'Х') {
                    result.append(input[i])
                } else{
                    result.append(maskChar)
                }
            }
            return result.toString()
        }
    }
}