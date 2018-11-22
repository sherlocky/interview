/**
 * 
 */
package com.sherlocky.interview.javase.fastjson;

import com.alibaba.fastjson.JSON;
import com.sherlocky.interview.javase.fastjson.bean.FastjsonBugBean;

/**
 * 1.2.28版本反序列化的时候使用ASM报错   java.lang.VerifyError (字段数刚好为32或64个才能复现)
 * @author zhangcx
 * @date 2018-01-11
 */
public class FastjsonBugTest {

    public static void main(String[] args) {
        FastjsonBugBean b = new FastjsonBugBean("key1", "key2", "key3", "key4", "key5", "key6", "key7", "key8", "key9",
                "key10", "key11", "key12", "key13", "key14", "key15", "key16", "key17", "key18", "key19", "key20",
                "key21", "key22", "key23", "key24", "key25", "key26", "key27", "key28", "key29", "key30", "key31",
                "key32");   
        String s = JSON.toJSONString(b, true);
        System.out.println(s);
        // 1.升级 1.2.29 （改版本已经修复）
        // 2.反序列化前手动关闭ASM 可以解决
        // ParserConfig.getGlobalInstance().setAsmEnable(false);
        // 3.将 FastjsonBugBean 字段数改为非 32 和 64 也可以解决
        System.out.println(JSON.parseObject(s, FastjsonBugBean.class));
    }
}
