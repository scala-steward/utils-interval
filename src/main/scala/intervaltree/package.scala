import spire.algebra.Order

package object intervaltree {

  implicit def orderInt[C : Order, T <: GenericInterval[C]]: Order[IntervalTree.IntervalTreeElement[C,T]] =
    Order.by((x: IntervalTree.IntervalTreeElement[C,T]) => x.elem.from)

  type IntervalTree[C,T <: GenericInterval[C]] = Tree[IntervalTree.IntervalTreeElement[C,T]]
}
