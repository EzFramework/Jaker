package com.github.ezframework.jaker;

import org.junit.jupiter.api.Test;

import com.github.ezframework.jaker.data.DataLoader;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

public class FakerTest {

    @Test
    public void seededFakerProducesSameOutput() {
        Jaker a = Jaker.builder().locale(Locale.forLanguageTag("en-US")).seed(1234L).build();
        Jaker b = Jaker.builder().locale(Locale.forLanguageTag("en-US")).seed(1234L).build();

        String n1 = a.faker().name().firstName();
        String n2 = b.faker().name().firstName();

        assertThat(n1).isEqualTo(n2);
        assertThat(n1).isNotBlank();
    }

    @Test
    public void multipleFakerInstancesShareDatasetCache() throws Exception {
        Faker firstFaker = new Faker(Locale.forLanguageTag("en-US"), new Random(11L));
        Faker secondFaker = new Faker(Locale.forLanguageTag("en-US"), new Random(22L));

        List<String> firstNames = dataLoaderOf(firstFaker).load("en-US", "names");
        List<String> secondNames = dataLoaderOf(secondFaker).load("en-US", "names");

        assertThat(secondNames).isSameAs(firstNames);
    }

    private static DataLoader dataLoaderOf(Faker faker) throws Exception {
        Field loaderField = Faker.class.getDeclaredField("loader");
        loaderField.setAccessible(true);
        return (DataLoader) loaderField.get(faker);
    }

}
