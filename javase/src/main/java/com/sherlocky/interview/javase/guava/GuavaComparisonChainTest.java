/**
 * 
 */
package com.sherlocky.interview.javase.guava;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;

/**
 * 替代繁琐的 Comparator 实现<p>
 * 它执行比较操作直至发现非零的结果，在那之后的比较输入将被忽略。
 * @author zhangcx
 * @date 2018-05-11
 */
public class GuavaComparisonChainTest {
    
}

// 新写法
class PersonNew implements Comparable<PersonNew> {
    private String lastName;
    private String firstName;
    private int zipCode;
    
    @Override
    public int compareTo(PersonNew that) {
        // 链式调用
        return ComparisonChain.start()
                .compare(this.lastName, that.lastName)
                .compare(this.firstName, that.firstName)
                .compare(this.zipCode, that.zipCode, Ordering.natural().nullsLast())
                .result();
    }
}

// 旧写法
class Person implements Comparable<Person> {
    private String lastName;
    private String firstName;
    private int zipCode;
    
    public Person() {
        super();
    }

    /**
     * @param firstName
     * @param lastName
     * @param zipCode
     */
    public Person(String firstName, String lastName, int zipCode) {
        super();
        this.lastName = lastName;
        this.firstName = firstName;
        this.zipCode = zipCode;
    }

    @Override
    public int compareTo(Person other) {
        int cmp = lastName.compareTo(other.lastName);
        if (cmp != 0) {
            return cmp;
        }
        cmp = firstName.compareTo(other.firstName);
        if (cmp != 0) {
            return cmp;
        }
        return Integer.compare(zipCode, other.zipCode);
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public int getZipCode() {
        return zipCode;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }
}