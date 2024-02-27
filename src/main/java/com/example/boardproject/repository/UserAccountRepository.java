package com.example.boardproject.repository;

import com.example.boardproject.domain.QUserAccount;
import com.example.boardproject.domain.UserAccount;
import com.example.boardproject.domain.projection.UserAccountProjection;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

/**
 * @author daecheol song
 * @since 1.0
 */
@RepositoryRestResource(excerptProjection = UserAccountProjection.class)
public interface UserAccountRepository extends JpaRepository<UserAccount, Long>
        ,QuerydslPredicateExecutor<UserAccount>
        ,QuerydslBinderCustomizer<QUserAccount> {

    Optional<UserAccount> findByUserId(String userId);

    @Override
    default void customize(QuerydslBindings bindings, QUserAccount root) {
        bindings.excludeUnlistedProperties(true);
        bindings.including(root.userId);
        bindings.bind(root.userId).first(StringExpression::eq);

    }
}
