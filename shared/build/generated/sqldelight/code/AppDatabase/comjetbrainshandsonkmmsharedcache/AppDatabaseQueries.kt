package comjetbrainshandsonkmmsharedcache

import com.squareup.sqldelight.Query
import com.squareup.sqldelight.Transacter
import kotlin.Any
import kotlin.Long
import kotlin.String
import kotlin.Unit

public interface AppDatabaseQueries : Transacter {
  public fun <T : Any> getUser(mapper: (
    firstName: String?,
    lastName: String?,
    email: String?,
    image: String?,
    id: Long
  ) -> T): Query<T>

  public fun getUser(): Query<USER>

  public fun <T : Any> getPass(mapper: (
    name: String?,
    description: String?,
    icon: String?,
    time: String?,
    ready: String?,
    id: Long
  ) -> T): Query<T>

  public fun getPass(): Query<PASS>

  public fun <T : Any> getPassByName(name: String?, mapper: (
    name: String?,
    description: String?,
    icon: String?,
    time: String?,
    ready: String?,
    id: Long
  ) -> T): Query<T>

  public fun getPassByName(name: String?): Query<PASS>

  public fun insertUser(
    firstName: String?,
    lastName: String?,
    email: String?,
    image: String?
  ): Unit

  public fun insertPass(
    name: String?,
    description: String?,
    icon: String?,
    time: String?,
    ready: String?
  ): Unit

  public fun updatePassByTime(time: String?, name: String?): Unit

  public fun updatePassByReady(ready: String?, name: String?): Unit
}
