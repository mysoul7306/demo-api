/*
 * Author: rok_root
 * Created time: 2021. 07. 25
 * Copyrights rok_root. All rights reserved.
 */

package kr.co.rokroot.spring.demo.api.swagger.mybatis.advice.db;

import kr.co.rokroot.core.annotations.MariaDB;
import kr.co.rokroot.core.utilities.PropertyUtility;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.BooleanTypeHandler;
import org.apache.ibatis.type.ClobTypeHandler;
import org.apache.ibatis.type.DateTypeHandler;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.Serializable;
import java.util.Properties;

@EnableTransactionManagement(proxyTargetClass = true)
@MapperScan(annotationClass = MariaDB.class)
public class MariaDataSource {

	protected final Properties prop = PropertyUtility.getProperties("jdbc.yaml");

	@Bean
	public BasicDataSource mariaDataSource() {
		BasicDataSource dataSource = new BasicDataSource();
		// TODO :: Encrypt URL
		dataSource.setUrl(this.prop.getProperty("maria.jdbc.url"));
		dataSource.setUsername(this.prop.getProperty("maria.jdbc.username"));
		dataSource.setPassword(this.prop.getProperty("maria.jdbc.password"));
		dataSource.setMaxTotal(Integer.parseInt(this.prop.getProperty("maria.jdbc.max.total")));
		dataSource.setMaxIdle(Integer.parseInt(this.prop.getProperty("maria.jdbc.max.idle")));
		// TODO :: Encrypt URL
		dataSource.setDriverClassName("org.mariadb.jdbc.Driver");
		dataSource.setValidationQuery("SELECT 1 FROM dual");
		dataSource.setMaxWaitMillis(60000);
		dataSource.setLogAbandoned(true);
		dataSource.setTestOnBorrow(true);
		dataSource.setRollbackOnReturn(true);
		dataSource.setDefaultAutoCommit(false);
		dataSource.setPoolPreparedStatements(true);
		dataSource.setClearStatementPoolOnReturn(true);
		dataSource.setRemoveAbandonedOnMaintenance(true);
		dataSource.setRemoveAbandonedTimeout(60);

		return dataSource;
	}

	@Bean
	public SqlSessionFactoryBean mariaSqlSessionFactory(DataSource mariaDataSource) throws Exception {
		SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
		sessionFactory.setDataSource(mariaDataSource);
		sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("**/repo/*.xml"));
		sessionFactory.setTypeAliasesPackage("kr.co.rokroot.demo.api.spring.framework");
		sessionFactory.setTypeAliasesSuperType(Serializable.class);
		sessionFactory.setConfigurationProperties(this.getProperties());
		sessionFactory.setTypeHandlers(
				new DateTypeHandler(),
				new BooleanTypeHandler(),
				new ClobTypeHandler());

		return sessionFactory;
	}

	@Bean(destroyMethod = "clearCache")
	public SqlSessionTemplate mariaSqlSessionTemplate(SqlSessionFactory mariaSqlSessionFactory) {
		return new SqlSessionTemplate(mariaSqlSessionFactory);
	}

	@Bean
	public DataSourceTransactionManager mariaTransactionManager(DataSource mariaDataSource) {
		DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
		transactionManager.setDataSource(mariaDataSource);
		transactionManager.setDefaultTimeout(2);
		transactionManager.setRollbackOnCommitFailure(true);

		return transactionManager;
	}


	protected final Properties getProperties() {
		Properties properties = new Properties();
		properties.put("cacheEnabled", true);
		properties.put("mapUnderscoreToCamelCase", true);
		properties.put("autoMappingBehavior", "FULL");
		properties.put("multipleResultSetsEnabled", true);
		properties.put("lazyLoadingEnabled", true);
		properties.put("lazyLoadTriggerMethods", "equals, clone, hashCode, toString");
		properties.put("aggressiveLazyLoading", false);

		return properties;
	}
}