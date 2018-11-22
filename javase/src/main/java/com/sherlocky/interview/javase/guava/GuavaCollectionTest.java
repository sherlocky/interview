package com.sherlocky.interview.javase.guava;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.BiMap;
import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.Collections2;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Multiset;
import com.google.common.collect.MutableClassToInstanceMap;
import com.google.common.collect.ObjectArrays;
import com.google.common.collect.Sets;
import com.google.common.collect.Table;
import com.google.common.collect.TreeMultimap;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * guava Collections
 * @author zhangcx
 * @date 2018-05-11
 */
public class GuavaCollectionTest {

    @Test
    public void testCreate() {
        // old
        Map<String, Map<String, String>> oldmap = new HashMap<String, Map<String,String>>();
        List<List<Map<String, String>>> oldlist = new ArrayList<List<Map<String,String>>>();
        
        // guava
        Map<String, Map<String, String>> map = Maps.newHashMap();
        Map<String, Person> personMap = Maps.newHashMap();
        
        List<List<Map<String, String>>> list = Lists.newArrayList();
        List<Person> personList= Lists.newLinkedList();

        Set<Person> personSet= Sets.newHashSet();

        Integer[] intArrays= ObjectArrays.newArray(Integer.class, 10);
    }
    
    
    @Test
    public void testInit() {
        // old
        Set<String> oldset = new HashSet<String>();
        oldset.add("one");
        oldset.add("two");
        oldset.add("three");
        
        // guava
        Set<String> set = Sets.newHashSet("one", "two", "three");
        Set<Person> personSet2 = Sets.newHashSet(new Person(), new Person());
                
        List<String> list = Lists.newArrayList("one", "two", "three");
        List<Person> personList2 = Lists.newArrayList(new Person(), new Person());

        // 这样创建的 map 不可再修改
        Map<String, String> immutableMap = ImmutableMap.of("ON", "TRUE", "OFF", "FALSE");
        Map<String, Person> immutablePersonMap = ImmutableMap.of("hello", new Person(), "fuck", new Person());
        
        Map<String, String> map = Maps.newHashMap();
        map.put("1", "2");
    }    
    
    @Test
    public void testMutilMap() {
        // 一种key可以重复的map，子类有ListMultimap和SetMultimap，对应的通过key分别得到list和set
        Multimap<String, String> customersByType = ArrayListMultimap.create();
        // ArrayListMultimap
        customersByType.put("abc", "1");
        customersByType.put("abc", "2");
        customersByType.put("abc", "3");
        customersByType.put("abc", "4");
        customersByType.put("abc", "4");
        customersByType.put("abcd", "!");
        customersByType.put("abcde", "@");
        for (String val : customersByType.get("abc")) {
            // 可得到 list
            System.out.println(val);
        }
        /**********************************************/
        Multimap<String, String> treeMultimap = TreeMultimap.create();
        // TreeMultimap
        treeMultimap.put("abc", "1");
        treeMultimap.put("abc", "2");
        treeMultimap.put("abc", "3");
        treeMultimap.put("abc", "4");
        treeMultimap.put("abc", "4");
        treeMultimap.put("abcd", "!");
        treeMultimap.put("abcde", "@");
        for (String val : treeMultimap.get("abc")) {
            // 可得到 set
            System.out.println(val);
        }
    }
    // 分片集合
    @Test
    public void testChunkCollection() {
        Map<String, String> map = Maps.newHashMap(ImmutableMap.of("type", "blog", "id", "1", "author", "zhangsan"));
        Map<String, String> map2 = Maps.newHashMap(ImmutableMap.of("type", "blog", "id", "2", "author", "zhaoliu"));
        Map<String, String> map3 = Maps.newHashMap(ImmutableMap.of("type", "new", "id", "3", "author", "lisi"));
        Map<String, String> map4 = Maps.newHashMap(ImmutableMap.of("type", "code", "id", "4", "author", "wangwu"));
        // 假设我们已经拥有了包含了一组map的list。list里的每一个Map代表拥有指定属性的一个文档。这个Map看起来可能会是上面的样子
        List<Map<String, String>> listOfMaps = Lists.newArrayList(map, map2, map3, map4); 
        // 即每个Map中我们拥有3个属性，他们分别是“type”、 “id”和“author”。
        
        // 现在，我们想把这个list根据所装载对象的类型不同分成多个list，比如一个叫“blog”，一个叫“new”等等...
        Multimap<String, Map<String, String>> partitionedMap = Multimaps.index(listOfMaps,
                new Function<Map<String, String>, String>() {
                    public String apply(final Map<String, String> element) {
                        return element.get("type");
                    }
                });       
        // 现在我们拥有了每一个key代表不同类型的Multimaps了
        System.out.println(partitionedMap);
    }
    
