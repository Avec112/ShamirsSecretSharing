# Shamir's Secret Sharing
![Build Status](https://img.shields.io/github/workflow/status/Avec112/ShamirsSecretSharing/Build?logo=github)
![CodeQL](https://img.shields.io/github/workflow/status/Avec112/ShamirsSecretSharing/CodeQL?label=CodeQL&logo=github)
![GitHub license](https://img.shields.io/github/license/avec112/ShamirsSecretSharing)
![GitHub last commit](https://img.shields.io/github/last-commit/Avec112/ShamirsSecretSharing)
<!-- ![GitHub tag](https://img.shields.io/github/tag/avec112/ShamirsSecretSharing) -->


This project is based on [Coda Hale's](https://github.com/codahale) implementation of [Shamir's Secret Sharing](https://github.com/codahale/shamir)

I have wrapped and simplified the API so it better fits my needs. I also found out that I could have static methods
for creating shares and recreate secret. 

## class Shamir
```java
public class Shamir {

    // s: secret
    // n: share count
    // k: minimum keys to recreate secret
    public static List<String> getShares(String s, int n, int k) {
        // code omitted
    }

    // provide k unique shares to recreate secret
    public static String getSecret(String... shares) {
        // code omitted
    }
}
```

Hale's `Scheme` use `Map<Integer, byte[]>`. I wanted something simpler and am now using `List<String>` instead by doing the following:
* Base64 encode the map `var firstEncoding = encode(value)`
* Concatenate the maps key and encoded value like this `key` + `firstEncoding`. Ex. `1+tRMnJCzlIxaDFSRmM=`
* Base64 encode a second time
* return the list with all shares

To get the secret I just do the opposite

## How to use (example)
Step 1 - Create Shares
* -> secret to encrypt and split
* -> n specifies unique shares.
* -> k specifies unique shares needed to recreate secret
* <- List with n unique shares

Step 2 - Recreate Secret
* -> k unique shares
* <- secret

```java
 class ShamirTest {

    @Test
    void test() {
        String secret = "My secret";
        
        int n = 5;
        int k = 3;
        
        // Create shares
        List<String> shares = Shamir.getShares(secret, n, k); 
        System.out.println(shares); // list shares for distribution

        // Bring back secret
        String actual = Shamir.getSecret(shares.get(0), shares.get(1), shares.get(2)); // provide at least k unique shares
        System.out.println(actual);

        assertEquals(secret, actual);
    }
}
```

Example output
```shell
[MStqZDB4NUpwRHdzenM=, MitnKzdRK3VKQlpiakU=, MytRMHJCYlIxaDFSRmM=, NCtoWEcrdk9PMDk2SlY=, NStSZFd2S3h5VVJ3dk4=]
My secret
```

## Dependency needed
```xml
<dependency>
  <groupId>com.codahale</groupId>
  <artifactId>shamir</artifactId>
  <version>0.7.0</version>
</dependency>
```

