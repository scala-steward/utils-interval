package intervaltree

trait Interval {
  def from: Int
  def to: Int
  def size = to - from
  def intersects[T <: Interval](that: T): Boolean =
    that.size > 0 && this.size > 0 && (that.from < this.to && this.from < that.to)
}

case class IntervalWithPayLoad[T](from: Int, to: Int, payload: T)
    extends Interval

case class IntervalTreeElement[T <: Interval](elem: T, max: Int)

object IntervalTree {

  def makeTree[T <: Interval](i: List[T]): Tree[IntervalTreeElement[T]] =
    Tree.toTree(i.map(i => IntervalTreeElement(i, i.to))) {
      (elem, left, right) =>
        (elem, left, right) match {
          case (elem, None, None) => elem
          case (elem, Some(x), None) => elem.copy(max = (elem.max max x.max))
          case (elem, None, Some(x)) => elem.copy(max = (elem.max max x.max))
          case (elem, Some(x), Some(y)) =>
            elem.copy(max = ((elem.max max x.max) max y.max))
        }
    }

  def lookup[Q <: Interval, T <: Interval](query: Q,
                                           tree: IntervalTree[T]): List[T] = {
    if (query.size == 0) Nil
    else
      tree match {
        case EmptyTree => Nil
        case NonEmptyTree(IntervalTreeElement(interval, maxTo), left, right) =>
          if (maxTo < query.from) Nil
          else if (interval.from >= query.to) lookup(query, left)
          else {
            val hit =
              if (interval.size > 0 && interval.from < query.to && interval.to > query.from)
                Some(interval)
              else None
            hit.toList ::: (lookup(query, left) ::: lookup(query, right))
          }
      }

  }

  def intervalForest[T <: Interval](
      in: Iterator[(String, T)]): Map[String, IntervalTree[T]] = {

    val trees = scala.collection.mutable.Map[String, List[T]]()

    in.foreach {
      case (label, interval) =>
        trees.get(label) match {
          case None => trees.update(label, List(interval))
          case Some(x) => trees.update(label, interval :: x)
        }
    }

    trees.map(x => x._1 -> IntervalTree.makeTree(x._2)).toMap

  }

}
