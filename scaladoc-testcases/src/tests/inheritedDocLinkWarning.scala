package tests
package inheritedDocLinkWarning

// Reproduction of https://github.com/scala/scala3/issues/20028
//
// `Parent` lives in a nested package and carries a doc comment referring to
// itself via the wiki link `[[Parent]]`. In the context of `Parent` this link
// resolves fine. However `Child`, which lives in the outer package and inherits
// the documented member, cannot see `Parent` by its simple name. When scaladoc
// re-resolves the inherited doc comment in `Child`'s context the `[[Parent]]`
// link fails to resolve and a spurious warning is emitted:
//
//   Couldn't resolve a member for the given link query: Parent
//
// (In the original report, using `enumeratum`, `Parent` was `enumeratum.Enum`
// and the warning read `No DRI found for query: Enum`.)
// Ideally no warning should be produced here at all.

package lib:
  trait Parent:
    /** Doc comment referring to [[Parent]] */
    def values: Any

class Child extends lib.Parent:
  def values = ???
