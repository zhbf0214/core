package com.dotmarketing.business.cache.provider;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class will be extended by any Cache implementation that needs/want to belong to the Cache Providers execution chain.
 * <br/>
 * <br/>
 * In order add and use a custom CacheProvider in the Cache Providers execution chain is required an Enterprise
 * License, without it only the default CacheProviders will be use ({@link com.dotmarketing.business.cache.provider.guava.GuavaCache},
 * {@link com.dotmarketing.business.cache.provider.h2.H2CacheLoader}).
 * <br/>
 * <br/>
 * With a valid Enterprise License the CacheProviders to use can be specified using the property <strong>cache.adminconfigpool.chain</strong>
 * in the <strong>dotmarketing-config.properties</strong> file, in that property you can specify the list of classes to use as CacheProviders, that
 * list will also define the order of execution of those providers.
 * <p/>
 * <strong>Examples:</strong>
 * <ul>
 * <li>cache.adminconfigpool.chain=com.dotmarketing.business.cache.provider.guava.GuavaCache,com.dotmarketing.business.cache.provider.h2.H2CacheLoader</li>
 * <li>cache.adminconfigpool.chain=com.dotmarketing.business.cache.provider.guava.TestCacheProvider,com.dotmarketing.business.cache.provider.guava.GuavaCache,com.dotmarketing.business.cache.provider.h2.H2CacheLoader</li>
 * </ul>
 *
 * @author Jonathan Gamba
 *         Date: 8/31/15
 */
public abstract class CacheProvider implements Serializable {

    private static final long serialVersionUID = -2139235480907776009L;

    /**
     * Returns the human readable name for this Cache Provider
     *
     * @return
     */
    public abstract String getName ();

    /**
     * Returns a unique key for this Cache Provider
     *
     * @return
     */
    public abstract String getKey ();

    /**
     * Initializes the provider
     *
     * @throws Exception
     */
    public abstract void init () throws Exception;

    /**
     * Adds the given content to the given region and for the given key
     *
     * @param group
     * @param key
     * @param content
     */
    public abstract void put ( String group, String key, final Object content );

    /**
     * Searches and return the content in a given region and with a given key
     *
     * @param group
     * @param key
     * @return
     */
    public abstract Object get ( String group, String key );

    /**
     * Invalidates a given key for a given region
     *
     * @param group
     * @param key
     */
    public abstract void remove ( String group, String key );

    /**
     * Invalidates the given region
     *
     * @param group
     */
    public abstract void remove ( String group );

    /**
     * Invalidates all the regions
     */
    public abstract void removeAll ();

    /**
     * Returns the keys found inside a given region
     *
     * @param group
     * @return
     */
    public abstract Set<String> getKeys ( String group );

    /**
     * Returns all the regions found in this Provider
     *
     * @return
     */
    public abstract Set<String> getGroups ();

    /**
     * Returns stats information of the objects handle by this provider
     *
     * @return
     */
    public abstract List<Map<String, Object>> getStats ();

    /**
     * Shutdowns the Provider
     */
    public abstract void shutdown ();

}