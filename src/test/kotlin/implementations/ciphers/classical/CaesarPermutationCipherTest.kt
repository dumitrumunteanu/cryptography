package implementations.ciphers.classical

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class CaesarPermutationCipherTest
{
    @Test
    fun testEncryptMethod()
    {
        val message = "FRUIT SALAD"
        val sut = CaesarPermutationCipher("BANANA", 7)

        val encryptedMessage = sut.encrypt(message)

        assertEquals("LYAPB ZGSGJ", encryptedMessage)
    }

    @Test
    fun testDecryptMethod()
    {
        val encryptedMessage = "FRUIT SALAD"
        val sut = CaesarPermutationCipher("BANANA", 7)

        val decryptedMessage = sut.encrypt(encryptedMessage)

        assertEquals("FRUIT SALAD", encryptedMessage)
    }
}