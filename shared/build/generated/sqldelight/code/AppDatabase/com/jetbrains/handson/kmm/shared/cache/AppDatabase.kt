package com.jetbrains.handson.kmm.shared.cache

import com.jetbrains.handson.kmm.shared.cache.shared.newInstance
import com.jetbrains.handson.kmm.shared.cache.shared.schema
import com.squareup.sqldelight.Transacter
import com.squareup.sqldelight.db.SqlDriver
import comjetbrainshandsonkmmsharedcache.AppDatabaseQueries

public interface AppDatabase : Transacter {
  public val appDatabaseQueries: AppDatabaseQueries

  public companion object {
    public val Schema: SqlDriver.Schema
      get() = AppDatabase::class.schema

    public operator fun invoke(driver: SqlDriver): AppDatabase =
        AppDatabase::class.newInstance(driver)
  }
}
