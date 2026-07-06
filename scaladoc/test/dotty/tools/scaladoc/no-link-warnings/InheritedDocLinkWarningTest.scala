package dotty.tools.scaladoc
package noLinkWarnings

import org.junit.Assert.assertEquals

// Reproduces https://github.com/scala/scala3/issues/20028
//
// A member documented in `lib.Parent` references its enclosing trait via the
// wiki link `[[Parent]]`. When `Child` (in the outer package) inherits that
// member, scaladoc re-resolves the inherited doc comment in `Child`'s context,
// where `Parent` is not visible by its simple name, and emits a spurious
// "Couldn't resolve a member for the given link query: Parent" warning.
class InheritedDocLinkWarningTest extends ScaladocTest("inheritedDocLinkWarning"):

  override def args = Scaladoc.Args(
    name = "test",
    tastyFiles = tastyFiles(name),
    output = getTempDir().getRoot,
    projectVersion = Some("1.0")
  )

  override def runTest = afterRendering {
    val diagnostics = summon[DocContext].compilerContext.reportedDiagnostics
    val linkWarnings = diagnostics.warningMsgs.filter(_.contains("link query"))
    assertEquals(
      "The inherited `[[Parent]]` link should currently produce exactly one spurious warning (see #20028)",
      List("Couldn't resolve a member for the given link query: Parent"),
      linkWarnings
    )
    assertNoErrors(diagnostics)
  }
