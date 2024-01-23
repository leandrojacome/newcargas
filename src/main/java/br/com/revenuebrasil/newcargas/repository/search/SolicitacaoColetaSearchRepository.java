package br.com.revenuebrasil.newcargas.repository.search;

import br.com.revenuebrasil.newcargas.domain.SolicitacaoColeta;
import br.com.revenuebrasil.newcargas.repository.SolicitacaoColetaRepository;
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
 * Spring Data Elasticsearch repository for the {@link SolicitacaoColeta} entity.
 */
public interface SolicitacaoColetaSearchRepository
    extends ElasticsearchRepository<SolicitacaoColeta, Long>, SolicitacaoColetaSearchRepositoryInternal {}

interface SolicitacaoColetaSearchRepositoryInternal {
    Page<SolicitacaoColeta> search(String query, Pageable pageable);

    Page<SolicitacaoColeta> search(Query query);

    @Async
    void index(SolicitacaoColeta entity);

    @Async
    void deleteFromIndexById(Long id);
}

class SolicitacaoColetaSearchRepositoryInternalImpl implements SolicitacaoColetaSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final SolicitacaoColetaRepository repository;

    SolicitacaoColetaSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, SolicitacaoColetaRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<SolicitacaoColeta> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<SolicitacaoColeta> search(Query query) {
        SearchHits<SolicitacaoColeta> searchHits = elasticsearchTemplate.search(query, SolicitacaoColeta.class);
        List<SolicitacaoColeta> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(SolicitacaoColeta entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), SolicitacaoColeta.class);
    }
}
