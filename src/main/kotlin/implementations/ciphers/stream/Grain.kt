package implementations.ciphers.stream

import implementations.Cipher
import java.math.BigInteger
import java.util.*


class GrainCipher(iv: String, key: String) : Cipher {
    var lfsr = arrayOfNulls<Byte>(80)
    var nfsr = arrayOfNulls<Byte>(80)
    var keyStream: String
    private val temp_lfsr: Array<Byte?>
    private val temp_nfsr: Array<Byte?>
    private val filter: Array<Byte?>

    init {
        temp_lfsr = arrayOfNulls(160)
        temp_nfsr = arrayOfNulls(160)
        filter = arrayOfNulls(160)
        val iv_result = stringToBinary(iv)
        for (i in 0 until iv_result.length) {
            lfsr[i] = iv_result[i].toString().toByte()
        }
        for (i in 64..79) {
            lfsr[i] = 1
        }
        val result2 = stringToBinary(key)
        for (i in 0 until result2.length) {
            nfsr[i] = result2[i].toString().toByte()
        }
        lfsr()
        nfsr()
        val x0 = temp_lfsr[3]
        val x1 = temp_lfsr[25]
        val x2 = temp_lfsr[46]
        val x3 = temp_lfsr[64]
        val x4 = temp_nfsr[63]
        val f_h =
            (x1!!.toInt() xor x4!!.toInt() xor (x0!!.toInt() and x3!!.toInt()) xor (x2!!.toInt() and x3.toInt()) xor (x3.toInt() and x3.toInt()) xor (x0.toInt() and x1.toInt() and x2.toInt())
                    xor (x0.toInt() and x2.toInt() and x3.toInt()) xor (x0.toInt() and x2.toInt() and x4.toInt()) xor (x1.toInt() and x2.toInt() and x4.toInt()) xor (x2.toInt() and x3.toInt() and x4.toInt())).toByte()
        val string_filter = StringBuilder()
        for (i in 0..159) {
            filter[i] = (temp_nfsr[i]!!.toInt() xor f_h.toInt()).toByte()
            string_filter.append(filter[i])
        }
        keyStream = stringBinaryToHex(string_filter.toString())
    }

    fun stringToBinary(string: String): String {
        val result = StringBuilder()
        var tmpStr: String
        var tmpInt: Int
        val messChar = string.toCharArray()
        for (c in messChar) {
            tmpStr = Integer.toBinaryString(c.code)
            tmpInt = tmpStr.length
            if (tmpInt != 8) {
                tmpInt = 8 - tmpInt
                if (tmpInt == 8) {
                    result.append(tmpStr)
                } else if (tmpInt > 0) {
                    result.append("0".repeat(tmpInt))
                    result.append(tmpStr)
                } else {
                    System.err.println("argument 'bits' is too small")
                }
            } else {
                result.append(tmpStr)
            }
        }
        return result.toString()
    }

    fun stringBinaryToHex(string: String?): String {
        return BigInteger(string, 2).toString(16)
    }

    fun hexToBinary(hex: String): String {
        val binStrBuilder = StringBuilder()
        var i = 0
        while (i < hex.length - 1) {
            val output = hex.substring(i, i + 2)
            val decimal = output.toInt(16)
            val binStr = Integer.toBinaryString(decimal)
            val len = binStr.length
            val sbf = StringBuilder()
            if (len < 8) {
                sbf.append("0".repeat(8 - len))
                sbf.append(binStr)
            } else {
                sbf.append(binStr)
            }
            binStrBuilder.append(sbf.toString())
            i += 2
        }
        return binStrBuilder.toString()
    }

    fun lfsr() {
        val data = lfsr
        var xor: Byte
        for (i in 1..160) {
            xor =
                (data[62]!!.toInt() xor data[51]!!.toInt() xor data[38]!!.toInt() xor data[23]!!.toInt() xor data[13]!!
                    .toInt() xor data[0]!!.toInt()).toByte()
            val data_list = Arrays.asList(*data)
            Collections.rotate(data_list, 1)
            temp_lfsr[i - 1] = data_list[0]
            data_list[0] = xor
        }
    }

