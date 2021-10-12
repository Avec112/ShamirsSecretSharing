# Shamir's Secret Sharing

This project is based on [Coda Hale's](https://github.com/codahale) implementation of [Shamir's Secret Sharing](https://github.com/codahale/shamir)

I have wrapped and simplified the API so it better fits my needs. I also found out that I could have static methods
for creating shares and getting the secret back. 

## class Shamir
```java
public class Shamir {

    // s: secret
    // n: share count
    // k: minimum keys to get secret
    public static List<String> getShares(String s, int n, int k) {
        // code omitted
    }

    // provide needed shares returned from getShares(..)
    public static String getSecret(String... shares) {
        // code omitted
    }
}
```

Hale's `Scheme` works with `Map<Integer, byte[]>`. I wanted to `List<String>` instead and to make this work I do this.
* Base64 encode the map `value`
* Create string with `key` + encoded `value`. Ex. `1+xxxxxxxxx`
* Base64 encode a second time
* return the list with all shares

To get the secret I just do the same the opposite way

## How to use (example)
Step 1: 
* Create five(5) shares.
* Three(3) needed to get the secret.

Step 2:
* Get secret with three(3) of the five(5) shares
```java
 class ShamirTest {

    @Test
    void test() {
        String secret = "My secret"; // expected

        List<String> shares = Shamir.getShares(secret, 5, 3); // distribute to users
        System.out.println(shares);

        String actual = Shamir.getSecret(shares.get(0), shares.get(1), shares.get(2)); // provide at least 3 shares
        System.out.println(actual);

        assertEquals(secret, expected);
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

