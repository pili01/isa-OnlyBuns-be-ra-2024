package rs.ac.uns.ftn.informatika.jpa.config;
import org.ehcache.jsr107.EhcacheCachingProvider;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.CacheManager;
import javax.cache.Caching;
import java.net.URI;
import java.net.URISyntaxException;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public org.springframework.cache.CacheManager cacheManager() {
        try {
            // Učitavanje Ehcache konfiguracije
            URI ehcacheUri = getClass().getResource("/ehcache.xml").toURI();
            CacheManager jCacheManager = Caching.getCachingProvider(EhcacheCachingProvider.class.getName())
                    .getCacheManager(ehcacheUri, getClass().getClassLoader());

            // Spring CacheManager
            return new JCacheCacheManager(jCacheManager);

        } catch (URISyntaxException e) {
            throw new RuntimeException("Invalid URI for Ehcache configuration file", e);
        }
    }
}