    // 相当于有两个key的map
    @Test
    public void testTable() {
        Table<Integer, Integer, String> strTable = HashBasedTable.create();
        strTable.put(1, 20, "a");
        strTable.put(0, 30, "b");
        strTable.put(0, 25, "c");
        strTable.put(1, 50, "d");
        strTable.put(0, 27, "e");
        strTable.put(1, 29, "f");
        strTable.put(0, 33, "g");
        strTable.put(1, 66, "h");
        strTable.put(2, 20, "jhj");

        // 所有行 key set
        System.out.println(strTable.rowKeySet());
        // 得到行集合
        Map<Integer, String> rowMap = strTable.row(0);
        System.out.println(rowMap.keySet());
        
        // 所有列 key set
        System.out.println(strTable.columnKeySet());
        // 得到列集合
        Map<Integer, String> columnMap = strTable.column(20);
        System.out.println(columnMap.keySet());
    }

    // 双向map
    @Test
    public void testBitMap() {
        // 一一映射，可以通过key得到value，也可以通过value得到key；
        BiMap<Integer, String> biMap = HashBiMap.create();

        biMap.put(1, "hello");
        biMap.put(2, "helloa");
        biMap.put(3, "world");
        biMap.put(4, "worldb");
        biMap.put(5, "my");
        // biMap.put(5, "my2"); // key 重复：会覆盖 5 --> key
        biMap.put(6, "myc");
        // biMap.put(7, "my");  // value 重复：报异常 java.lang.IllegalArgumentException: value already present: my
        
        String value = biMap.get(5);
        System.out.println("5 -- (value) -->" + value);
        
        int key = biMap.inverse().get("my");
        System.out.println("my -- (key) -->" + key);

    }
    
    // 不是集合，可以增加重复的元素，并且可以统计出重复元素的个数，例子如下：
    @Test
    public void testMulitiSet() {
        Multiset<Integer> multiSet = HashMultiset.create();
        multiSet.add(10);
        multiSet.add(30);
        multiSet.add(30);
        multiSet.add(40);

        System.out.println(multiSet.count(30)); // 2
        System.out.println(multiSet.size()); // 4
    }
    
    // ClassToInstanceMap
    @Test
    public void testClassToInstanceMap() {
        /**
         * 有的时候，你的map的key并不是一种类型，他们是很多类型，你想通过映射他们得到这种类型，
         * guava提供了ClassToInstanceMap满足了这个目的。 继承自Map接口，ClassToInstaceMap提供了方法 T
         * getInstance(Class<T>) 和 T putInstance(Class<T>, T),消除了强制类型转换。
         */
        ClassToInstanceMap<Person> personMap = MutableClassToInstanceMap.create();
        Person person = new Person();
        System.out.println(person);
        personMap.putInstance(Person.class, person);
        // System.out.println("string:"+classToInstanceMap.getInstance(String.class)); // 编译报错
        Person person1 = personMap.getInstance(Person.class);
        System.out.println(person1);
        
        /**************************************/
        ClassToInstanceMap classToInstanceMap = MutableClassToInstanceMap.create();
        classToInstanceMap.putInstance(Person.class, new Person());
        classToInstanceMap.putInstance(String.class, "abcadmin");
        classToInstanceMap.putInstance(Integer.class, 125);
        Person person3 = (Person) classToInstanceMap.getInstance(Person.class);
        String str = (String) classToInstanceMap.getInstance(String.class);
        Integer in = (Integer) classToInstanceMap.getInstance(Integer.class);
        System.out.println("Person: " + person3);        
        System.out.println("String: " + str);
        System.out.println("Integer: " + in);
    }
    
    /**
     * 谓词（Predicate）是用来筛选集合的；<p>
     * 谓词是一个简单的接口，只有一个方法返回布尔值，但是他是一个很令人惊讶的集合方法，
     * 当你结合Collections2.filter方法使用，这个筛选方法返回原来的集合中满足这个谓词接口的元素
     */
    @Test
    public void testPredicates() {
        List<Integer> intList = Lists.newArrayList(1, 2, 3, 4, 5, 6);

        ImmutableMultiset<Integer> filterDestion = ImmutableMultiset.copyOf(Collections2.filter(intList, new Predicate<Integer>() {
            @Override
            public boolean apply(Integer input) {
                return input >= 3;
            }
        }));
        
        
        Optional<ImmutableMultiset<Integer>> optional = Optional.fromNullable(filterDestion);
        if (optional.isPresent()) {
            for (Integer i : optional.get()) {
                System.out.println("Int：" + i);
            }
        }
        System.out.println(optional.isPresent());
        // Predicates 含有一些内置的筛选方法，比如说 in ,and ,not等。。。
    }

    // 集合转换
    @Test
    public void testTransform() {
        List<Person> personList = Lists.newArrayList(new Person("zhang", "san", 123456),
                new Person("li", "si", 123456), new Person("wang", "wu", 123456),
                new Person("zhao", "liu", 123456), new Person("feng", "qi", 123456),
                new Person("chen", "ba", 123456), new Person("wei", "jiu", 123456));
        ImmutableMultiset<String> transform = ImmutableMultiset.copyOf(Lists.transform(personList, new Function<Person, String>() {
            @Override
            public String apply(Person input) {
                return input.getFirstName();
            }
        }));
        
        Optional<ImmutableMultiset<String>> optional = Optional.fromNullable(transform);
        if (optional.isPresent()) {
            for (String p : optional.get()) {
                System.out.println("姓氏：" + p);
            }
        }
        System.out.println(optional.isPresent());
    }
}
