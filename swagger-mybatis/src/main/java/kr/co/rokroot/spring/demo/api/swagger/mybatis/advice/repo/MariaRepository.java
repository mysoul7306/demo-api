/*
 * Author: rok_root
 * Created time: 2021. 07. 25
 * Copyrights rok_root. All rights reserved.
 */

package kr.co.rokroot.spring.demo.api.swagger.mybatis.advice.repo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.Resource;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MariaRepository extends DefaultTransactionDefinition {

    @Getter
    @Resource(name = "mariaSqlSessionTemplate")
    private final SqlSessionTemplate sqlSession;

    @Getter
    @Resource(name = "mariaTransactionManager")
    private final DataSourceTransactionManager transaction;

}
