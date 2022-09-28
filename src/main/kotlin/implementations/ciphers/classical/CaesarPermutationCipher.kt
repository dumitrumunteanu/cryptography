package implementations.ciphers.classical

import implementations.Cipher
import implementations.Cipher.Companion.ALPHABET

class CaesarPermutationCipher(val substitutionKey: String, val permutationKey: Int) : Cipher {
    private val alphabet = getSubstitutionAlphabet()

    override fun encrypt(message: String): String
    {
        var encryptedMessage = ""
        for (character in message) {
            val encryptedChar = if (character == ' ') {
                ' '
            } else {
                val charAlphabetIndex = character.code - 65
                val encodedCharAlphabetIndex = (charAlphabetIndex + permutationKey).mod(alphabet.length)

                alphabet[encodedCharAlphabetIndex]
            }

            encryptedMessage += encryptedChar
        }

        return encryptedMessage
    }

    override fun decrypt(message: String): String
    {
        var encryptedMessage = ""
        for (character in message) {
            val encryptedChar = if (character == ' ') {
                ' '
            } else {
                val charAlphabetIndex = character.code - 65
                val encodedCharAlphabetIndex = (charAlphabetIndex - permutationKey).mod(alphabet.length)

                alphabet[encodedCharAlphabetIndex]
            }

            encryptedMessage += encryptedChar
        }

        return encryptedMessage
    }

    private fun getSubstitutionAlphabet(): String
    {
        return getStringWithoutDuplicates(substitutionKey + ALPHABET)
    }

    private fun getStringWithoutDuplicates(string: String) : String
    {
        return string.toCharArray().distinct().joinToString(separator = "")
    }
}