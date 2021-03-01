package intervaltree

import org.scalatest.funspec.{AnyFunSpec => FunSpec}
import org.scalatest.PrivateMethodTester
import org.scalatestplus.scalacheck.{ScalaCheckPropertyChecks => Checkers}
import org.scalacheck.Prop.BooleanOperators
import org.scalacheck.{Arbitrary, Gen}

// import cats.std.int._

class IntervalTreeSpec extends FunSpec with PrivateMethodTester with Checkers {

  case class TestInterval(from: Int, to: Int) extends Interval

  implicit def arbInterval[TestInterval] =
    Arbitrary {
      for {
        from <- Gen.choose(0, 500)
        to <- Gen.choose(from, 500)
      } yield {
        TestInterval(from, to)
      }
    }

  describe("tree") {
    it("contains went in") {
      forAll { (a: List[Int]) =>
        Tree.inOrder(Tree.toTree(a.sorted)()) == (a.sorted)
      }
    }
    it("size") {
      forAll { (a: List[Int]) =>
        Tree.length((Tree.toTree(a.sorted)())) == a.size
      }
    }
    it("balanced") {
      forAll { (a: List[Int]) =>
        Tree.isBalanced(Tree.toTree(a.sorted)())
      }
    }
    it("bst") {
      forAll { (a: List[Int]) =>
        val x = Tree.bstHolds(Tree.toTree(a.sorted)())
        if (!x) {
          print(Tree.toTree(a.sorted)()); println(a)
        }
        x
      }
    }
  }
  describe("intervaltree") {
    it("find") {
      forAll { (a: List[TestInterval], b: TestInterval) =>
        val sorted = a.sortBy(_.from)
        val tree = IntervalTree.makeTree[Int, TestInterval](sorted)
        val result = IntervalTree.lookup(b, tree)
        val resultByFilter = a.filter(_.intersects(b))
        val x = result.toSet == resultByFilter.toSet
        if (!x) {
          println(a)
          println(b)
          println(result)
          println(resultByFilter)
        }
        x
      }
    }
  }

}
