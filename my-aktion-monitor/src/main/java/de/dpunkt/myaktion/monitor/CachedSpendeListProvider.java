package de.dpunkt.myaktion.monitor;

import java.util.List;

import javax.cache.Cache;
import javax.cache.CacheBuilder;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;

import de.dpunkt.myaktion.model.Spende;

public class CachedSpendeListProvider {

	private SpendeListProvider spendeListProvider;
	private Cache<Long, List<Spende>> cache;

	public CachedSpendeListProvider() {
		spendeListProvider = new SpendeListProvider();
		CacheManager cacheManager = Caching.getCacheManager();
		CacheBuilder<Long, List<Spende>> cacheBuilder = cacheManager
				.createCacheBuilder("spendeListCache");
		cache = cacheBuilder.build();
	}

	public List<Spende> getSpendeList(long aktionId) throws NotFoundException,
			WebApplicationException {
		List<Spende> ret = null;
		if (cache.containsKey(aktionId)) {
			ret = cache.get(aktionId);
		} else {
			ret = spendeListProvider.getSpendeList(aktionId);
			cache.put(aktionId, ret);
		}
		return ret;
	}

}
