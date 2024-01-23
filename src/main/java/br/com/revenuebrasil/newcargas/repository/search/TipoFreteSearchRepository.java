package br.com.revenuebrasil.newcargas.repository.search;

import br.com.revenuebrasil.newcargas.domain.TipoFrete;
import br.com.revenuebrasil.newcargas.repository.TipoFreteRepository;
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
 * Spring Data Elasticsearch repository for the {@link TipoFrete} entity.
 */
public interface TipoFreteSearchRepository extends ElasticsearchRepository<TipoFrete, Long>, TipoFreteSearchRepositoryInternal {}

interface TipoFreteSearchRepositoryInternal {
    Page<TipoFrete> search(String query, Pageable pageable);

    Page<TipoFrete> search(Query query);

    @Async
    void index(TipoFrete entity);

    @Async
    void deleteFromIndexById(Long id);
}

class TipoFreteSearchRepositoryInternalImpl implements TipoFreteSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final TipoFreteRepository repository;

    TipoFreteSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, TipoFreteRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<TipoFrete> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<TipoFrete> search(Query query) {
        SearchHits<TipoFrete> searchHits = elasticsearchTemplate.search(query, TipoFrete.class);
        List<TipoFrete> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(TipoFrete entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), TipoFrete.class);
    }
}
