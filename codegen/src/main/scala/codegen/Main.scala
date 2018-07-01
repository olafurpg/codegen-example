package codegen
import org.scalafmt.internal.AssociatedTrivias
import scala.meta._
import scala.meta.internal.format.Comments._
import org.scalafmt.internal.TreePrinter

object Main {
  def main(args: Array[String]): Unit = {
    val originalTree =
      """
        |package a
        |
        |/** This is a docstring
        |  *
        |  * @param a is an int
        |  */
        |case class Foo(/* aaaa */ a: Int) { // trailing
        |
        |  /** This is a method */
        |  def d = a
        |}
      """.stripMargin.parse[Source].get
    val trivia = AssociatedTrivias(originalTree)
    val syntheticMethod =
      q"def b: Int = a".withLeadingComment("/** Returns a */\n")
    val syntheticMethod2 =
      q"def c: Int = a".withLeadingComment("/** Returns a again */\n")
    val transformedTree = originalTree.transform {
      case c: Defn.Class =>
        c.copy(
          templ = c.templ.copy(
            stats = c.templ.stats ++ List(syntheticMethod, syntheticMethod2)
          )
        )
    }

    val obtained = TreePrinter.print(transformedTree, trivia).render(100)
    println(obtained)
  }
}
