package comjetbrainshandsonkmmsharedcache

import kotlin.Long
import kotlin.String

public data class USER(
  public val firstName: String?,
  public val lastName: String?,
  public val email: String?,
  public val image: String?,
  public val id: Long
) {
  public override fun toString(): String = """
  |USER [
  |  firstName: $firstName
  |  lastName: $lastName
  |  email: $email
  |  image: $image
  |  id: $id
  |]
  """.trimMargin()
}
