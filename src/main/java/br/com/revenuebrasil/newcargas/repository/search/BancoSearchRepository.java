package br.com.revenuebrasil.newcargas.repository.search;

import br.com.revenuebrasil.newcargas.domain.Banco;
import br.com.revenuebrasil.newcargas.repository.BancoRepository;
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
 * Spring Data Elasticsearch repository for the {@link Banco} entity.
 */
public interface BancoSearchRepository extends ElasticsearchRepository<Banco, Long>, BancoSearchRepositoryInternal {}

interface BancoSearchRepositoryInternal {
    Page<Banco> search(String query, Pageable pageable);

    Page<Banco> search(Query query);

    @Async
    void index(Banco entity);

    @Async
    void deleteFromIndexById(Long id);
}

class BancoSearchRepositoryInternalImpl implements BancoSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final BancoRepository repository;

    BancoSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, BancoRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Banco> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<Banco> search(Query query) {
        SearchHits<Banco> searchHits = elasticsearchTemplate.search(query, Banco.class);
        List<Banco> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Banco entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), Banco.class);
    }
}
