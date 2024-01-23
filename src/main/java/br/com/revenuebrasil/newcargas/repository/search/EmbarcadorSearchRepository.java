package br.com.revenuebrasil.newcargas.repository.search;

import br.com.revenuebrasil.newcargas.domain.Embarcador;
import br.com.revenuebrasil.newcargas.repository.EmbarcadorRepository;
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
 * Spring Data Elasticsearch repository for the {@link Embarcador} entity.
 */
public interface EmbarcadorSearchRepository extends ElasticsearchRepository<Embarcador, Long>, EmbarcadorSearchRepositoryInternal {}

interface EmbarcadorSearchRepositoryInternal {
    Page<Embarcador> search(String query, Pageable pageable);

    Page<Embarcador> search(Query query);

    @Async
    void index(Embarcador entity);

    @Async
    void deleteFromIndexById(Long id);
}

class EmbarcadorSearchRepositoryInternalImpl implements EmbarcadorSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final EmbarcadorRepository repository;

    EmbarcadorSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, EmbarcadorRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Embarcador> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<Embarcador> search(Query query) {
        SearchHits<Embarcador> searchHits = elasticsearchTemplate.search(query, Embarcador.class);
        List<Embarcador> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Embarcador entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), Embarcador.class);
    }
}
