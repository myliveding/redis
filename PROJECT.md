## 分支说明
这个分支我们尝试集群的配置，主要是想把luttuce和redisson的结合起来使用。

redisson 主要用于分布式锁相关的业务逻辑，可能去加个AOP对部分功能进行重复操作的拦截。

luttuce 主要用于rdis基本操作的场景以及键空间通知，因为redisson对基本的数据存储支持的不是很好，所以有此合并。

luttuce 和 redisson都是线程安全的，原则上说可以不使用线程池，实际是本着防范于未然这个原则，
我们还是要加上这个。


## 疑问
这个网站上面的问题跟我的差不多，但是一直没有回复：
https://www.v2ex.com/t/542261

下面是我运行之后的异常信息，但是不报错~
2019-06-10 14:23:54.901 ERROR 13276 --- [sson-netty-6-24] o.redisson.client.handler.CommandsQueue  : Exception occured. Channel: [id: 0xaa31a78c, L:/127.0.0.1:55217 - R:127.0.0.1/127.0.0.1:6379]

java.io.IOException: 远程主机强迫关闭了一个现有的连接。
	at sun.nio.ch.SocketDispatcher.read0(Native Method)
	at sun.nio.ch.SocketDispatcher.read(SocketDispatcher.java:43)
	at sun.nio.ch.IOUtil.readIntoNativeBuffer(IOUtil.java:223)
	at sun.nio.ch.IOUtil.read(IOUtil.java:192)
	at sun.nio.ch.SocketChannelImpl.read(SocketChannelImpl.java:380)
	at io.netty.buffer.PooledUnsafeDirectByteBuf.setBytes(PooledUnsafeDirectByteBuf.java:288)
	at io.netty.buffer.AbstractByteBuf.writeBytes(AbstractByteBuf.java:1125)
	at io.netty.channel.socket.nio.NioSocketChannel.doReadBytes(NioSocketChannel.java:347)
	at io.netty.channel.nio.AbstractNioByteChannel$NioByteUnsafe.read(AbstractNioByteChannel.java:148)
	at io.netty.channel.nio.NioEventLoop.processSelectedKey(NioEventLoop.java:682)
	at io.netty.channel.nio.NioEventLoop.processSelectedKeysOptimized(NioEventLoop.java:617)
	at io.netty.channel.nio.NioEventLoop.processSelectedKeys(NioEventLoop.java:534)
	at io.netty.channel.nio.NioEventLoop.run(NioEventLoop.java:496)
	at io.netty.util.concurrent.SingleThreadEventExecutor$5.run(SingleThreadEventExecutor.java:906)
	at io.netty.util.internal.ThreadExecutorMap$2.run(ThreadExecutorMap.java:74)
	at io.netty.util.concurrent.FastThreadLocalRunnable.run(FastThreadLocalRunnable.java:30)
	at java.lang.Thread.run(Thread.java:745)