    fun nfsr() {
        val data = nfsr
        var xor: Byte
        for (i in 1..160) {
            xor = (data[0]!!.toInt() xor data[63]!!.toInt() xor data[60]!!
                .toInt()
                    xor data[52]!!.toInt() xor data[45]!!.toInt() xor data[37]!!
                .toInt()
                    xor data[33]!!.toInt() xor data[28]!!.toInt() xor data[21]!!
                .toInt()
                    xor data[15]!!.toInt() xor data[19]!!.toInt() xor data[0]!!
                .toInt()
                    xor (data[63]!!.toInt() and data[60]!!.toInt()) xor (data[37]!!.toInt() and data[33]!!.toInt())
                    xor (data[15]!!.toInt() and data[9]!!.toInt()) xor (data[60]!!.toInt() and data[52]!!.toInt() and data[45]!!
                .toInt())
                    xor (data[33]!!.toInt() and data[28]!!.toInt() and data[21]!!.toInt())
                    xor (data[63]!!.toInt() and data[45]!!.toInt() and data[28]!!.toInt() and data[9]!!.toInt())
                    xor (data[60]!!.toInt() and data[52]!!.toInt() and data[37]!!.toInt() and data[33]!!.toInt())
                    xor (data[63]!!.toInt() and data[60]!!.toInt() and data[21]!!.toInt() and data[15]!!.toInt())
                    xor (data[63]!!.toInt() and data[60]!!.toInt() and data[52]!!.toInt() and data[45]!!.toInt() and data[37]!!
                .toInt())
                    xor (data[33]!!.toInt() and data[28]!!.toInt() and data[21]!!.toInt() and data[15]!!.toInt() and data[9]!!
                .toInt())
                    xor (data[52]!!.toInt() and data[45]!!.toInt() and data[37]!!.toInt() and data[33]!!.toInt() and data[28]!!
                .toInt() and data[21]!!.toInt())).toByte()
            val data_list = Arrays.asList(*data)
            Collections.rotate(data_list, 1)
            temp_nfsr[i - 1] = data_list[0]
            data_list[0] = xor
        }
    }

    override fun encrypt(message: String): String {
        val result = stringToBinary(message)
        val result_string = StringBuilder()
        val result_array = arrayOfNulls<Byte>(result.length)
        val result_xor_array = arrayOfNulls<Byte>(result.length)
        for (i in result_array.indices) {
            result_array[i] = result[i].toString().toByte()
            result_xor_array[i] = (filter[i]!!.toInt() xor result_array[i]!!.toInt()).toByte()
            result_string.append(result_xor_array[i])
        }
        return stringBinaryToHex(result_string.toString())
    }

    override fun decrypt(message: String): String {
        val a = hexToBinary(message)
        val a_array = arrayOfNulls<Byte>(a.length)
        val b = hexToBinary(keyStream)
        val b_array = arrayOfNulls<Byte>(b.length)
        val plain_binary = StringBuilder()
        val plain = StringBuilder()
        val hasil = arrayOfNulls<Byte>(a.length)
        for (i in a_array.indices) {
            a_array[i] = a[i].toString().toByte()
        }
        for (i in b_array.indices) {
            b_array[i] = b[i].toString().toByte()
        }
        for (i in 0 until a.length) {
            hasil[i] = (b_array[i]!!.toInt() xor a_array[i]!!.toInt()).toByte()
            plain_binary.append(hasil[i])
        }
        var i = 0
        while (i <= plain_binary.length - 8) {
            val k = plain_binary.substring(i, i + 8).toInt(2)
            plain.append(k.toChar())
            i += 8
        }
        return plain.toString()
    }
}