## 分支说明
这个分支我们尝试集群的配置，主要是想把luttuce和redisson的结合起来使用。

redisson 主要用于分布式锁相关的业务逻辑，可能去加个AOP对部分功能进行重复操作的拦截。

luttuce 主要用于rdis基本操作的场景以及键空间通知，因为redisson对基本的数据存储支持的不是很好，所以有此合并。

luttuce 和 redisson都是线程安全的，原则上说可以不使用线程池，实际是本着防范于未然这个原则，
我们还是要加上这个。

## 可以试试
luttuce和redisson配置文件的合并
redisson存储的key是乱码的，不知道怎么序列化存储进去，
可能封装之后锁相关的操作不能自定义序列化（可能是通过配置文件序列化）

## 疑问
和单机版一样同样是有那个链接主机强制关闭的问题，猜测可能是服务器的配置和项目的配置有哪些地方不是很和谐

2019-06-11 17:25:10.931 ERROR 9592 --- [isson-netty-4-1] o.redisson.client.handler.CommandsQueue  : Exception occured. Channel: [id: 0x54ab159c, L:/127.0.0.1:54691 - R:127.0.0.1/127.0.0.1:20002]

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