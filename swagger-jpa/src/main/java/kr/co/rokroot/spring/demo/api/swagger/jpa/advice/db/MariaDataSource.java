/*
 * Author: rok_root
 * Created time: 2021. 07. 25
 * Copyrights rok_root. All rights reserved.
 */

package kr.co.rokroot.spring.demo.api.swagger.jpa.advice.db;

import kr.co.rokroot.core.utilities.PropertyUtility;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Properties;

@EnableTransactionManagement(proxyTargetClass = true)
public class MariaDataSource {

	protected final Properties prop = PropertyUtility.getProperties("jdbc.yaml");



}