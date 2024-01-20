package br.com.revenuebrasil.newcargas.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration =
            Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                    .build()
            );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, br.com.revenuebrasil.newcargas.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, br.com.revenuebrasil.newcargas.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, br.com.revenuebrasil.newcargas.domain.User.class.getName());
            createCache(cm, br.com.revenuebrasil.newcargas.domain.Authority.class.getName());
            createCache(cm, br.com.revenuebrasil.newcargas.domain.User.class.getName() + ".authorities");
            createCache(cm, br.com.revenuebrasil.newcargas.domain.Estado.class.getName());
            createCache(cm, br.com.revenuebrasil.newcargas.domain.Estado.class.getName() + ".cidades");
            createCache(cm, br.com.revenuebrasil.newcargas.domain.Cidade.class.getName());
            createCache(cm, br.com.revenuebrasil.newcargas.domain.Cidade.class.getName() + ".enderecos");
            createCache(cm, br.com.revenuebrasil.newcargas.domain.Embarcador.class.getName());
            createCache(cm, br.com.revenuebrasil.newcargas.domain.Embarcador.class.getName() + ".enderecos");
            createCache(cm, br.com.revenuebrasil.newcargas.domain.Embarcador.class.getName() + ".cidades");
            createCache(cm, br.com.revenuebrasil.newcargas.domain.Embarcador.class.getName() + ".contaBancarias");
            createCache(cm, br.com.revenuebrasil.newcargas.domain.Embarcador.class.getName() + ".tabelaFretes");
            createCache(cm, br.com.revenuebrasil.newcargas.domain.Embarcador.class.getName() + ".solitacaoColetas");
            createCache(cm, br.com.revenuebrasil.newcargas.domain.Embarcador.class.getName() + ".notificacaos");
            createCache(cm, br.com.revenuebrasil.newcargas.domain.Embarcador.class.getName() + ".faturas");
            createCache(cm, br.com.revenuebrasil.newcargas.domain.Transportadora.class.getName());
            createCache(cm, br.com.revenuebrasil.newcargas.domain.Transportadora.class.getName() + ".enderecos");
            createCache(cm, br.com.revenuebrasil.newcargas.domain.Transportadora.class.getName() + ".cidades");
            createCache(cm, br.com.revenuebrasil.newcargas.domain.Transportadora.class.getName() + ".contaBancarias");
            createCache(cm, br.com.revenuebrasil.newcargas.domain.Transportadora.class.getName() + ".tabelaFretes");
            createCache(cm, br.com.revenuebrasil.newcargas.domain.Transportadora.class.getName() + ".tomadaPrecos");
            createCache(cm, br.com.revenuebrasil.newcargas.domain.Transportadora.class.getName() + ".contratacaos");
            createCache(cm, br.com.revenuebrasil.newcargas.domain.Transportadora.class.getName() + ".notificacaos");
            createCache(cm, br.com.revenuebrasil.newcargas.domain.Transportadora.class.getName() + ".faturas");
            createCache(cm, br.com.revenuebrasil.newcargas.domain.Banco.class.getName());
            createCache(cm, br.com.revenuebrasil.newcargas.domain.Banco.class.getName() + ".contaBancarias");
            createCache(cm, br.com.revenuebrasil.newcargas.domain.ContaBancaria.class.getName());
            createCache(cm, br.com.revenuebrasil.newcargas.domain.TabelaFrete.class.getName());
            createCache(cm, br.com.revenuebrasil.newcargas.domain.Endereco.class.getName());
            createCache(cm, br.com.revenuebrasil.newcargas.domain.FormaCobranca.class.getName());
            createCache(cm, br.com.revenuebrasil.newcargas.domain.FormaCobranca.class.getName() + ".tabelaFretes");
            createCache(cm, br.com.revenuebrasil.newcargas.domain.FormaCobranca.class.getName() + ".fatutas");
            createCache(cm, br.com.revenuebrasil.newcargas.domain.TipoFrete.class.getName());
            createCache(cm, br.com.revenuebrasil.newcargas.domain.TipoFrete.class.getName() + ".tabelaFretes");
            createCache(cm, br.com.revenuebrasil.newcargas.domain.TipoVeiculo.class.getName());
            createCache(cm, br.com.revenuebrasil.newcargas.domain.TipoVeiculo.class.getName() + ".solitacaoColetas");
            createCache(cm, br.com.revenuebrasil.newcargas.domain.TipoCarga.class.getName());
            createCache(cm, br.com.revenuebrasil.newcargas.domain.TipoCarga.class.getName() + ".tabelaFretes");
            createCache(cm, br.com.revenuebrasil.newcargas.domain.SolicitacaoColeta.class.getName());
            createCache(cm, br.com.revenuebrasil.newcargas.domain.SolicitacaoColeta.class.getName() + ".notaFiscalColetas");
            createCache(cm, br.com.revenuebrasil.newcargas.domain.SolicitacaoColeta.class.getName() + ".enderecoOrigems");
            createCache(cm, br.com.revenuebrasil.newcargas.domain.SolicitacaoColeta.class.getName() + ".enderecoDestinos");
            createCache(cm, br.com.revenuebrasil.newcargas.domain.SolicitacaoColeta.class.getName() + ".historicoStatusColetas");
            createCache(cm, br.com.revenuebrasil.newcargas.domain.NotaFiscalColeta.class.getName());
            createCache(cm, br.com.revenuebrasil.newcargas.domain.NotaFiscalColeta.class.getName() + ".enderecoOrigems");
            createCache(cm, br.com.revenuebrasil.newcargas.domain.NotaFiscalColeta.class.getName() + ".enderecoDestinos");
            createCache(cm, br.com.revenuebrasil.newcargas.domain.StatusColeta.class.getName());
            createCache(cm, br.com.revenuebrasil.newcargas.domain.StatusColeta.class.getName() + ".solicitacaoColetas");
            createCache(cm, br.com.revenuebrasil.newcargas.domain.StatusColeta.class.getName() + ".historicoStatusColetaOrigems");
            createCache(cm, br.com.revenuebrasil.newcargas.domain.StatusColeta.class.getName() + ".historicoStatusColetaDestinos");
            createCache(cm, br.com.revenuebrasil.newcargas.domain.StatusColeta.class.getName() + ".roteirizacaos");
            createCache(cm, br.com.revenuebrasil.newcargas.domain.StatusColeta.class.getName() + ".statusColetaOrigems");
            createCache(cm, br.com.revenuebrasil.newcargas.domain.StatusColeta.class.getName() + ".statusColetaDestinos");
            createCache(cm, br.com.revenuebrasil.newcargas.domain.HistoricoStatusColeta.class.getName());
            createCache(cm, br.com.revenuebrasil.newcargas.domain.Roteirizacao.class.getName());
            createCache(cm, br.com.revenuebrasil.newcargas.domain.Roteirizacao.class.getName() + ".historicoStatusColetas");
            createCache(cm, br.com.revenuebrasil.newcargas.domain.Roteirizacao.class.getName() + ".solitacaoColetas");
            createCache(cm, br.com.revenuebrasil.newcargas.domain.Roteirizacao.class.getName() + ".tomadaPrecos");
            createCache(cm, br.com.revenuebrasil.newcargas.domain.TomadaPreco.class.getName());
            createCache(cm, br.com.revenuebrasil.newcargas.domain.Notificacao.class.getName());
            createCache(cm, br.com.revenuebrasil.newcargas.domain.Contratacao.class.getName());
            createCache(cm, br.com.revenuebrasil.newcargas.domain.Contratacao.class.getName() + ".faturas");
            createCache(cm, br.com.revenuebrasil.newcargas.domain.Fatura.class.getName());
            createCache(cm, br.com.revenuebrasil.newcargas.domain.Regiao.class.getName());
            createCache(cm, br.com.revenuebrasil.newcargas.domain.Regiao.class.getName() + ".tabelaFreteOrigems");
            createCache(cm, br.com.revenuebrasil.newcargas.domain.Regiao.class.getName() + ".tabelaFreteDestinos");
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
