/*
 * Author: rok_root
 * Created time: 2021. 07. 25
 * Copyrights rok_root. All rights reserved.
 */

package kr.co.rokroot.mybatis.rest.api.advice.db;

import kr.co.rokroot.mybatis.core.annotations.MicrosoftDB;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement(proxyTargetClass = true)
@MapperScan(basePackages = "kr.co.rokroot.mybatis.rest.api", annotationClass = MicrosoftDB.class)
public class MSDataSource {



}
