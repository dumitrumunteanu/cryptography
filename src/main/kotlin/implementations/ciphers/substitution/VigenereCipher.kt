package implementations.ciphers.substitution

import implementations.Cipher
import implementations.Cipher.Companion.ALPHABET

class VigenereCipher(val key: String) : Cipher
{
    override fun encrypt(message: String): String
    {
        var i = 0
        var encryptedMessage = ""
        for (character in message) {
            if (character == ' ') {
                continue
            }

            val indexOfShift = (ALPHABET.indexOf(character) + ALPHABET.indexOf(key[i])).mod(ALPHABET.length)

            encryptedMessage += ALPHABET[indexOfShift]

            i++
            if (i >= key.length) {
                i = 0
            }
        }

        return encryptedMessage
    }

    override fun decrypt(message: String): String {
        var i = 0
        var decryptedMessage = ""
        for (character in message) {
            if (character == ' ') {
                continue
            }

            val indexOfShift = (ALPHABET.indexOf(character) - ALPHABET.indexOf(key[i])).mod(ALPHABET.length)

            decryptedMessage += ALPHABET[indexOfShift]

            i++
            if (i >= key.length) {
                i = 0
            }
        }

        return decryptedMessage
    }
}