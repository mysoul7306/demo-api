/*
 * Author: rok_root
 * Created time: 2021. 07. 24
 * Copyrights rok_root. All rights reserved.
 */

package kr.co.rokroot.demo.api.spring.framework.advice.db;

import kr.co.rokroot.core.annotations.OracleDB;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement(proxyTargetClass = true)
@MapperScan(annotationClass = OracleDB.class)
public class OracleDataSource {



}
