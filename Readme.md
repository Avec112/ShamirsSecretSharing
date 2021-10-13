# Shamir's Secret Sharing

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
* In: secret to encrypt and split
* In: n specifies unique shares.
* In: k specifies unique shares needed to recreate secret
* Out: List with n unique shares

Step 2 - Recreate Secret
* In: k unique shares
* Out: secret

```java
 class ShamirTest {

    @Test
    void test() {
        String secret = "My secret"; // expected
        
        int n = 5;
        int k = 3;

        List<String> shares = Shamir.getShares(secret, n, k); 
        System.out.println(shares); // list shares for distribution

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
**Note!** You cannot create shares for the same secret and expect the same shares since a random salt is used.

## Dependency needed
```xml
<dependency>
  <groupId>com.codahale</groupId>
  <artifactId>shamir</artifactId>
  <version>0.7.0</version>
</dependency>
```

