package br.com.revenuebrasil.newcargas.repository.search;

import br.com.revenuebrasil.newcargas.domain.TipoVeiculo;
import br.com.revenuebrasil.newcargas.repository.TipoVeiculoRepository;
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
 * Spring Data Elasticsearch repository for the {@link TipoVeiculo} entity.
 */
public interface TipoVeiculoSearchRepository extends ElasticsearchRepository<TipoVeiculo, Long>, TipoVeiculoSearchRepositoryInternal {}

interface TipoVeiculoSearchRepositoryInternal {
    Page<TipoVeiculo> search(String query, Pageable pageable);

    Page<TipoVeiculo> search(Query query);

    @Async
    void index(TipoVeiculo entity);

    @Async
    void deleteFromIndexById(Long id);
}

class TipoVeiculoSearchRepositoryInternalImpl implements TipoVeiculoSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final TipoVeiculoRepository repository;

    TipoVeiculoSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, TipoVeiculoRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<TipoVeiculo> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<TipoVeiculo> search(Query query) {
        SearchHits<TipoVeiculo> searchHits = elasticsearchTemplate.search(query, TipoVeiculo.class);
        List<TipoVeiculo> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(TipoVeiculo entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), TipoVeiculo.class);
    }
}
