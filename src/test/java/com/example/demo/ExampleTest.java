package com.example.demo;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.table;

import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Table;
import org.junit.jupiter.api.BeforeAll;

public class ExampleTest extends PostgresContainerBase {

  private static final Table<Record> FOO = table(name("foo"));
  private static final Field<Integer> ID = field("ID", Integer.class);

  @BeforeAll
  public static void createTable() {
    dslContext().createTable(FOO).column(ID).execute();
    dslContext().insertInto(FOO).columns(ID).values(1).values(2).values(3).execute();
  }

  //  @RepeatedTest(5)
  public void givenStreamAndFindAny_ThenConnectionRemainsOpen() {
    var result = dslContext().select(ID).from(FOO).fetchStream().findAny();

    assertThat(result).isNotEmpty();
  }

  //  @RepeatedTest(5)
  public void givenStreamAndFindFirst_ThenConnectionRemainsOpen() {
    var result = dslContext().select(ID).from(FOO).fetchStream().findFirst();

    assertThat(result).isNotEmpty();
  }

  //  @RepeatedTest(5)
  public void givenStream_ThenWorksFine() {
    var result = dslContext().select(ID).from(FOO).fetchStream().collect(toList());

    assertThat(result).hasSize(3);
  }

  //  @RepeatedTest(5)
  public void givenLimitOf1AndOptional_ThenWorksFine() {
    var result = dslContext().select(ID).from(FOO).limit(1).fetchOptional();

    assertThat(result).isNotEmpty();
  }
}
