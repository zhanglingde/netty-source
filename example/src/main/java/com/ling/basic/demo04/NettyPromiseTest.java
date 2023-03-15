package com.ling.basic.demo04;

import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultPromise;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;

/**
 * @author zhangling  2021/5/17 22:13
 */
@Slf4j
public class NettyPromiseTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        EventLoop eventLoop = new NioEventLoopGroup().next();

        // 主动创建 Promise，结果容器
        DefaultPromise<Object> promise = new DefaultPromise<>(eventLoop);
        new Thread(() -> {
            // 任意一个线程执行计算，计算完毕后向 promise 填充结果
            log.info("开始计算");
            try {
                // int i = 1 / 0;
                Thread.sleep(1000);
                promise.setSuccess(80);
            } catch (InterruptedException e) {
                promise.setFailure(e);
                e.printStackTrace();
            }
        }).start();


        log.info("等待结果");
        log.info("结果是：{}", promise.get());  // 阻塞等待结果
    }
}
