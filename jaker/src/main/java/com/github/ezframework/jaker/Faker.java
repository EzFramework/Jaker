package com.github.ezframework.jaker;

import java.util.Locale;
import java.util.Random;

import com.github.ezframework.jaker.data.DataLoader;
import com.github.ezframework.jaker.providers.AddressProvider;
import com.github.ezframework.jaker.providers.AnimalProvider;
import com.github.ezframework.jaker.providers.BankProvider;
import com.github.ezframework.jaker.providers.CompanyProvider;
import com.github.ezframework.jaker.providers.CountryProvider;
import com.github.ezframework.jaker.providers.DateTimeProvider;
import com.github.ezframework.jaker.providers.FinanceProvider;
import com.github.ezframework.jaker.providers.FileProvider;
import com.github.ezframework.jaker.providers.FruitProvider;
import com.github.ezframework.jaker.providers.GameServerProvider;
import com.github.ezframework.jaker.providers.InternetProvider;
import com.github.ezframework.jaker.providers.LoremProvider;
import com.github.ezframework.jaker.providers.NameProvider;
import com.github.ezframework.jaker.providers.NumberProvider;
import com.github.ezframework.jaker.providers.PersonProvider;
import com.github.ezframework.jaker.providers.ServerProvider;
import com.github.ezframework.jaker.providers.UniqueProvider;
import com.github.ezframework.jaker.ProviderContext;

/**
 * Public facade exposing typed providers for localized fake data.
 *
 * <p>Small convenience methods are provided for quick tests and examples.</p>
 *
 * @author EzFramework
 * @version 1.0.0
 */
public final class Faker {

    /** Locale of this faker. */
    private final Locale locale;

    /** Random source for deterministic or non-deterministic generation. */
    private final Random random;

    /** Data loader for datasets. */
    private final DataLoader loader;

    /** Name provider. */
    private final NameProvider nameProvider;

    /** Address provider. */
    private final AddressProvider addressProvider;

    /** Company provider. */
    private final CompanyProvider companyProvider;

    /** Internet provider. */
    private final InternetProvider internetProvider;

    /** Finance provider. */
    private final FinanceProvider financeProvider;

    /** Date/time provider. */
    private final DateTimeProvider dateTimeProvider;

    /** Lorem provider. */
    private final LoremProvider loremProvider;

    /** Bank provider. */
    private final BankProvider bankProvider;

    /** Number provider. */
    private final NumberProvider numberProvider;

    /** Person provider. */
    private final PersonProvider personProvider;

    /** Server provider. */
    private final ServerProvider serverProvider;

    /** File metadata provider. */
    private final FileProvider fileProvider;

    /** Game server metadata provider. */
    private final GameServerProvider gameServerProvider;

    /** Fruit metadata provider. */
    private final FruitProvider fruitProvider;

    /** Animal metadata provider. */
    private final AnimalProvider animalProvider;

    /** Country metadata provider. */
    private final CountryProvider countryProvider;

    /** Unique/non-repeating provider entry point. */
    private final UniqueProvider uniqueProvider;

    public Faker(final Locale locale, final Random random) {
        this.locale = locale;
        this.random = random;
        this.loader = new DataLoader();

        // Provider context shared by providers to access loader/locale/random
        final ProviderContext ctx = new ProviderContext(loader, locale, random);

        this.nameProvider = new NameProvider(loader, locale, random);
        this.addressProvider = new AddressProvider(loader, locale, random);
        this.companyProvider = new CompanyProvider(loader, locale, random);
        this.internetProvider = new InternetProvider(loader, locale, random);
        this.financeProvider = new FinanceProvider(loader, locale, random);
        this.dateTimeProvider = new DateTimeProvider(locale, random);
        this.loremProvider = new LoremProvider(locale, random);
        this.bankProvider = new BankProvider(loader, locale, random);
        this.serverProvider = new ServerProvider(loader, locale, random);
        this.fileProvider = new FileProvider(loader, locale, random);
        this.gameServerProvider = new GameServerProvider(loader, locale, random);
        this.fruitProvider = new FruitProvider(loader, locale, random);
        this.animalProvider = new AnimalProvider(loader, locale, random);
        this.countryProvider = new CountryProvider(loader, locale, random);
        this.uniqueProvider = new UniqueProvider(loader, locale, random);

        // providers that rely on access to other providers or convenience APIs
        this.numberProvider = new NumberProvider(ctx);
        this.personProvider = new PersonProvider(ctx);

        // Now that all providers exist, publish access so providers may reference each other
        ctx.setProviderAccess(new ProviderContext.ProviderAccess() {
            @Override public NameProvider name() { return nameProvider; }
            @Override public AddressProvider address() { return addressProvider; }
            @Override public CompanyProvider company() { return companyProvider; }
            @Override public InternetProvider internet() { return internetProvider; }
            @Override public FinanceProvider finance() { return financeProvider; }
            @Override public DateTimeProvider dateTime() { return dateTimeProvider; }
            @Override public LoremProvider text() { return loremProvider; }
            @Override public BankProvider bank() { return bankProvider; }
            @Override public NumberProvider number() { return numberProvider; }
            @Override public PersonProvider person() { return personProvider; }
            @Override public ServerProvider server() { return serverProvider; }
            @Override public FileProvider file() { return fileProvider; }
            @Override public GameServerProvider gameServer() { return gameServerProvider; }
            @Override public FruitProvider fruit() { return fruitProvider; }
            @Override public AnimalProvider animal() { return animalProvider; }
            @Override public CountryProvider country() { return countryProvider; }
        });
    }

