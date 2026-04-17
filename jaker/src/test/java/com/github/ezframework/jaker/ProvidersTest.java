package com.github.ezframework.jaker;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ProvidersTest {

    @Test
    void personAndNumberProvidersWork() {
        final Faker faker = Jaker.builder().locale("en-US").seed(42).build().faker();
        assertNotNull(faker);

        final String first = faker.person().firstname();
        assertNotNull(first);
        assertFalse(first.isBlank());

        final String last = faker.person().lastname();
        assertNotNull(last);
        assertFalse(last.isBlank());

        final String dob = faker.person().dob();
        assertNotNull(dob);
        assertDoesNotThrow(() -> java.time.LocalDate.parse(dob));

        final String uuid = faker.person().uuid();
        assertNotNull(uuid);
        assertDoesNotThrow(() -> java.util.UUID.fromString(uuid));

        final String createdTimestamp = faker.person().createdTimestamp();
        assertNotNull(createdTimestamp);
        assertDoesNotThrow(() -> java.time.Instant.parse(createdTimestamp));

        final String updatedTimestamp = faker.person().updatedTimestamp();
        assertNotNull(updatedTimestamp);
        assertDoesNotThrow(() -> java.time.Instant.parse(updatedTimestamp));

        final String source = faker.person().source();
        assertEquals("jaker", source);

        final String safePhone = faker.person().phone();
        assertNotNull(safePhone);
        assertFalse(safePhone.isBlank());
        assertTrue(safePhone.matches("^1\\d{10}$"));

        final String unsafePhone = faker.person().unsafe().phone();
        assertNotNull(unsafePhone);
        assertFalse(unsafePhone.isBlank());
        assertTrue(unsafePhone.matches("^1\\d{10}$"));

        final int n = faker.number().numberBetween(1, 10);
        assertTrue(n >= 1 && n <= 10);

        final double d = faker.number().random();
        assertTrue(d >= 0.0 && d < 1.0);

        final String zip = faker.address().zipCode();
        assertNotNull(zip);
        assertTrue(zip.matches("^\\d{5}$"));
    }

    @Test
    void personUnsafePhoneRespectsLocaleShape() {
        final Faker fakerGb = Jaker.builder().locale("en-GB").seed(42).build().faker();
        assertTrue(fakerGb.person().phone().matches("^44\\d{10}$"));
        assertTrue(fakerGb.person().unsafe().phone().matches("^44\\d{10}$"));

        final Faker fakerFr = Jaker.builder().locale("fr").seed(42).build().faker();
        assertTrue(fakerFr.person().phone().matches("^33\\d{9}$"));
        assertTrue(fakerFr.person().unsafe().phone().matches("^33\\d{9}$"));

        final Faker fakerDe = Jaker.builder().locale("de").seed(42).build().faker();
        assertTrue(fakerDe.person().phone().matches("^49\\d{9,11}$"));
        assertTrue(fakerDe.person().unsafe().phone().matches("^49\\d{9}$"));

        final Faker fakerEs = Jaker.builder().locale("es").seed(42).build().faker();
        assertTrue(fakerEs.person().phone().matches("^34\\d{9}$"));
        assertTrue(fakerEs.person().unsafe().phone().matches("^34\\d{9}$"));

        final Faker fakerNl = Jaker.builder().locale("nl").seed(42).build().faker();
        assertTrue(fakerNl.person().phone().matches("^31\\d{9}$"));
        assertTrue(fakerNl.person().unsafe().phone().matches("^31\\d{9}$"));
    }

    @Test
    void zipCodeSupportsLocaleFormats() {
        final Faker fakerGb = Jaker.builder().locale("en-GB").seed(42).build().faker();
        final String gbZip = fakerGb.address().zipCode();
        assertNotNull(gbZip);
        assertTrue(gbZip.matches("^[A-Z]{2}\\d \\d[A-Z]{2}$"));

        final Faker fakerNl = Jaker.builder().locale("nl").seed(42).build().faker();
        final String nlZip = fakerNl.address().zipCode();
        assertNotNull(nlZip);
        assertTrue(nlZip.matches("^\\d{4} [A-Z]{2}$"));
    }

    @Test
    void serverProviderProducesHostnameAndIp() {
        final Faker faker = Jaker.builder().locale("en-US").seed(42).build().faker();
        assertNotNull(faker);

        final String host = faker.server().hostname();
        assertNotNull(host);
        assertTrue(host.startsWith("server-"));
        assertTrue(host.contains("."));
        assertTrue(host.matches("^server-\\d{4}\\..+$"));

        final String customDomainHost = faker.server().hostname("internal.example");
        assertNotNull(customDomainHost);
        assertTrue(customDomainHost.startsWith("server-"));
        assertTrue(customDomainHost.endsWith(".internal.example"));

        final String ip = faker.server().ip();
        assertNotNull(ip);
        final String[] parts = ip.split("\\.");
        assertEquals(4, parts.length);
        for (final String part : parts) {
            final int val = Integer.parseInt(part);
            assertTrue(val >= 0 && val <= 255);
        }

        final String ipv6 = faker.server().ip(true);
        assertNotNull(ipv6);
        final String[] v6parts = ipv6.split(":");
        assertEquals(8, v6parts.length);
        for (final String part : v6parts) {
            assertFalse(part.isBlank());
            assertTrue(part.length() <= 4);
        }
    }


    @Test
    void newDataTypeProvidersProduceValues() {
        final Faker faker = Jaker.builder().locale("en-US").seed(42).build().faker();
        assertNotNull(faker);

        final String fileName = faker.file().name();
        final String extension = faker.file().extension();
        final long size = faker.file().size();

        assertNotNull(fileName);
        assertFalse(fileName.isBlank());
        assertNotNull(extension);
        assertFalse(extension.isBlank());
        assertTrue(size > 0);

        final String game = faker.gameServer().game();
        final String gameIp = faker.gameServer().ip();
        final String version = faker.gameServer().version();

        assertNotNull(game);
        assertFalse(game.isBlank());
        assertNotNull(version);
        assertFalse(version.isBlank());

        final String[] gameIpParts = gameIp.split("\\.");
        assertEquals(4, gameIpParts.length);

        final String fruitName = faker.fruit().name();
        final String fruitCategory = faker.fruit().category();

        assertNotNull(fruitName);
        assertFalse(fruitName.isBlank());
        assertNotNull(fruitCategory);
        assertFalse(fruitCategory.isBlank());

        final String animalName = faker.animal().name();
        final String animalCategory = faker.animal().category();
        final String animalGender = faker.animal().gender();
        final String countryName = faker.country().name();
        final String countryCode = faker.country().code();

        assertNotNull(animalName);
        assertFalse(animalName.isBlank());
        assertNotNull(animalCategory);
        assertFalse(animalCategory.isBlank());
        assertNotNull(animalGender);
        assertFalse(animalGender.isBlank());
        assertNotNull(countryName);
        assertFalse(countryName.isBlank());
        assertNotNull(countryCode);
        assertTrue(countryCode.matches("^[A-Z]{2}$"));
    }

    @Test
    void uniqueNameProviderReturnsNonRepeatingValues() {
        final Faker faker = Jaker.builder().locale("en-US").seed(42).build().faker();
        assertNotNull(faker);

        final Set<String> names = Set.of(
            faker.unique().name().firstName(),
            faker.unique().name().firstName(),
            faker.unique().name().firstName(),
            faker.unique().name().firstName(),
            faker.unique().name().firstName()
        );
        assertEquals(5, names.size());
    }

    @Test
    void uniqueNameProviderThrowsWhenExhausted() {
        final Faker faker = Jaker.builder().locale("en-US").seed(42).build().faker();
        assertNotNull(faker);

        final Set<String> seen = new HashSet<>();
        IllegalStateException thrown = null;
        final int maxAttempts = 20_000;

        for (int i = 0; i < maxAttempts; i++) {
            try {
                final String next = faker.unique().name().firstName();
                assertTrue(seen.add(next), "Expected unique value before exhaustion");
            } catch (final IllegalStateException e) {
                thrown = e;
                break;
            }
        }

        assertNotNull(thrown, "Expected unique name provider to eventually exhaust dataset");
        assertEquals(
            "No unique first names remaining for locale en-US",
            thrown.getMessage()
        );
    }

    @Test
    void uniqueNameProviderCanBeReset() {
        final Faker faker = Jaker.builder().locale("en-US").seed(42).build().faker();
        assertNotNull(faker);

        final List<String> firstCycle = new ArrayList<>();
        IllegalStateException firstExhaustion = null;

        while (firstExhaustion == null) {
            try {
                firstCycle.add(faker.unique().name().firstName());
            } catch (final IllegalStateException e) {
                firstExhaustion = e;
            }
        }

        assertFalse(firstCycle.isEmpty());
        assertEquals(
            "No unique first names remaining for locale en-US",
            firstExhaustion.getMessage()
        );

        faker.unique().name().reset();

        final String firstAfterReset = faker.unique().name().firstName();
        assertTrue(firstCycle.contains(firstAfterReset));

        int secondCycleCount = 1;
        IllegalStateException secondExhaustion = null;
        while (secondExhaustion == null) {
            try {
                faker.unique().name().firstName();
                secondCycleCount++;
            } catch (final IllegalStateException e) {
                secondExhaustion = e;
            }
        }

        assertEquals(firstCycle.size(), secondCycleCount);
        assertEquals(
            "No unique first names remaining for locale en-US",
            secondExhaustion.getMessage()
        );
    }

    @Test
    void uniqueTrackingStateIsIsolatedPerJakerInstance() {
        final Faker fakerOne = Jaker.builder().locale("en-US").seed(42).build().faker();
        final Faker fakerTwo = Jaker.builder().locale("en-US").seed(42).build().faker();

        final String oneFirst = fakerOne.unique().name().firstName();
        final String twoFirst = fakerTwo.unique().name().firstName();
        assertEquals(oneFirst, twoFirst);

        final String oneSecond = fakerOne.unique().name().firstName();
        fakerOne.unique().name().firstName();

        final String twoSecond = fakerTwo.unique().name().firstName();
        assertEquals(oneSecond, twoSecond);
    }

    @Test
    void textProviderSupportsSentenceWordAmountAndWord() {
        final Faker faker = Jaker.builder().locale("en-US").seed(42).build().faker();
        assertNotNull(faker);

        final String sentence = faker.text().sentence();
        assertNotNull(sentence);
        assertTrue(sentence.endsWith("."));

        final String sentenceWithEightWords = faker.text().sentence(8);
        assertNotNull(sentenceWithEightWords);
        assertTrue(sentenceWithEightWords.endsWith("."));
        final int wordCount = sentenceWithEightWords.substring(0, sentenceWithEightWords.length() - 1).split("\\s+").length;
        assertEquals(8, wordCount);

        final String word = faker.text().word();
        assertNotNull(word);
        assertFalse(word.isBlank());

        assertThrows(IllegalArgumentException.class, () -> faker.text().sentence(0));
    }

    @Test
    void companyProviderSupportsBulkGeneration() {
        final Faker faker = Jaker.builder().locale("en-US").seed(42).build().faker();
        assertNotNull(faker);

        final String single = faker.company().companyName();
        assertNotNull(single);
        assertFalse(single.isBlank());

        final java.util.List<String> names = faker.company().companyNames(5);
        assertEquals(5, names.size());
        assertTrue(names.stream().noneMatch(String::isBlank));

        assertThrows(IllegalArgumentException.class, () -> faker.company().companyNames(0));
    }

    @Test
    void numberProviderSupportsAdditionalOptions() {
        final Faker faker = Jaker.builder().locale("en-US").seed(42).build().faker();
        assertNotNull(faker);

        final int underTwenty = faker.number().random(20);
        assertTrue(underTwenty >= 0 && underTwenty < 20);

        final double between = faker.number().random(10.0, 20.0);
        assertTrue(between >= 10.0 && between < 20.0);

        final long betweenLong = faker.number().randomLong(100L, 200L);
        assertTrue(betweenLong >= 100L && betweenLong <= 200L);

        assertDoesNotThrow(() -> faker.number().bool());

        assertThrows(IllegalArgumentException.class, () -> faker.number().random(0));
        assertThrows(IllegalArgumentException.class, () -> faker.number().random(2.0, 2.0));
        assertThrows(IllegalArgumentException.class, () -> faker.number().randomLong(3L, 2L));
    }

    @Test
    void dateTimeProviderSupportsCustomRanges() {
        final Faker faker = Jaker.builder().locale("en-US").seed(42).build().faker();
        assertNotNull(faker);

        final String withinLastYear = faker.dateTime().isoDate(1);
        final java.time.LocalDate parsedRecent = java.time.LocalDate.parse(withinLastYear);
        final java.time.LocalDate today = java.time.LocalDate.now();
        assertFalse(parsedRecent.isAfter(today));
        assertFalse(parsedRecent.isBefore(today.minusYears(1).minusDays(1)));

        final java.time.LocalDate start = java.time.LocalDate.of(2020, 1, 1);
        final java.time.LocalDate end = java.time.LocalDate.of(2020, 1, 31);
        final String ranged = faker.dateTime().isoDateBetween(start, end);
        final java.time.LocalDate parsedRanged = java.time.LocalDate.parse(ranged);
        assertFalse(parsedRanged.isBefore(start));
        assertFalse(parsedRanged.isAfter(end));

        final String withinLastYearDateTime = faker.dateTime().isoDateTime(1);
        final java.time.LocalDateTime parsedRecentDateTime = java.time.LocalDateTime.parse(withinLastYearDateTime);
        final java.time.LocalDateTime now = java.time.LocalDateTime.now();
        assertFalse(parsedRecentDateTime.isAfter(now));
        assertFalse(parsedRecentDateTime.isBefore(now.minusYears(1).minusDays(1)));

        assertThrows(IllegalArgumentException.class, () -> faker.dateTime().isoDate(0));
        assertThrows(IllegalArgumentException.class, () -> faker.dateTime().isoDateTime(0));
        assertThrows(IllegalArgumentException.class, () -> faker.dateTime().isoDateBetween(end, start));
    }

    @Test
    void internetProviderSupportsCustomEmailLocalPart() {
        final Faker faker = Jaker.builder().locale("en-US").seed(42).build().faker();
        assertNotNull(faker);

        final String custom = faker.internet().email("Test.User");
        assertTrue(custom.startsWith("test.user@"));
        assertTrue(custom.contains("@"));

        final String customWithDomain = faker.internet().email("Test.User", "Acme.EXAMPLE");
        assertEquals("test.user@acme.example", customWithDomain);

        assertThrows(IllegalArgumentException.class, () -> faker.internet().email(" "));
        assertThrows(IllegalArgumentException.class, () -> faker.internet().email("ok", " "));
    }

    @Test
    void providersSupportAdditionalCustomOptions() {
        final Faker faker = Jaker.builder().locale("en-US").seed(42).build().faker();
        assertNotNull(faker);

        final String customEmail = faker.internet().email("Admin.Bot", "custom.org");
        assertEquals("admin.bot@custom.org", customEmail);

        final String customHostname = faker.server().hostname("api", "internal.example");
        assertTrue(customHostname.startsWith("api-"));
        assertTrue(customHostname.endsWith(".internal.example"));

        final long exactSize = faker.file().size(2048, 2048);
        assertEquals(2048, exactSize);

        final long rangedSize = faker.file().size(128, 256);
        assertTrue(rangedSize >= 128 && rangedSize <= 256);

        assertThrows(IllegalArgumentException.class, () -> faker.internet().email("admin", " "));
        assertThrows(IllegalArgumentException.class, () -> faker.file().size(-1, 10));
        assertThrows(IllegalArgumentException.class, () -> faker.file().size(500, 100));
    }
}
