package com.github.ezframework.jaker;

import java.util.Locale;
import java.util.Random;

import com.github.ezframework.jaker.data.DataLoader;
import com.github.ezframework.jaker.providers.AddressProvider;
import com.github.ezframework.jaker.providers.AnimalProvider;
import com.github.ezframework.jaker.providers.BankProvider;
import com.github.ezframework.jaker.providers.FileProvider;
import com.github.ezframework.jaker.providers.FruitProvider;
import com.github.ezframework.jaker.providers.GameServerProvider;
import com.github.ezframework.jaker.providers.CompanyProvider;
import com.github.ezframework.jaker.providers.DateTimeProvider;
import com.github.ezframework.jaker.providers.FinanceProvider;
import com.github.ezframework.jaker.providers.InternetProvider;
import com.github.ezframework.jaker.providers.LoremProvider;
import com.github.ezframework.jaker.providers.NameProvider;
import com.github.ezframework.jaker.providers.NumberProvider;
import com.github.ezframework.jaker.providers.PersonProvider;
import com.github.ezframework.jaker.providers.ServerProvider;
import com.github.ezframework.jaker.providers.CountryProvider;

/**
 * Context object provided to providers so they can safely access shared
 * services (loader, locale, random) and other providers via a late-bound
 * {@link ProviderAccess} implementation.
 */
public final class ProviderContext {

    /** Minimal access interface for other providers. Implemented by `Faker`. */
    public interface ProviderAccess {
        NameProvider name();
        AddressProvider address();
        CompanyProvider company();
        InternetProvider internet();
        FinanceProvider finance();
        DateTimeProvider dateTime();
        LoremProvider text();
        BankProvider bank();
        NumberProvider number();
        PersonProvider person();
        ServerProvider server();
        FileProvider file();
        GameServerProvider gameServer();
        FruitProvider fruit();
        AnimalProvider animal();
        CountryProvider country();
    }

    private final DataLoader loader;
    private final Locale locale;
    private final Random random;
    private ProviderAccess access;

    public ProviderContext(final DataLoader loader, final Locale locale, final Random random) {
        this.loader = loader;
        this.locale = locale;
        this.random = random;
    }

    public DataLoader loader() {
        return loader;
    }

    public Locale locale() {
        return locale;
    }

    public Random random() {
        return random;
    }

    /**
     * Install the provider access implementation; done by the `Faker` after
     * all providers have been constructed so providers can safely call each other.
     */
    public void setProviderAccess(final ProviderAccess access) {
        this.access = access;
    }

    /**
     * Access other providers. May be null until {@link #setProviderAccess} is called.
     */
    public ProviderAccess providers() {
        return access;
    }
}
