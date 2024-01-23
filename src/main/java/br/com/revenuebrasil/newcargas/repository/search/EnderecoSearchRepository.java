package br.com.revenuebrasil.newcargas.repository.search;

import br.com.revenuebrasil.newcargas.domain.Endereco;
import br.com.revenuebrasil.newcargas.repository.EnderecoRepository;
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
 * Spring Data Elasticsearch repository for the {@link Endereco} entity.
 */
public interface EnderecoSearchRepository extends ElasticsearchRepository<Endereco, Long>, EnderecoSearchRepositoryInternal {}

interface EnderecoSearchRepositoryInternal {
    Page<Endereco> search(String query, Pageable pageable);

    Page<Endereco> search(Query query);

    @Async
    void index(Endereco entity);

    @Async
    void deleteFromIndexById(Long id);
}

class EnderecoSearchRepositoryInternalImpl implements EnderecoSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final EnderecoRepository repository;

    EnderecoSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, EnderecoRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Endereco> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<Endereco> search(Query query) {
        SearchHits<Endereco> searchHits = elasticsearchTemplate.search(query, Endereco.class);
        List<Endereco> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Endereco entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), Endereco.class);
    }
}