    /**
     * Returns the name provider for this faker.

     * @return name provider.
     */
    public NameProvider name() {
        return nameProvider;
    }

    /**
     * Returns the address provider for this faker.

     * @return address provider.
     */
    public AddressProvider address() {
        return addressProvider;
    }

    /**
     * Returns the company provider for this faker.

     * @return company provider.
     */
    public CompanyProvider company() {
        return companyProvider;
    }

    /**
     * Returns the internet provider for this faker.

     * @return internet provider.
     */
    public InternetProvider internet() {
        return internetProvider;
    }

    /**
     * Returns the finance provider for this faker.

     * @return finance provider.
     */
    public FinanceProvider finance() {
        return financeProvider;
    }

    /**
     * Returns the date/time provider for this faker.

     * @return date/time provider.
     */
    public DateTimeProvider dateTime() {
        return dateTimeProvider;
    }

    /**
     * Returns the text provider for this faker.

     * @return text provider.
     */
    public LoremProvider text() {
        return loremProvider;
    }

    /**
     * Returns the bank provider for this faker.

     * @return bank provider.
     */
    public BankProvider bank() {
        return bankProvider;
    }

    /**
     * Number provider utilities.
     *
     * @return number provider
     */
    public NumberProvider number() {
        return numberProvider;
    }

    /**
     * Person helper provider.
     *
     * @return person provider
     */
    public PersonProvider person() {
        return personProvider;
    }

    /**
     * Server/network helper provider.
     *
     * @return server provider
     */
    public ServerProvider server() {
        return serverProvider;
    }


    /**
     * File metadata helper provider.
     *
     * @return file provider
     */
    public FileProvider file() {
        return fileProvider;
    }

    /**
     * Game server metadata helper provider.
     *
     * @return game server provider
     */
    public GameServerProvider gameServer() {
        return gameServerProvider;
    }

    /**
     * Fruit helper provider.
     *
     * @return fruit provider
     */
    public FruitProvider fruit() {
        return fruitProvider;
    }

    /**
     * Animal helper provider.
     *
     * @return animal provider
     */
    public AnimalProvider animal() {
        return animalProvider;
    }

    /**
     * Country helper provider.
     *
     * @return country provider
     */
    public CountryProvider country() {
        return countryProvider;
    }
    /**
     * Entry point for unique (non-repeating) providers.
     *
     * @return unique provider
     */
    public UniqueProvider unique() {
        return uniqueProvider;
    }

    /**
     * Returns the locale configured for this faker.

     * @return locale for this faker.
     */
    public Locale locale() {
        return locale;
    }

    /**
     * Convenience: get a non-deterministic global faker using system default locale.
     *
     * @return global Faker instance
     */
    public static Faker global() {
        return Jaker.global().faker();
    }

    /**
     * Convenience: access the builder via `Faker.builder()`.
     *
     * @return builder instance
     */
    public static Jaker.Builder builder() {
        return Jaker.builder();
    }

    /**
     * Convenience: create a faker for the given IETF language tag (e.g. "en-US").
     *
     * @param languageTag the IETF language tag
     * @return faker instance for the given language tag
     */
    public static Faker of(final String languageTag) {
        return Jaker.builder().locale(languageTag).build().faker();
    }
}
