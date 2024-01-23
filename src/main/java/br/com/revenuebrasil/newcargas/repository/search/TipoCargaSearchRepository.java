package br.com.revenuebrasil.newcargas.repository.search;

import br.com.revenuebrasil.newcargas.domain.TipoCarga;
import br.com.revenuebrasil.newcargas.repository.TipoCargaRepository;
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
 * Spring Data Elasticsearch repository for the {@link TipoCarga} entity.
 */
public interface TipoCargaSearchRepository extends ElasticsearchRepository<TipoCarga, Long>, TipoCargaSearchRepositoryInternal {}

interface TipoCargaSearchRepositoryInternal {
    Page<TipoCarga> search(String query, Pageable pageable);

    Page<TipoCarga> search(Query query);

    @Async
    void index(TipoCarga entity);

    @Async
    void deleteFromIndexById(Long id);
}

class TipoCargaSearchRepositoryInternalImpl implements TipoCargaSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final TipoCargaRepository repository;

    TipoCargaSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, TipoCargaRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<TipoCarga> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<TipoCarga> search(Query query) {
        SearchHits<TipoCarga> searchHits = elasticsearchTemplate.search(query, TipoCarga.class);
        List<TipoCarga> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(TipoCarga entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), TipoCarga.class);
    }
}
