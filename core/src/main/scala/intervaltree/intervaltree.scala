package intervaltree

import cats.kernel.Order

trait GenericInterval[@specialized(Int, Long, Double) C] {
  def from: C
  def to: C
  def isEmpty: Boolean
  def nonEmpty = !isEmpty
  def intersects[T <: GenericInterval[C]](that: T): Boolean
}

trait Interval extends GenericInterval[Int] {
  def from: Int
  def to: Int
  def size = to - from
  def isEmpty = size == 0
  def intersects[T <: GenericInterval[Int]](that: T): Boolean =
    that.nonEmpty && this.nonEmpty && (that.from < this.to && this.from < that.to)
}

trait LongInterval extends GenericInterval[Long] {
  def from: Long
  def to: Long
  def size = to - from
  def isEmpty = size == 0L
  def intersects[T <: GenericInterval[Long]](that: T): Boolean =
    that.nonEmpty && this.nonEmpty && (that.from < this.to && this.from < that.to)
}

case class IntervalWithPayLoad[@specialized(Int, Long, Double, Float) T](
    from: Int,
    to: Int,
    payload: T
) extends Interval

case class LongIntervalWithPayLoad[@specialized(Int, Long, Double, Float) T](
    from: Long,
    to: Long,
    payload: T
) extends LongInterval

object IntervalTree {

  case class IntervalTreeElement[
      @specialized(Int, Long, Double) C,
      T <: GenericInterval[C]
  ](
      elem: T,
      max: C
  )

  def makeTree[@specialized(Int, Long, Double) C, T <: GenericInterval[C]](
      i: List[T]
  )(implicit ord: Order[C]): Tree[IntervalTreeElement[C, T]] =
    Tree.toTree(i.map(i => IntervalTreeElement(i, i.to))) {
      (elem, left, right) =>
        (elem, left, right) match {
          case (elem, None, None) => elem
          case (elem, Some(x), None) =>
            elem.copy(max = ord.max(elem.max, x.max))
          case (elem, None, Some(x)) =>
            elem.copy(max = ord.max(elem.max, x.max))
          case (elem, Some(x), Some(y)) =>
            elem.copy(max = ord.max(ord.max(elem.max, x.max), y.max))
        }
    }

  def lookup[@specialized(Int, Long, Double) C: Order, Q <: GenericInterval[
    C
  ], T <: GenericInterval[
    C
  ]](
      query: Q,
      tree: IntervalTree[C, T]
  ): List[T] =
    if (query.isEmpty) Nil
    else
      lookup1(query.from, query.to, tree)

  def lookup1[@specialized(Int, Long, Double) C: Order, T <: GenericInterval[
    C
  ]](
      queryFrom: C,
      queryTo: C,
      tree: IntervalTree[C, T]
  ): List[T] = {
    val ord = implicitly[Order[C]]
    tree match {
      case EmptyTree => Nil
      case NonEmptyTree(IntervalTreeElement(interval, maxTo), left, right) =>
        if (ord.lt(maxTo, queryFrom)) Nil
        else if (ord.gteqv(interval.from, queryTo))
          lookup1(queryFrom, queryTo, left)
        else {
          val hit =
            if (
              interval.nonEmpty && ord.lt(interval.from, queryTo) && ord
                .gt(interval.to, queryFrom)
            )
              Some(interval)
            else None
          hit.toList ::: (lookup1(queryFrom, queryTo, left) ::: lookup1(
            queryFrom,
            queryTo,
            right
          ))
        }
    }

  }

  def intervalForest[
      @specialized(Int, Long, Double) C: Order,
      T <: GenericInterval[C]
  ](
      in: Iterator[(String, T)]
  ): Map[String, IntervalTree[C, T]] = {

    val trees = scala.collection.mutable.Map[String, List[T]]()

    in.foreach { case (label, interval) =>
      trees.get(label) match {
        case None    => trees.update(label, List(interval))
        case Some(x) => trees.update(label, interval :: x)
      }
    }

    trees.map(x => x._1 -> IntervalTree.makeTree[C, T](x._2)).toMap

  }

}
