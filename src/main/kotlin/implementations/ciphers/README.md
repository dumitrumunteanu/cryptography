# Intro to Cryptography. Classical ciphers

### Course: Cryptography & Security
### Author: Munteanu Dumitru

----

## Theory
A classical cipher is a form of encryption used in cryptography that was once popular
but is now largely out of usage. The majority of classical ciphers may practically be
computed and solved by hand, in contrast to modern cryptographic techniques.
With contemporary technology, they may often be broken fairly easily though.
The phrase covers everything from basic systems used since Greek and Roman times to
complex Renaissance ciphers and beyond, including World War II encryption like the Enigma machine.
### Caesar Cipher
One of the simplest and most well-known encryption methods is the Caesar cipher,
often known as Caesar's cipher, the shift cipher, Caesar's code, or Caesar shift.
Each letter in the plaintext is replaced by a letter that is located a specific number
of positions farther down the alphabet in this form of substitution cipher.
With a left shift of 3, for instance, D would become A, E would become B, and so on.
Julius Caesar, who used it in his personal communications, gave it its name.

### Caesar Cipher with alphabet permutation
This is quite similar to the traditional Caesar cipher, the only difference being that it has one
more key, which swaps the letter of the alphabet.

### Vigenere Cipher
The Vigenère cipher uses a sequence of interconnected Caesar ciphers that are based on
the letters of a keyword to encrypt alphabetic text. It makes use of a polyalphabetic
replacement technique.

### Playfair Cipher
The Playfair cipher, also known as the Playfair square or the Wheatstone-Playfair cipher,
was the first literal digram substitution cipher. It is a manual symmetric encryption method.
Instead of using single letters like in the simple substitution cipher and the somewhat more
complicated Vigenère cipher systems used then, the technique encrypts pairs of letters (bigrams or digrams). Therefore, because the frequency analysis used to crack simple substitution ciphers does not work with the Playfair, it is far more difficult to crack. Bigram frequency analysis is doable but much more challenging.

## Objectives:

1. Get familiar with the basics of cryptography and classical ciphers.

2. Implement 4 types of the classical ciphers:
    - Caesar cipher with one key used for substitution (as explained above),
    - Caesar cipher with one key used for substitution, and a permutation of the alphabet,
    - Vigenere cipher,
    - Playfair cipher.
    - If you want you can implement other.

3. Structure the project in methods/classes/packages as needed.


## Implementation description

### Caesar Cipher
The transformation can be represented by aligning two alphabets; 
the cipher alphabet is the plain alphabet rotated left or right by some number of positions.
For instance, here is a Caesar cipher using a left rotation of three places, equivalent to a right
shift of 23 (the shift parameter is used as the key). When encrypted, each letter in the plain text
is replace with is corresponding letter in the rotated alphabet.
```kotlin
val encodedCharAlphabetIndex = (charAlphabetIndex + key).mod(ALPHABET.length)
```
For encryption, the alphabet rotation is performed by doing some simple calculations by adding
the 'shift' to the character order in the alphabet and using the modulo operation. For decryption,
the reverse is done.

### Caesar with alphabet permutation
This cipher is similar to the previous but instead of only rotating the alphabet, it first adds the letters
of a word at the beginning without duplicates. For example, if we used the substitution key `ANANAS`,
the alphabet we will get is:
```
ANSBCDEFGHIJKLMOPQRTUVWXYZ
```

### Vigenere Cipher
The encryption of the original text is done using the Vigenère square or Vigenère table
- The table consists of the alphabets written out 26 times in different rows,
each alphabet shifted cyclically to the left compared to the previous alphabet,
corresponding to the 26 possible Caesar Ciphers.
- At different points in the encryption process, the cipher uses a different alphabet from one of the rows.
- The alphabet used at each point depends on a repeating keyword.

The implementation is based on these relations:

Encryption  
$E_i = (P_i + K_i) mod 26$

Decryption  
$D_i = (E_i - K_i + 26) mod 26$

### Playfair Cipher
This cipher follows the following steps:

1. Generate the key Square(5×5):
   - The key square is a 5×5 grid of alphabets that acts as the key for encrypting the plaintext. Each of the 25 alphabets must be unique and one letter of the alphabet (usually J) is omitted from the table (as the table can hold only 25 alphabets). If the plaintext contains J, then it is replaced by I.

   - The initial alphabets in the key square are the unique alphabets of the key in the order in which they appear followed by the remaining letters of the alphabet in order.

2. Algorithm to encrypt the plain text: The plaintext is split into pairs of two letters (digraphs). If there is an odd number of letters, a Z is added to the last letter.

## Conclusions / Screenshots / Results
In order to test the ciphers there were written some unit tests for each of them.
There are some screenshots that can be found in the resources folder.