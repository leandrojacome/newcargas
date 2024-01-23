package br.com.revenuebrasil.newcargas.repository.search;

import br.com.revenuebrasil.newcargas.domain.NotaFiscalColeta;
import br.com.revenuebrasil.newcargas.repository.NotaFiscalColetaRepository;
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
 * Spring Data Elasticsearch repository for the {@link NotaFiscalColeta} entity.
 */
public interface NotaFiscalColetaSearchRepository
    extends ElasticsearchRepository<NotaFiscalColeta, Long>, NotaFiscalColetaSearchRepositoryInternal {}

interface NotaFiscalColetaSearchRepositoryInternal {
    Page<NotaFiscalColeta> search(String query, Pageable pageable);

    Page<NotaFiscalColeta> search(Query query);

    @Async
    void index(NotaFiscalColeta entity);

    @Async
    void deleteFromIndexById(Long id);
}

class NotaFiscalColetaSearchRepositoryInternalImpl implements NotaFiscalColetaSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final NotaFiscalColetaRepository repository;

    NotaFiscalColetaSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, NotaFiscalColetaRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<NotaFiscalColeta> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<NotaFiscalColeta> search(Query query) {
        SearchHits<NotaFiscalColeta> searchHits = elasticsearchTemplate.search(query, NotaFiscalColeta.class);
        List<NotaFiscalColeta> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(NotaFiscalColeta entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), NotaFiscalColeta.class);
    }
}
