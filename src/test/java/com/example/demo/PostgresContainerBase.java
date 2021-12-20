package com.example.demo;

import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.testcontainers.containers.PostgreSQLContainer;

public abstract class PostgresContainerBase {

  private static final DSLContext DSL_CONTEXT;

  static {
    var container =
        new PostgreSQLContainer<>("postgres:11-alpine")
            .withUsername("postgres")
            .withPassword("test");
    container.start();
    var dataSource = createFromJdbcUrl(container);
    DSL_CONTEXT = DSL.using(dataSource, SQLDialect.POSTGRES);
  }

  private static DataSource createFromJdbcUrl(PostgreSQLContainer<?> container) {
    HikariDataSource dataSource = new HikariDataSource();
    dataSource.setJdbcUrl(container.getJdbcUrl());
    dataSource.setUsername(container.getUsername());
    dataSource.setPassword(container.getPassword());
    dataSource.setMaximumPoolSize(1); // <-- important bit for connection leak to show up
    dataSource.setConnectionTimeout(2000);
    return dataSource;
  }

  protected static DSLContext dslContext() {
    return DSL_CONTEXT;
  }
}
