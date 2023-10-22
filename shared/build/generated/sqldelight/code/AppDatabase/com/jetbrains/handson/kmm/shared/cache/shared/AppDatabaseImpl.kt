package com.jetbrains.handson.kmm.shared.cache.shared

import com.jetbrains.handson.kmm.shared.cache.AppDatabase
import com.squareup.sqldelight.Query
import com.squareup.sqldelight.TransacterImpl
import com.squareup.sqldelight.`internal`.copyOnWriteList
import com.squareup.sqldelight.db.SqlCursor
import com.squareup.sqldelight.db.SqlDriver
import comjetbrainshandsonkmmsharedcache.AppDatabaseQueries
import comjetbrainshandsonkmmsharedcache.PASS
import comjetbrainshandsonkmmsharedcache.USER
import kotlin.Any
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Unit
import kotlin.collections.MutableList
import kotlin.reflect.KClass

internal val KClass<AppDatabase>.schema: SqlDriver.Schema
  get() = AppDatabaseImpl.Schema

internal fun KClass<AppDatabase>.newInstance(driver: SqlDriver): AppDatabase =
    AppDatabaseImpl(driver)

private class AppDatabaseImpl(
  driver: SqlDriver
) : TransacterImpl(driver), AppDatabase {
  public override val appDatabaseQueries: AppDatabaseQueriesImpl = AppDatabaseQueriesImpl(this,
      driver)

  public object Schema : SqlDriver.Schema {
    public override val version: Int
      get() = 1

    public override fun create(driver: SqlDriver): Unit {
      driver.execute(null, """
          |CREATE TABLE USER (
          |	firstName TEXT,
          |    lastName TEXT,
          |    email TEXT,
          |    image TEXT,
          |    id INTEGER PRIMARY KEY AUTOINCREMENT
          |)
          """.trimMargin(), 0)
      driver.execute(null, """
          |CREATE TABLE PASS (
          |	name TEXT,
          |    description TEXT,
          |    icon TEXT,
          |    time TEXT,
          |    ready TEXT,
          |    id INTEGER PRIMARY KEY AUTOINCREMENT
          |)
          """.trimMargin(), 0)
    }

    public override fun migrate(
      driver: SqlDriver,
      oldVersion: Int,
      newVersion: Int
    ): Unit {
    }
  }
}

private class AppDatabaseQueriesImpl(
  private val database: AppDatabaseImpl,
  private val driver: SqlDriver
) : TransacterImpl(driver), AppDatabaseQueries {
  internal val getUser: MutableList<Query<*>> = copyOnWriteList()

  internal val getPass: MutableList<Query<*>> = copyOnWriteList()

  internal val getPassByName: MutableList<Query<*>> = copyOnWriteList()

  public override fun <T : Any> getUser(mapper: (
    firstName: String?,
    lastName: String?,
    email: String?,
    image: String?,
    id: Long
  ) -> T): Query<T> = Query(602110833, getUser, driver, "AppDatabase.sq", "getUser", """
  |SELECT USER.*
  |FROM USER
  """.trimMargin()) { cursor ->
    mapper(
      cursor.getString(0),
      cursor.getString(1),
      cursor.getString(2),
      cursor.getString(3),
      cursor.getLong(4)!!
    )
  }

  public override fun getUser(): Query<USER> = getUser { firstName, lastName, email, image, id ->
    USER(
      firstName,
      lastName,
      email,
      image,
      id
    )
  }

  public override fun <T : Any> getPass(mapper: (
    name: String?,
    description: String?,
    icon: String?,
    time: String?,
    ready: String?,
    id: Long
  ) -> T): Query<T> = Query(601945015, getPass, driver, "AppDatabase.sq", "getPass", """
  |SELECT PASS.*
  |FROM PASS
  """.trimMargin()) { cursor ->
    mapper(
      cursor.getString(0),
      cursor.getString(1),
      cursor.getString(2),
      cursor.getString(3),
      cursor.getString(4),
      cursor.getLong(5)!!
    )
  }

  public override fun getPass(): Query<PASS> = getPass { name, description, icon, time, ready, id ->
    PASS(
      name,
      description,
      icon,
      time,
      ready,
      id
    )
  }

  public override fun <T : Any> getPassByName(name: String?, mapper: (
    name: String?,
    description: String?,
    icon: String?,
    time: String?,
    ready: String?,
    id: Long
  ) -> T): Query<T> = GetPassByNameQuery(name) { cursor ->
    mapper(
      cursor.getString(0),
      cursor.getString(1),
      cursor.getString(2),
      cursor.getString(3),
      cursor.getString(4),
      cursor.getLong(5)!!
    )
  }

  public override fun getPassByName(name: String?): Query<PASS> = getPassByName(name) { name_,
      description, icon, time, ready, id ->
    PASS(
      name_,
      description,
      icon,
      time,
      ready,
      id
    )
  }

  public override fun insertUser(
    firstName: String?,
    lastName: String?,
    email: String?,
    image: String?
  ): Unit {
    driver.execute(1776969972,
        """INSERT INTO USER (firstName, lastName, email, image) VALUES (?, ?,?,?)""", 4) {
      bindString(1, firstName)
      bindString(2, lastName)
      bindString(3, email)
      bindString(4, image)
    }
    notifyQueries(1776969972, {database.appDatabaseQueries.getUser})
  }

  public override fun insertPass(
    name: String?,
    description: String?,
    icon: String?,
    time: String?,
    ready: String?
  ): Unit {
    driver.execute(1776804154,
        """INSERT INTO PASS (name, description, icon, time, ready) VALUES (?, ?, ?, ?, ?)""", 5) {
      bindString(1, name)
      bindString(2, description)
      bindString(3, icon)
      bindString(4, time)
      bindString(5, ready)
    }
    notifyQueries(1776804154, {database.appDatabaseQueries.getPassByName +
        database.appDatabaseQueries.getPass})
  }

  public override fun updatePassByTime(time: String?, name: String?): Unit {
    driver.execute(null, """
    |UPDATE PASS
    |SET
    |    time = ?
    |WHERE name ${ if (name == null) "IS" else "=" } ?
    """.trimMargin(), 2) {
      bindString(1, time)
      bindString(2, name)
    }
    notifyQueries(-1798018130, {database.appDatabaseQueries.getPassByName +
        database.appDatabaseQueries.getPass})
  }

  public override fun updatePassByReady(ready: String?, name: String?): Unit {
    driver.execute(null, """
    |UPDATE PASS
    |SET
    |    ready = ?
    |WHERE name ${ if (name == null) "IS" else "=" } ?
    """.trimMargin(), 2) {
      bindString(1, ready)
      bindString(2, name)
    }
    notifyQueries(94035170, {database.appDatabaseQueries.getPassByName +
        database.appDatabaseQueries.getPass})
  }

  private inner class GetPassByNameQuery<out T : Any>(
    public val name: String?,
    mapper: (SqlCursor) -> T
  ) : Query<T>(getPassByName, mapper) {
    public override fun execute(): SqlCursor = driver.executeQuery(null, """
    |SELECT PASS.*
    |FROM PASS
    |WHERE PASS.name ${ if (name == null) "IS" else "=" } ?
    """.trimMargin(), 1) {
      bindString(1, name)
    }

    public override fun toString(): String = "AppDatabase.sq:getPassByName"
  }
}
