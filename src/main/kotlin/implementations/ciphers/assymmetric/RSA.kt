package implementations.ciphers.assymmetric;

import kotlin.random.Random

class RSA {
    // The modulus value
    private var n: Int = 0
    // The public key
    private var e: Int = 0
    // The private key
    private var d: Int = 0

    // Generates the keys for the RSA algorithm
    fun generateKeys() {
        // Generate two prime numbers, p and q
        val p = generatePrime()
        val q = generatePrime()

        // Calculate n = p * q
        n = p * q

        // Calculate the totient of n
        val totient = (p - 1) * (q - 1)

        // Choose an integer e such that 1 < e < totient and e is coprime to totient
        e = generateCoprime(totient)

        // Calculate the modular multiplicative inverse of e (mod totient)
        d = modularMultiplicativeInverse(e, totient)
    }

    // Encrypts the given plaintext using the RSA algorithm
    fun encrypt(plaintext: Int): Int {
        // Perform the encryption using the public key and the modulus value
        return (Math.pow(plaintext.toDouble(), e.toDouble()) % n).toInt()
    }

    // Decrypts the given ciphertext using the RSA algorithm
    fun decrypt(ciphertext: Int): Int {
        // Perform the decryption using the private key and the modulus value
        return (Math.pow(ciphertext.toDouble(), d.toDouble()) % n).toInt()
    }

    // Generates a prime number
    private fun generatePrime(): Int {
        // Generate a random number
        var num = Random.nextInt()

        // Ensure that the number is prime
        while (!isPrime(num)) {
            num = Random.nextInt()
        }

        return num
    }

    // Determines whether the given number is prime
    private fun isPrime(num: Int): Boolean {
        // Check if the number is less than 2
        if (num < 2) {
            return false
        }

        // Check if the number is divisible by any number between 2 and the square root of the number
        for (i in 2..Math.sqrt(num.toDouble()).toInt()) {
            if (num % i == 0) {
                return false
            }
        }

        return true
    }

    // Generates an integer that is coprime to the given number
    private fun generateCoprime(num: Int): Int {
        // Generate a random number
        var coprime = Random.nextInt(num)

        // Ensure that the number is coprime to num
        while (gcd(coprime, num) != 1) {
            coprime = Random.nextInt(num)
        }

        return coprime
    }

    // Calculates the greatest common divisor of two numbers
    private fun gcd(a: Int, b: Int): Int {
        if (b == 0) {
            return a
        }

        return gcd(b, a % b)
    }

    // Calculates the modular multiplicative inverse of a number (mod m)
    private fun modularMultiplicativeInverse(a: Int, m: Int): Int {
        // Calculate the greatest common divisor of a and m
        val g = gcd(a, m)

        // Ensure that a and m are coprime
        if (g != 1) {
            throw IllegalArgumentException("a and m must be coprime")
        }

        // Use the Extended Euclidean algorithm to calculate the modular multiplicative inverse
        val result = extendedEuclidean(a, m)

        // Ensure that the result is positive
        return (result % m + m) % m
    }

    // Uses the Extended Euclidean algorithm to calculate the modular multiplicative inverse of a number
    private fun extendedEuclidean(a: Int, b: Int): Int {
        var x = 0
        var y = 1
        var u = 1
        var v = 0
        var m = b
        var n = a
        var q: Int
        var r: Int

        while (n != 0) {
            q = m / n
            r = m % n
            m = n
            n = r

            val tmp = x - u * q
            x = u
            u = tmp

            val tmp2 = y - v * q
            y = v
            v = tmp2
        }

        return y
    }
}

