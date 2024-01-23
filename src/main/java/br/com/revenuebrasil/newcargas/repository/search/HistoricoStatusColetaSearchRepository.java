package br.com.revenuebrasil.newcargas.repository.search;

import br.com.revenuebrasil.newcargas.domain.HistoricoStatusColeta;
import br.com.revenuebrasil.newcargas.repository.HistoricoStatusColetaRepository;
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
 * Spring Data Elasticsearch repository for the {@link HistoricoStatusColeta} entity.
 */
public interface HistoricoStatusColetaSearchRepository
    extends ElasticsearchRepository<HistoricoStatusColeta, Long>, HistoricoStatusColetaSearchRepositoryInternal {}

interface HistoricoStatusColetaSearchRepositoryInternal {
    Page<HistoricoStatusColeta> search(String query, Pageable pageable);

    Page<HistoricoStatusColeta> search(Query query);

    @Async
    void index(HistoricoStatusColeta entity);

    @Async
    void deleteFromIndexById(Long id);
}

class HistoricoStatusColetaSearchRepositoryInternalImpl implements HistoricoStatusColetaSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final HistoricoStatusColetaRepository repository;

    HistoricoStatusColetaSearchRepositoryInternalImpl(
        ElasticsearchTemplate elasticsearchTemplate,
        HistoricoStatusColetaRepository repository
    ) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<HistoricoStatusColeta> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<HistoricoStatusColeta> search(Query query) {
        SearchHits<HistoricoStatusColeta> searchHits = elasticsearchTemplate.search(query, HistoricoStatusColeta.class);
        List<HistoricoStatusColeta> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(HistoricoStatusColeta entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), HistoricoStatusColeta.class);
    }
}
