package nz.co.bottech

import org.scalatest.flatspec.AnyFlatSpec

import scala.meta._

class TypeResolverSpec extends AnyFlatSpec {
  behavior of "TypeResolver"

  it should "resolve the type of a val with an explicit type" in {
    val tree: Defn.Val = q"""val x: Int = 123"""
    TypeResolver.resolveType()
  }
}
