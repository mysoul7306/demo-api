/*
 * Author: rok_root
 * Created time: 2021. 07. 25
 * Copyrights rok_root. All rights reserved.
 */

package kr.co.rokroot.mybatis.rest.api.advice.repo;

import jakarta.annotation.Resource;
import kr.co.rokroot.mybatis.core.abstracts.AbstractRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MariaRepository extends AbstractRepository {

    @Getter
    @Resource(name = "mariaSqlSessionTemplate")
    private final SqlSessionTemplate sqlSession;

    @Getter
    @Resource(name = "mariaTransactionManager")
    private final DataSourceTransactionManager transaction;

    @Getter
    private final String repository = MariaRepository.class.getName();

}
