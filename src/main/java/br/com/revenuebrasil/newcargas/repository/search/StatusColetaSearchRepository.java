package br.com.revenuebrasil.newcargas.repository.search;

import br.com.revenuebrasil.newcargas.domain.StatusColeta;
import br.com.revenuebrasil.newcargas.repository.StatusColetaRepository;
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
 * Spring Data Elasticsearch repository for the {@link StatusColeta} entity.
 */
public interface StatusColetaSearchRepository extends ElasticsearchRepository<StatusColeta, Long>, StatusColetaSearchRepositoryInternal {}

interface StatusColetaSearchRepositoryInternal {
    Page<StatusColeta> search(String query, Pageable pageable);

    Page<StatusColeta> search(Query query);

    @Async
    void index(StatusColeta entity);

    @Async
    void deleteFromIndexById(Long id);
}

class StatusColetaSearchRepositoryInternalImpl implements StatusColetaSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final StatusColetaRepository repository;

    StatusColetaSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, StatusColetaRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<StatusColeta> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<StatusColeta> search(Query query) {
        SearchHits<StatusColeta> searchHits = elasticsearchTemplate.search(query, StatusColeta.class);
        List<StatusColeta> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(StatusColeta entity) {
        repository.findOneWithEagerRelationships(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), StatusColeta.class);
    }
}
