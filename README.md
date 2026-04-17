# Jaker - Performance-first Faker Java Library

Jaker is a small, performance-first Java faker library (v1.0.0).

## Quickstart (JitPack):

Add repository:

```xml
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>
```

Then add the dependency:

```xml
<dependency>
  <groupId>com.github.EzFramework</groupId>
  <artifactId>jaker</artifactId>
  <version>1.0.0</version>
</dependency>

**Note**: English datasets are provided as the separate module `jaker-data-en-US`. In this multi-module repository the `jaker` core depends on `jaker-data-en-US` so English data is available during local builds. Consumers using JitPack should add the `jaker-data-en-US` artifact as a dependency to include the `en-US` datasets at runtime.
```

## Usage

Basic, non-deterministic usage (system default locale):

```java
import com.github.ezframework.jaker.Faker;

public class Demo {
    public static void main(String[] args) {
        Faker faker = Faker.global();
        System.out.println(faker.name().firstName());
        System.out.println(faker.address().city());
    }
}
```

Deterministic (seeded) and specific-locale example:

```java
import com.github.ezframework.jaker.Faker;

// create a deterministic faker for en-GB
Faker seeded = Jaker.builder()
    .locale("en-GB")
    .seed(42)
    .build()
    .faker();

System.out.println(seeded.name().firstName());
```

You can also call the builder via `Faker.builder()`:

```java
Faker f = Faker.builder().locale("fr").seed(1234).build().faker();
```

## Options

- **locale**: IETF BCP 47 language tag (e.g. `en-US`, `en-GB`, `fr`, `nl`, `es`, `de`).
- **seed**: a `long` value for deterministic output across runs (useful in tests).
- **dataset packs**: the `DataLoader` prefers compact `.bin` packs when present next to text datasets; otherwise it falls back to newline text files.

## Available providers

The core `Faker` exposes the following providers:

- `name()` — person names (e.g. `firstName()`)
- `address()` — city, street, zip code (`zipCode()`)
- `company()` — company names
- `internet()` — domains, emails
- `finance()` — credit card numbers
- `dateTime()` — simple date/time helpers
- `text()` — lorem ipsum text (`sentence()`, `sentence(wordAmount)`, `word()`)
- `bank()` — safe example IBANs, IBAN format templates, and `unsafe().iban()` synthetic generation
- `number()` — numeric helpers (`numberBetween()`, `random()`, ranged values)
- `person()` — person utilities (`firstname()`, `lastname()`, `dob()`, `uuid()`, timestamps, `phone()`)
- `server()` — server hostnames plus private IPv4/IPv6 addresses
- `file()` — file metadata (`name()`, `extension()`, `size()`)
- `gameServer()` — game server metadata (`game()`, `ip()`, `version()`)
- `fruit()` — fruit metadata (`name()`, `category()`)
- `animal()` — animal metadata (`name()`, `category()`, `gender()`)
- `country()` — country metadata (`name()`, `code()`)
- `unique()` — non-repeating value providers (e.g. `unique().name().firstName()`)

## Person & Number provider examples

The `PersonProvider` and `NumberProvider` provide convenient helpers for simple person data and numeric utilities. Example:

```java
Faker faker = Jaker.builder().locale("en-US").seed(42).build().faker();
System.out.println(faker.person().firstname() + " " + faker.person().lastname() + " born " + faker.person().dob());
System.out.println("uuid=" + faker.person().uuid());
System.out.println("created=" + faker.person().createdTimestamp());
System.out.println("updated=" + faker.person().updatedTimestamp());
System.out.println("source=" + faker.person().source());
System.out.println("safe phone=" + faker.person().phone());
System.out.println("unsafe phone=" + faker.person().unsafe().phone());
System.out.println("Full name: " + faker.person().fullname());
System.out.println("Random number 1-10: " + faker.number().numberBetween(1, 10));
System.out.println("Random double: " + faker.number().random());
System.out.println("Random int below 100: " + faker.number().random(100));
System.out.println("Random double in range: " + faker.number().random(10.0, 20.0));
System.out.println("Email for local part: " + faker.internet().email("qa.user"));
System.out.println("Email with custom domain: " + faker.internet().email("qa.user", "example.org"));
System.out.println("Date in last year: " + faker.dateTime().isoDate(1));
System.out.println("Host: " + faker.server().hostname());
System.out.println("Host (custom domain): " + faker.server().hostname("internal.example"));
System.out.println("Host (custom prefix + domain): " + faker.server().hostname("api", "internal.example"));
System.out.println("IPv4: " + faker.server().ip());
System.out.println("IPv6: " + faker.server().ip(true));
System.out.println("Ranged file size: " + faker.file().size(1024, 4096));
```

