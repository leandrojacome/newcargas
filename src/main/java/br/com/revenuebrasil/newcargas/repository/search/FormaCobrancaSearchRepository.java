package br.com.revenuebrasil.newcargas.repository.search;

import br.com.revenuebrasil.newcargas.domain.FormaCobranca;
import br.com.revenuebrasil.newcargas.repository.FormaCobrancaRepository;
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
 * Spring Data Elasticsearch repository for the {@link FormaCobranca} entity.
 */
public interface FormaCobrancaSearchRepository
    extends ElasticsearchRepository<FormaCobranca, Long>, FormaCobrancaSearchRepositoryInternal {}

interface FormaCobrancaSearchRepositoryInternal {
    Page<FormaCobranca> search(String query, Pageable pageable);

    Page<FormaCobranca> search(Query query);

    @Async
    void index(FormaCobranca entity);

    @Async
    void deleteFromIndexById(Long id);
}

class FormaCobrancaSearchRepositoryInternalImpl implements FormaCobrancaSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final FormaCobrancaRepository repository;

    FormaCobrancaSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, FormaCobrancaRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<FormaCobranca> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<FormaCobranca> search(Query query) {
        SearchHits<FormaCobranca> searchHits = elasticsearchTemplate.search(query, FormaCobranca.class);
        List<FormaCobranca> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(FormaCobranca entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), FormaCobranca.class);
    }
}
