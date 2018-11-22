/**
 * 
 */
package com.sherlocky.interview.javase.guava;

import com.google.common.base.Function;
import com.google.common.collect.Ordering;
import com.sun.istack.internal.Nullable;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 排序功能 ：
    链式调用方法：通过链式调用，可以由给定的排序器衍生出其它排序器
 * @author zhangcx
 * @date 2018-05-11
 */
public class GuavaOrderingTest {
    /**
        reverse()   获取语义相反的排序器
        nullsFirst()    使用当前排序器，但额外把null值排到最前面。
        nullsLast() 使用当前排序器，但额外把null值排到最后面。
        compound(Comparator)    合成另一个比较器，以处理当前排序器中的相等情况。
        lexicographical()   基于处理类型T的排序器，返回该类型的可迭代对象Iterable<T>的排序器。
        onResultOf(Function)    对集合中元素调用Function，再按返回值用当前排序器排序。
     */
    @Test
    public void testOrdering() {
        List<Integer> numbers = new ArrayList<Integer>();
        numbers.add(new Integer(5));
        numbers.add(new Integer(2));
        numbers.add(new Integer(15));
        numbers.add(new Integer(51));
        numbers.add(new Integer(53));
        numbers.add(new Integer(35));
        numbers.add(new Integer(45));
        numbers.add(new Integer(32));
        numbers.add(new Integer(43));
        numbers.add(new Integer(16));
        
        Ordering o1 = Ordering.natural();
        System.out.println("Input List: ");
        System.out.println(numbers);
        System.out.println("Minimum: " + o1.min(numbers));
        System.out.println("Maximum: " + o1.max(numbers));
        
        Collections.sort(numbers, o1 );
        System.out.println("Sorted List: ");
        System.out.println(numbers);
        
        System.out.println("======================");
        System.out.println("List is sorted: " + o1.isOrdered(numbers));
        System.out.println("Minimum: " + o1.min(numbers));
        System.out.println("Maximum: " + o1.max(numbers));

        Collections.sort(numbers, o1.reverse());
        System.out.println("Reverse: " + numbers);

        numbers.add(null);
        System.out.println("Null added to Sorted List: ");
        System.out.println(numbers);

        Collections.sort(numbers, o1.nullsFirst());
        System.out.println("Null first Sorted List: ");
        System.out.println(numbers);
        System.out.println("======================");

        List<String> names = new ArrayList<String>();
        names.add("Ram");
        names.add("Shyam");
        names.add("Mohan");
        names.add("Sohan");
        names.add("Ramesh");
        names.add("Suresh");
        names.add("Naresh");
        names.add("Mahesh");
        names.add(null);
        names.add("Vikas");
        names.add("Deepak");

        System.out.println("Another List: ");
        System.out.println(names);

        Collections.sort(names, o1.nullsFirst().reverse());
        System.out.println("Null first then reverse sorted list: ");
        System.out.println(names);
        
        /***************** 还可以支持复杂的链式调用 ******************/
        /**
         * 当阅读链式调用产生的排序器时，应该从后往前读。
         * 上面的例子中，排序器首先调用apply方法获取sortedBy值，并把sortedBy为null的元素都放到最前面
         * 然后把剩下的元素按sortedBy进行自然排序。
         * 之所以要从后往前读，是因为每次链式调用都是用后面的方法包装了前面的排序器。
         */
        Ordering<Foo> ordering = Ordering.natural().nullsFirst().onResultOf(new Function<Foo, String>() {
            public String apply(Foo foo) {
                return foo.sortedBy;
            }
        });
        /** 
         * ！！！注意！！！
         * 用compound方法包装排序器时，就不应遵循从后往前读的原则。
         * 为了避免理解上的混乱，请不要把compound写在一长串链式调用的中间，你可以另起一行，
         * 在链中最先或最后调用compound。
         */
         /**
          * 超过一定长度的链式调用，也可能会带来阅读和理解上的难度。
          * 我们建议按下面的代码这样，在一个链中最多使用三个方法。
          * 此外，你也可以把Function分离成中间对象，让链式调用更简洁紧凑。
          */
        Function sortKeyFunction = new Function<Foo, String>() {
            public String apply(Foo foo) {
                return foo.sortedBy;
            }
        };
        Ordering<Foo> ordering2 = Ordering.natural().nullsFirst().onResultOf(sortKeyFunction);
    }
}

class Foo {
    @Nullable String sortedBy;
    int notSortedBy;
}