package implementations.ciphers.classical

import implementations.ciphers.substitution.VigenereCipher
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class VigenereCipherTest {

    @Test
    fun testEncryptMethod()
    {
        val message = "PER ASPERA AD ASTRA"
        val key = "SUPER"
        val sut = VigenereCipher(key)

        val encryptedMessage = sut.encrypt(message)

        assertEquals("HYGEJHYGERVUHXIS", encryptedMessage)
    }

    @Test
    fun testDecryptMethod()
    {
        val encryptedMessage = "NWSIZIJWVOKXBXVWO";
        val key = "VIGENERE"
        val sut = VigenereCipher(key)

        val decryptedMessage = sut.decrypt(encryptedMessage)

        assertEquals("SOMEMESSAGETOTEST", decryptedMessage)
    }
}