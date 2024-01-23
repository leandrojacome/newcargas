package br.com.revenuebrasil.newcargas.repository.search;

import br.com.revenuebrasil.newcargas.domain.TabelaFrete;
import br.com.revenuebrasil.newcargas.repository.TabelaFreteRepository;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link TabelaFrete} entity.
 */
public interface TabelaFreteSearchRepository extends ElasticsearchRepository<TabelaFrete, Long>, TabelaFreteSearchRepositoryInternal {}

interface TabelaFreteSearchRepositoryInternal {
    Page<TabelaFrete> search(String query, Pageable pageable);

    Page<TabelaFrete> search(Query query);

    @Async
    void index(TabelaFrete entity);

    @Async
    void deleteFromIndexById(Long id);
}

class TabelaFreteSearchRepositoryInternalImpl implements TabelaFreteSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final TabelaFreteRepository repository;

    TabelaFreteSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, TabelaFreteRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<TabelaFrete> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<TabelaFrete> search(Query query) {
        SearchHits<TabelaFrete> searchHits = elasticsearchTemplate.search(query, TabelaFrete.class);
        List<TabelaFrete> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(TabelaFrete entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), TabelaFrete.class);
    }
}
