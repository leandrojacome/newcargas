package br.com.revenuebrasil.newcargas.repository.search;

import br.com.revenuebrasil.newcargas.domain.Regiao;
import br.com.revenuebrasil.newcargas.repository.RegiaoRepository;
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
 * Spring Data Elasticsearch repository for the {@link Regiao} entity.
 */
public interface RegiaoSearchRepository extends ElasticsearchRepository<Regiao, Long>, RegiaoSearchRepositoryInternal {}

interface RegiaoSearchRepositoryInternal {
    Page<Regiao> search(String query, Pageable pageable);

    Page<Regiao> search(Query query);

    @Async
    void index(Regiao entity);

    @Async
    void deleteFromIndexById(Long id);
}

class RegiaoSearchRepositoryInternalImpl implements RegiaoSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final RegiaoRepository repository;

    RegiaoSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, RegiaoRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Regiao> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<Regiao> search(Query query) {
        SearchHits<Regiao> searchHits = elasticsearchTemplate.search(query, Regiao.class);
        List<Regiao> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Regiao entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), Regiao.class);
    }
}
