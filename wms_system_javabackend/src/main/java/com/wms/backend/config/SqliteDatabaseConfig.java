package com.wms.backend.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration
public class SqliteDatabaseConfig {
    private static final Logger LOG = LoggerFactory.getLogger(SqliteDatabaseConfig.class);

    @Bean(destroyMethod = "close")
    public HikariDataSource dataSource(@Value("${app.database.path}") String filename) throws IOException {
        Path path = Paths.get(filename).toAbsolutePath().normalize();
        Files.createDirectories(path.getParent());
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("org.sqlite.JDBC");
        config.setJdbcUrl("jdbc:sqlite:" + path.toString().replace('\\', '/') + "?busy_timeout=10000&foreign_keys=on&journal_mode=WAL");
        config.setMaximumPoolSize(1);
        HikariDataSource source = new HikariDataSource(config);
        try {
            initialize(source);
            LOG.info("SQLite database: {}", path);
            return source;
        } catch (RuntimeException e) {
            source.close();
            throw e;
        }
    }

    private void initialize(DataSource source) {
        new TransactionTemplate(new DataSourceTransactionManager(source)).execute(status -> {
            ResourceDatabasePopulator schema = new ResourceDatabasePopulator(new ClassPathResource("db/wms_system_schema.sql"));
            schema.setSqlScriptEncoding("UTF-8");
            schema.setSeparator(";;");
            schema.execute(source);
            JdbcTemplate jdbc = new JdbcTemplate(source);
            jdbc.execute("CREATE TABLE IF NOT EXISTS wms_initialization (version INTEGER PRIMARY KEY)");
            if (jdbc.queryForObject("SELECT COUNT(*) FROM wms_initialization WHERE version = 1", Integer.class) == 0) {
                ResourceDatabasePopulator seed = new ResourceDatabasePopulator(new ClassPathResource("db/wms_system_seed.sql"));
                seed.setSqlScriptEncoding("UTF-8");
                seed.execute(source);
                jdbc.update("INSERT INTO wms_initialization (version) VALUES (1)");
            }
            return null;
        });
    }
}
