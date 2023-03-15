package com.ling.basic.demo04;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * @author zhangling  2021/5/17 20:56
 */
@Slf4j
public class JdkFutureTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        // 1. 线程池
        ExecutorService service = Executors.newFixedThreadPool(2);

        // 2. 提交任务
        Future<Integer> future = service.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                log.info("执行计算");
                Thread.sleep(1000);
                return 50;
            }
        });
        // 3. 主线程通过 future 来获取结果(阻塞同步等待)
        log.info("等待结果");
        log.info("结果是 {}",future.get());

    }
}
