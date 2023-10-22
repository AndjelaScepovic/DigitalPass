package comjetbrainshandsonkmmsharedcache

import kotlin.Long
import kotlin.String

public data class PASS(
  public val name: String?,
  public val description: String?,
  public val icon: String?,
  public val time: String?,
  public val ready: String?,
  public val id: Long
) {
  public override fun toString(): String = """
  |PASS [
  |  name: $name
  |  description: $description
  |  icon: $icon
  |  time: $time
  |  ready: $ready
  |  id: $id
  |]
  """.trimMargin()
}
