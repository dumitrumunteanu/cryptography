package implementations.ciphers.substitution

import implementations.Cipher
import implementations.Cipher.Companion.ALPHABET

class PlayfairCipher(keyword: String) : Cipher {
    private val table: Array<CharArray> = Array(5) { CharArray(5) }

    init {
        // build table
        val used = BooleanArray(26)
        used[9]  = true  // J used
        val alphabet = keyword + ALPHABET
        var i = 0
        var j = 0
        var c: Char
        var d: Int
        for (character in alphabet) {
            c = character
            if (c !in 'A'..'Z') continue
            d = c.toInt() - 65
            if (!used[d]) {
                table[i][j] = c
                used[d] = true
                if (++j == 5) {
                    if (++i == 5) break
                    j = 0
                }
            }
        }
    }

    override fun encrypt(message: String): String
    {
        val cleanText = getCleanText(message)
        var encryptedMessage = ""
        val length = cleanText.length
        for (i in 0 until length step 2) {
            val (row1, col1) = findChar(cleanText[i])
            val (row2, col2) = findChar(cleanText[i + 1])
            encryptedMessage += when {
                row1 == row2 -> table[row1][(col1 + 1) % 5].toString() + table[row2][(col2 + 1) % 5]
                col1 == col2 -> table[(row1 + 1) % 5][col1].toString() + table[(row2 + 1) % 5][col2]
                else         -> table[row1][col2].toString() + table[row2][col1]
            }
            if (i < length - 1) encryptedMessage += " "
        }

        return encryptedMessage.trim()
    }

    override fun decrypt(message: String): String
    {
        var decryptedMessage = ""
        val length = message.length
        for (i in 0 until length step 3) {
            val (row1, col1) = findChar(message[i])
            val (row2, col2) = findChar(message[i + 1])
            decryptedMessage += when {
                row1 == row2 -> table[row1][if (col1 > 0) col1 - 1 else 4].toString() + table[row2][if (col2 > 0) col2 - 1 else 4]
                col1 == col2 -> table[if (row1 > 0) row1- 1 else 4][col1].toString() + table[if (row2 > 0) row2 - 1 else 4][col2]
                else         -> table[row1][col2].toString() + table[row2][col1]
            }
            if (i < length - 1) decryptedMessage += " "
        }

        return decryptedMessage.trim()
    }

    private fun getCleanText(message: String): String {
        var cleanText = ""
        var prevChar = '\u0000'
        var nextChar: Char
        for (character in message) {
            nextChar = character
            if (nextChar !in 'A'..'Z') continue
            if (nextChar == 'J') nextChar = 'I'
            if (nextChar != prevChar)
                cleanText += nextChar
            else
                cleanText += "X$nextChar"
            prevChar = nextChar
        }
        val len = cleanText.length
        if (len % 2 == 1)  {
            cleanText += if (cleanText[len - 1] != 'X')
                'X'
            else
                'Z'
        }

        return cleanText
    }

    private fun findChar(c: Char): Pair<Int, Int> {
        for (i in 0..4)
            for (j in 0..4)
                if (table[i][j] == c) return Pair(i, j)

        return Pair(-1, -1)
    }
}
