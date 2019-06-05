### Redisson哨兵模式
#### 搭建

Redis集群主从复制（一主两从）搭建配置教程【Windows环境】
（https://blog.csdn.net/u010648555/article/details/79427606）

Redis高可用集群-哨兵模式（Redis-Sentinel）搭建配置教程【Windows环境】
（https://www.jianshu.com/p/af3180a32fdb）

### 说明
Redis 的 Sentinel 系统用于管理多个 Redis 服务器（instance），Redis 的 Sentinel 为Redis提供了高可用性。使用哨兵模式创建一个可以不用人为干预而应对各种故障的Redis部署。

该系统执行以下三个任务：

监控（Monitoring）：Sentinel会不断地检查你的主服务器和从服务器是否允许正常。

提醒（Notification）：当被监控的某个Redis服务器出现问题时，Sentinel可以通过API向管理员或者其他应用程序发送通知。
自动故障迁移（Automatic failover）: 

    （1）当一个主服务器不能正常工作时，Sentinel会开始一次自动故障迁移操作，他会将失效主服务器的其中一个从服务器升级为新的主服务器，并让失效主服务器的其他从服务器改为复制新的主服务器；
    （2）客户端试图连接失败的主服务器时，集群也会向客服端返回新主服务器的地址，是的集群可以使用新主服务器代替失效服务器。

#### sentinel的分布式特性
Redis Sentinel 是一个分布式系统， 你可以在一个架构中运行多个 Sentinel 进程（progress）， 这些进程使用流言协议（gossip protocols)来接收关于主服务器是否下线的信息， 并使用投票协议（agreement protocols）来决定是否执行自动故障迁移， 以及选择哪个从服务器作为新的主服务器。

单个sentinel进程来监控redis集群是不可靠的，当sentinel进程宕掉后(sentinel本身也有单点问题，single-point-of-failure)整个集群系统将无法按照预期的方式运行。所以有必要将sentinel集群，这样有几个好处：

有一些sentinel进程宕掉了，依然可以进行redis集群的主备切换；

如果只有一个sentinel进程，如果这个进程运行出错，或者是网络堵塞，那么将无法实现redis集群的主备切换（单点问题）;

如果有多个sentinel，redis的客户端可以随意地连接任意一个sentinel来获得关于redis集群中的信息

一个健壮的部署至少需要三个哨兵实例。

三个哨兵实例应该放置在客户使用独立方式确认故障的计算机或虚拟机中。例如不同的物理机或不同可用区域的虚拟机。【本次讲解是一个机器上进行搭建，和多级是一个道理