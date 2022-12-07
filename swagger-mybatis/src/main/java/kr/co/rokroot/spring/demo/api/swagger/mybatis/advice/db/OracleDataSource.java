/*
 * Author: rok_root
 * Created time: 2021. 07. 25
 * Copyrights rok_root. All rights reserved.
 */

package kr.co.rokroot.spring.demo.api.swagger.mybatis.advice.db;

import kr.co.rokroot.core.annotations.OracleDB;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement(proxyTargetClass = true)
@MapperScan(basePackages = "kr.co.rokroot.spring.demo.api.swagger.mybatis", annotationClass = OracleDB.class)
public class OracleDataSource {



}
