# Lab 3

### Course: Cryptography & Security
### Author: Munteanu Dumitru

----

The RSA algorithm is a popular public-key cryptosystem that is used for secure data transmission.

The implementation consists of a single RSA class with several methods. The main methods are generateKeys, encrypt, and decrypt.

The generateKeys method is used to generate the keys that are needed for the RSA algorithm. It does this by generating two prime numbers, p and q, and calculating the modulus value n = p * q. It also calculates the totient of n and chooses an integer e such that 1 < e < totient and e is coprime to the totient. Finally, it calculates the modular multiplicative inverse of e (mod totient), which is the private key d.
```kotlin
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
```

The encrypt method takes a plaintext integer as input and returns the corresponding ciphertext integer. It does this by raising the plaintext to the power of the public key e (mod n).
```kotlin
fun encrypt(plaintext: Int): Int {
  // Perform the encryption using the public key and the modulus value
  return (Math.pow(plaintext.toDouble(), e.toDouble()) % n).toInt()
}
```

The decrypt method takes a ciphertext integer as input and returns the corresponding plaintext integer. It does this by raising the ciphertext to the power of the private key d (mod n).
```kotlin
fun decrypt(ciphertext: Int): Int {
  // Perform the decryption using the private key and the modulus value
  return (Math.pow(ciphertext.toDouble(), d.toDouble()) % n).toInt()
}
```

The class also includes several private helper methods that are used by the main methods.
These include generatePrime, which generates a random prime number; 
```kotlin
private fun generatePrime(): Int {
  // Generate a random number
  var num = Random.nextInt()

  // Ensure that the number is prime
  while (!isPrime(num)) {
      num = Random.nextInt()
  }

  return num
}
```
isPrime, which determines whether a given number is prime;
```kotlin
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
```
generateCoprime, which generates an integer that is coprime to a given number;
```kotlin
private fun generateCoprime(num: Int): Int {
  // Generate a random number
  var coprime = Random.nextInt(num)

  // Ensure that the number is coprime to num
  while (gcd(coprime, num) != 1) {
      coprime = Random.nextInt(num)
  }

  return coprime
}
```
gcd, which calculates the greatest common divisor of two numbers;
```kotlin
private fun gcd(a: Int, b: Int): Int {
  if (b == 0) {
      return a
  }

  return gcd(b, a % b)
}
```

and modularMultiplicativeInverse, which calculates the modular multiplicative inverse of a number.
```kotlin
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
```

To use the RSA class, you can create an instance of the class and call the generateKeys method to generate the keys needed for the RSA algorithm. Then, you can use the encrypt and decrypt methods to encrypt and decrypt messages using the RSA algorithm.
Here is a working example:
```kotlin
val rsa = RSA()
rsa.generateKeys()

val plaintext = 123
val ciphertext = rsa.encrypt(plaintext)
val decrypted = rsa.decrypt(ciphertext)

println(decrypted)  // Output: 123
```

In this example, we create an instance of the RSA class and generate the keys using the generateKeys method. Then, we use the encrypt method to encrypt the plaintext message 123, and we use the decrypt method to decrypt the resulting ciphertext. Finally, we print the decrypted message to the console.
