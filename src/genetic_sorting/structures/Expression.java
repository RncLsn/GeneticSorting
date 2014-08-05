package genetic_sorting.structures;

import genetic_sorting.operators.TreeNode;

import java.util.List;

/**
 * @author Alessandro Ronca
 */
public abstract class Expression extends TreeNode<Expression> implements Cloneable {

    abstract void init ();
    abstract int evaluate (List<Integer> list, int index);

    @Override
    public Object clone () throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    // todo removed for efficiency reason
//    @Override
//    public int hashCode () {
//        int hashCode = 0;
//        int base = 3;
//        int exp = 0;
//        for (TreeNode<Expression> expression : this) {
//            hashCode += Math.pow(base, exp++) * expression.getClass().hashCode();
//        }
//        return hashCode;
//    }

    // todo removed for efficiency reason
//    @Override
//    public boolean equals (Object obj) {
//        if(!obj.getClass().equals(getClass())) return false;
//        Expression that = (Expression) obj;
//        Iterator<TreeNode<Expression>> thatIterator = that.iterator();
//        Iterator<TreeNode<Expression>> thisIterator = this.iterator();
//
//        while(thisIterator.hasNext() && thatIterator.hasNext()) {
//            if(!thisIterator.next().getClass().equals(thatIterator.next().getClass())) {
//                return false;
//            }
//        }
//        if(thisIterator.hasNext() || thatIterator.hasNext()) return false;
//
//        return true;
//    }
}
