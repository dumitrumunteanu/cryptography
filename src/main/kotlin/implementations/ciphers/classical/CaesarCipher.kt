package implementations.ciphers.classical

import implementations.Cipher
import implementations.Cipher.Companion.ALPHABET

class CaesarCipher(private val key: Int) : Cipher
{
    override fun encrypt(message: String) : String
    {
        var encryptedMessage = ""
        for (character in message) {
            val encryptedChar = if (character == ' ') {
                ' '
            } else {
                val charAlphabetIndex = character.code - 65
                val encodedCharAlphabetIndex = (charAlphabetIndex + key).mod(ALPHABET.length)

                ALPHABET[encodedCharAlphabetIndex]
            }

            encryptedMessage += encryptedChar
        }

        return encryptedMessage
    }

    override fun decrypt(message: String) : String
    {
        var decryptedMessage = ""
        for (character in message) {
            val decryptedChar = if (character == ' ') {
                ' '
            } else {
                val charAlphabetIndex = character.code - 65
                val encodedCharAlphabetIndex = (charAlphabetIndex - key).mod(ALPHABET.length)

                ALPHABET[encodedCharAlphabetIndex]
            }

            decryptedMessage += decryptedChar
        }

        return decryptedMessage
    }
}