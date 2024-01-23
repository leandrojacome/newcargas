package br.com.revenuebrasil.newcargas.repository.search;

import br.com.revenuebrasil.newcargas.domain.TomadaPreco;
import br.com.revenuebrasil.newcargas.repository.TomadaPrecoRepository;
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
 * Spring Data Elasticsearch repository for the {@link TomadaPreco} entity.
 */
public interface TomadaPrecoSearchRepository extends ElasticsearchRepository<TomadaPreco, Long>, TomadaPrecoSearchRepositoryInternal {}

interface TomadaPrecoSearchRepositoryInternal {
    Page<TomadaPreco> search(String query, Pageable pageable);

    Page<TomadaPreco> search(Query query);

    @Async
    void index(TomadaPreco entity);

    @Async
    void deleteFromIndexById(Long id);
}

class TomadaPrecoSearchRepositoryInternalImpl implements TomadaPrecoSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final TomadaPrecoRepository repository;

    TomadaPrecoSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, TomadaPrecoRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<TomadaPreco> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<TomadaPreco> search(Query query) {
        SearchHits<TomadaPreco> searchHits = elasticsearchTemplate.search(query, TomadaPreco.class);
        List<TomadaPreco> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(TomadaPreco entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), TomadaPreco.class);
    }
}
