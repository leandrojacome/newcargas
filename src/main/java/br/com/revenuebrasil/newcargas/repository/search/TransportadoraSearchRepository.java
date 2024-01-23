package br.com.revenuebrasil.newcargas.repository.search;

import br.com.revenuebrasil.newcargas.domain.Transportadora;
import br.com.revenuebrasil.newcargas.repository.TransportadoraRepository;
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
 * Spring Data Elasticsearch repository for the {@link Transportadora} entity.
 */
public interface TransportadoraSearchRepository
    extends ElasticsearchRepository<Transportadora, Long>, TransportadoraSearchRepositoryInternal {}

interface TransportadoraSearchRepositoryInternal {
    Page<Transportadora> search(String query, Pageable pageable);

    Page<Transportadora> search(Query query);

    @Async
    void index(Transportadora entity);

    @Async
    void deleteFromIndexById(Long id);
}

class TransportadoraSearchRepositoryInternalImpl implements TransportadoraSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final TransportadoraRepository repository;

    TransportadoraSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, TransportadoraRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Transportadora> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<Transportadora> search(Query query) {
        SearchHits<Transportadora> searchHits = elasticsearchTemplate.search(query, Transportadora.class);
        List<Transportadora> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Transportadora entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), Transportadora.class);
    }
}
