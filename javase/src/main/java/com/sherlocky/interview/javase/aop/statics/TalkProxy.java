package com.sherlocky.interview.javase.aop.statics;

import com.sherlocky.interview.javase.aop.ITalk;

public class TalkProxy implements ITalk {

    private ITalk talker;

    public TalkProxy(ITalk talker) {
        super();
        this.talker = talker;
    }

    @Override
    public void talk(String msg) {
        talker.talk(msg);
    }

}