```java
// Generate a count of full names
Faker faker2 = Jaker.builder().locale("en-US").seed(42).build().faker();
int count = 5;
for (int i = 0; i < count; i++) {
  System.out.println(faker2.person().fullname());
}
```

`PersonProvider` will prefer localized `names`/`surnames`/`phones` datasets when present and fall back to sensible defaults when absent. Phone numbers returned by `person().phone()` are intended as safe examples from locale data packs, while `person().unsafe().phone()` generates locale-shaped random synthetic values.

## Including locale data


The core `jaker` module contains the code. Locale data is shipped as separate artifacts (e.g. `jaker-data-en-US`) so you can include only the locales you need. Add the JitPack repository (see Quickstart above) and include any locale artifact you need. Example dependencies (using JitPack):

```xml
<dependency>
  <groupId>com.github.EzFramework</groupId>
  <artifactId>jaker</artifactId>
  <version>1.0.0</version>
</dependency>
<!-- Include the English locale pack if you need en-US datasets at runtime -->
<dependency>
  <groupId>com.github.EzFramework</groupId>
  <artifactId>jaker-data-en-US</artifactId>
  <version>1.0.0</version>
</dependency>
```

For other locales, replace the artifactId with the desired pack, e.g. `jaker-data-fr`, `jaker-data-de`, etc. Example for French:

```xml
<dependency>
  <groupId>com.github.EzFramework</groupId>
  <artifactId>jaker-data-fr</artifactId>
  <version>1.0.0</version>
</dependency>
```

## Notes

- The library is implemented with minimal runtime dependencies and is designed for use in tests and light-weight production scenarios.
- The project targets Java 25 in the POM; adjust your build if you need wider compatibility.
- If a locale's dataset is not present on the classpath, the builder will throw `LocaleNotSupportedException` to make missing locale errors explicit. Include the appropriate locale pack (via JitPack) or fall back to a supported locale.
 
## Checkstyle

This repository includes a `checkstyle.xml` configuration at the repo root and a Maven Checkstyle plugin wired in the parent `pom.xml`.

- Generate a Checkstyle report (report only by default):

```bash
mvn -Dcheckstyle.skip=false -DskipTests=true verify
```

- To fail the build on violations, set `failsOnError` to `true` in the `maven-checkstyle-plugin` configuration in the root `pom.xml`, or run the `check` goal directly:

```bash
mvn -Dcheckstyle.skip=false -DskipTests=true -pl jaker -am org.apache.maven.plugins:maven-checkstyle-plugin:3.1.2:check
```

Checkstyle reports are written under each module's `target/site` directory as `checkstyle.html`/`checkstyle-result.xml`.

## Supported Locales

The following locale packs are available in this repository. Include the artifact for any locale you need at runtime:

| Locale | Language / Region | Artifact | Notes |
|---|---|---|---|
| `en-US` | English (United States) | `jaker-data-en-US` | Default, wide coverage |
| `en-GB` | English (United Kingdom) | `jaker-data-en-GB` | British variants (spellings, addresses) |
| `fr` | French (France) | `jaker-data-fr` | France-localized names and addresses |
| `nl` | Dutch (Netherlands) | `jaker-data-nl` | Netherlands locale data with banking coverage |
| `es` | Spanish (Spain) | `jaker-data-es` | Spain-localized datasets with banking coverage |
| `de` | German (Germany) | `jaker-data-de` | Germany-localized datasets with banking coverage |

For more examples, see `jaker-example` and the tests in `jaker/src/test/java`.

## Included data

### Dataset families available

Jaker currently ships these dataset families (loaded from locale packs).

