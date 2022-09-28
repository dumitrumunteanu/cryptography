package implementations

interface Cipher {

    fun encrypt(message: String): String
    fun decrypt(message: String): String

    companion object {
        const val ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    }
}