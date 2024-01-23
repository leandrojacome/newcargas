package br.com.revenuebrasil.newcargas.config.audit;

import br.com.revenuebrasil.newcargas.config.Constants;
import br.com.revenuebrasil.newcargas.security.SecurityUtils;
import org.javers.spring.auditable.AuthorProvider;
import org.springframework.stereotype.Component;

@Component
public class JaversAuthorProvider implements AuthorProvider {

    @Override
    public String provide() {
        return SecurityUtils.getCurrentUserLogin().orElse(Constants.SYSTEM);
    }
}