- `names` — localized name examples (safe)
- `surnames` — localized surname examples (safe)
- `streets` — localized street names (safe)
- `cities` — localized city names (safe)
- `domains` — example domains (safe)
- `phones` — localized safe example phone numbers (`phones.txt` + optional `phones.bin`); synthetic values available via `person().unsafe().phone()`
- `company_names` — localized company names (safe)
- `creditcards` — example/test card numbers (safe fallback present); synthetic Luhn-valid numbers available via `finance().unsafe().creditCardNumber()`
- `countries` — localized country data (safe)
- `ibans` — safe example IBANs taken from public bank/payment documentation (available in locales with banking coverage); synthetic IBAN-like values available via `bank().unsafe().iban()`
- `bank_formats` — IBAN format templates (available in locales with banking coverage)
- `animals` (currently `en-US`) — animal names (safe)
- `files` (currently `en-US`) — file metadata examples (safe)
- `fruits` (currently `en-US`) — fruit names (safe)
- `game_servers` (currently `en-US`) — game server metadata (safe)

### Dataset availability by locale

| Locale | Artifact | Datasets present |
|---|---|---|
| `en-US` | `jaker-data-en-US` | `animals`, `bank_formats`, `cities`, `company_names`, `countries`, `creditcards`, `domains`, `files`, `fruits`, `game_servers`, `ibans`, `names`, `phones`, `streets`, `surnames` |
| `en-GB` | `jaker-data-en-GB` | `bank_formats`, `cities`, `company_names`, `countries`, `creditcards`, `domains`, `files`, `fruits`, `game_servers`, `ibans`, `names`, `phones`, `streets`, `surnames` |
| `fr` | `jaker-data-fr` | `bank_formats`, `cities`, `company_names`, `countries`, `creditcards`, `domains`, `files`, `fruits`, `game_servers`, `ibans`, `names`, `phones`, `streets`, `surnames` |
| `de` | `jaker-data-de` | `bank_formats`, `cities`, `company_names`, `countries`, `creditcards`, `domains`, `files`, `fruits`, `game_servers`, `ibans`, `names`, `phones`, `streets`, `surnames` |
| `es` | `jaker-data-es` | `bank_formats`, `cities`, `company_names`, `countries`, `creditcards`, `domains`, `files`, `fruits`, `game_servers`, `ibans`, `names`, `phones`, `streets`, `surnames` |
| `nl` | `jaker-data-nl` | `bank_formats`, `cities`, `company_names`, `countries`, `creditcards`, `domains`, `files`, `fruits`, `game_servers`, `ibans`, `names`, `phones`, `streets`, `surnames` |

### Approximate total rows across all locale packs

Current bundled dataset sizes (total non-empty rows across all `jaker-data-*` text datasets):

| Data type | Total rows | Expandable | Uses generation | Total possibilities |
|---|---:|:---:|:---:|---:|
| `names` | 1,483 + | Yes | No | 1,483 |
| `surnames` | 1,289 + | Yes | No | 1,289 |
| `streets` | 1,496 + | Yes | No | 1,496 |
| `cities` | 1,184 + | Yes | No | 1,184 |
| `domains` | 174 + | Yes | Yes | 258,042+ |
| `phones` | 69 + | Yes | Yes | 1,000,000,000+ |
| `company_names` | 3,000 + | Yes | No | 3,000 |
| `creditcards` | 60 + | Yes | Yes | 10,000,000,000,000+ |
| `ibans` | 60 + | Yes | Yes | 19,400 |
| `bank_formats` | 60 + | Yes | Yes | 308,915,776,000 |
| `animals` | 115 + | Yes | Yes | 1,520,875 (115³) |
| `countries` | 1,494 + | Yes | No | 1,494 |
| `files` | 15 + | Yes | Yes | 3,375 (15³) |
| `fruits` | 64 + | Yes | Yes | 4,096 (64²) |
| `game_servers` | 10 + | Yes | Yes | 1,000 (10³) |

`+` indicates the dataset can be expanded in future locale/data-pack releases. For generated providers (`Yes`), totals include synthetic methods such as `unsafe()` APIs and are approximate lower bounds where random numeric/string spaces are involved.


## Running the example

Build and run the `jaker-example` module (recommended via Maven so the classpath is assembled automatically):

```bash
# Build the example and its dependencies
mvn -pl jaker-example -am package

# Run the example using the Maven Exec plugin (recommended)
mvn -pl jaker-example -am exec:java -Dexec.mainClass="com.github.ezframework.jakerexample.Demo"
```

