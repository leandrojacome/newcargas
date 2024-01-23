package br.com.revenuebrasil.newcargas.repository.search;

import br.com.revenuebrasil.newcargas.domain.ContaBancaria;
import br.com.revenuebrasil.newcargas.repository.ContaBancariaRepository;
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
 * Spring Data Elasticsearch repository for the {@link ContaBancaria} entity.
 */
public interface ContaBancariaSearchRepository
    extends ElasticsearchRepository<ContaBancaria, Long>, ContaBancariaSearchRepositoryInternal {}

interface ContaBancariaSearchRepositoryInternal {
    Page<ContaBancaria> search(String query, Pageable pageable);

    Page<ContaBancaria> search(Query query);

    @Async
    void index(ContaBancaria entity);

    @Async
    void deleteFromIndexById(Long id);
}

class ContaBancariaSearchRepositoryInternalImpl implements ContaBancariaSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final ContaBancariaRepository repository;

    ContaBancariaSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, ContaBancariaRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<ContaBancaria> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<ContaBancaria> search(Query query) {
        SearchHits<ContaBancaria> searchHits = elasticsearchTemplate.search(query, ContaBancaria.class);
        List<ContaBancaria> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(ContaBancaria entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), ContaBancaria.class);
    }
}
