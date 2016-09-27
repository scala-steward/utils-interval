package object intervaltree {

  implicit def ordering[T <: Interval]: Ordering[IntervalTreeElement[T]] =
    Ordering.by((x: IntervalTreeElement[T]) => x.elem.from)

  type IntervalTree[T <: Interval] = Tree[IntervalTreeElement[T]]
}