Alternatively, after packaging you can run the example main class directly when you assemble the module jars on the classpath:

```bash
mvn -pl jaker-example -am package
java -cp jaker-example/target/jaker-example-1.0.0.jar:jaker/target/jaker-1.0.0.jar:jaker-data-en-US/target/jaker-data-en-US-1.0.0.jar \
  com.github.ezframework.jakerexample.Demo
```

Example output (truncated):

```
Name: Alice
Email: alice@example.com
Company: Acme Corp
City: New York
CreditCard: 4111-1111-1111-1111
IBAN: DE1200000000000012
Person: Alice Doe
DOB: 1987-05-12
Random 1-100: 42
Random double: 0.123456789
```

## Continuous integration & coverage

This project includes dedicated GitHub Actions workflows for CI and coverage:

- `.github/workflows/ci.yml` for cross-platform build/test/benchmark validation.
- `.github/workflows/coverage.yml` for focused JaCoCo report generation and artifact publishing on each push and pull request.

- The parent POM configures the `jacoco-maven-plugin` to prepare the agent, generate HTML reports under `target/site/jacoco`, and perform a coverage check.
- By default the coverage policy is permissive (minimum ratio configured via the `jacoco.minimum` property). Override it in CI or locally: `-Djacoco.minimum=0.2`.

CI uploads the generated JaCoCo HTML reports and `jacoco.exec` artifacts to the workflow run so you can download and inspect them.

Locally run coverage and check with:

```bash
mvn -Djacoco.minimum=0.0 verify
```

## Benchmarks

This repository includes a JMH benchmark module `jaker-bench` that exercises common faker operations (name generation, person assembly, numeric generation, and faker creation) for both Jaker-only and cross-library comparisons.

The benchmark classes include an `@Param` locale axis (`en-US`, `en-GB`) so each benchmark is measured across multiple locale datasets.

Build the bench module and produce an executable shaded JAR:

```bash
mvn -pl jaker-bench -am -DskipTests=true package
```

Run the benchmarks (example options shown):

```bash
# warmup 3, iterations 5, forks 2
java -jar jaker-bench/target/jaker-bench-1.0.0-shaded.jar -wi 3 -i 5 -f 2
```

For a quick smoke-run (useful in CI or to verify the jar runs):

```bash
java -jar jaker-bench/target/jaker-bench-1.0.0-shaded.jar -wi 0 -i 1 -f 1
```

Benchmarks are implemented using JMH; consult `jaker-bench/src/main/java/com/github/ezframework/jaker/bench/FakerBench.java` for details and add more scenarios as needed.

### Benchmark summary

We written a benchmark module in the Jaker repository to make sure it stays performance-first. Below the last benchmark of our run on Ubuntu + JDK 17.

- Run: 3 warmups × 3 measurements (10s each)
- Key throughput (ops/s):
  - firstName: Jaker 19,372,388.15 vs DataFaker 1,609,515.99 vs JavaFaker 239,574.58
  - fullPerson: Jaker 2,234,038.31 vs DataFaker 1,060,180.89 vs JavaFaker 176,240.79
  - numberBetween: Jaker 220,305,435.10 vs DataFaker 119,992,411.45 vs JavaFaker 2,492,076.58

Note on JVM version
-------------------

This project targets Java 25 (see parent POM). The shaded bench JAR and the generated JMH classes are compiled for Java 25 and therefore require a Java 25 runtime to execute. If your system default `java` is an older JDK (for example Java 17), running the bench JAR will fail with an UnsupportedClassVersionError. To run benchmarks using a specific JDK install, invoke the desired `java` executable directly:

```bash
/path/to/jdk-25/bin/java -jar jaker-bench/target/jaker-bench-1.0.0.jar -wi 3 -i 5 -f 2
```

If you cannot install Java 25, an alternative is to rebuild the project with a lower `maven.compiler.release` (for example 17) but note this changes the compilation target for all modules and may not match the intended Java support of the library.

An aggregated HTML coverage report for the entire multi-module reactor is produced at `target/site/jacoco-aggregate` (root project) when running `mvn verify`. The GitHub Actions workflow uploads this aggregated report as the `jacoco-aggregate-report` artifact.
